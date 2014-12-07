package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.FileIO;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

import java.util.*;

/**
 * Created by Jonathan on 2014/10/30.
 */
public class BeingQuestionCreator implements QuestionCreator {
    private static final boolean test = true;

    // Naive implementation. Only sentence rearrangement, yes is always the answer.
    // Assuming all sentences with nn roots are being sentences with the root being the object of the being verb
    // ex. Bob is sick. Sick = nn root. Bob = nsubj related nnp/nn child, is = cop related vbz child
    public Set<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("createQuestion called");
        Set<Question> questions = new HashSet<Question>();
        for (CoreMap sentence : sentences) {
            try {
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                for (IndexedWord root : dependencies.getRoots()) {
                    if (test) System.out.println("root found: " + root.toString());
                    if (root.tag().equalsIgnoreCase("nn")
                            || root.tag().equalsIgnoreCase("nns")
                            || root.tag().equalsIgnoreCase("nnp")
                            || root.tag().equalsIgnoreCase("nnps")
                            || root.tag().equalsIgnoreCase("prp")
                            || root.tag().equalsIgnoreCase("prp$")
                            || root.tag().equalsIgnoreCase("jj")) {
                        List<Pair<GrammaticalRelation, IndexedWord>> children = dependencies.childPairs(root);

                        // Create trees for sentence fragments
                        TreeMap<Integer, IndexedWord> subject = new TreeMap<Integer, IndexedWord>();
                        TreeMap<Integer, IndexedWord> predicateVerb = new TreeMap<Integer, IndexedWord>(); // Do I need more than one word for verb?
                        TreeMap<Integer, IndexedWord> predicateObject = new TreeMap<Integer, IndexedWord>();

                        // Create roots to find related words
                        IndexedWord subjectRoot = new IndexedWord();
                        IndexedWord predicateVerbRoot = new IndexedWord();
                        IndexedWord predicateObjectRoot = root;

                        // Create predicateObject and roots
                        Stack<SemanticGraphEdge> edgeStack = new Stack<SemanticGraphEdge>();
                        for (SemanticGraphEdge edge : dependencies.outgoingEdgeList(root)) {
                            edgeStack.push(edge);
                        }
                        while (!edgeStack.empty()) {
                            SemanticGraphEdge edge = edgeStack.pop();

                            String relation = edge.getRelation().getShortName();
                            if (test) System.out.println("Pop from pobj stack: " + edge.getRelation().toString() + " " + edge.getTarget().toString());
                            if (relation.equalsIgnoreCase("nsubj")) {
                                subjectRoot = edge.getTarget();
                            } else if (relation.equalsIgnoreCase("cop")) {
                                predicateVerbRoot = edge.getTarget();
                            } else if (edge.getTarget().tag().equalsIgnoreCase("nn") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nns") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nnp") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nnps") ||
                                    edge.getTarget().tag().equalsIgnoreCase("prp") ||
                                    edge.getTarget().tag().equalsIgnoreCase("prp$") ||
                                    edge.getTarget().tag().equalsIgnoreCase("pos") ||
                                    relation.equalsIgnoreCase("amod") ||
                                    relation.equalsIgnoreCase("det")) {
                                if (test)
                                    System.out.println("Inserting into predicateObject graph: " + edge.getTarget().toString());
                                predicateObject.put(edge.getTarget().index(), edge.getTarget());
                                for(SemanticGraphEdge child : dependencies.outgoingEdgeList(edge.getTarget())) {
                                    System.out.println("Putting into pobj stack " + child.getRelation().toString() + " " + child.getTarget().toString());
                                    edgeStack.push(child);
                                }
                            }
                        }

                        // Create subject
                        while (!edgeStack.empty()) {
                            edgeStack.pop();
                        }
                        for (SemanticGraphEdge edge : dependencies.outgoingEdgeList(subjectRoot)) {
                            edgeStack.push(edge);
                        }
                        while (!edgeStack.empty()) {
                            SemanticGraphEdge edge = edgeStack.pop();

                            String relation = edge.getRelation().getShortName();
                            if (test) System.out.println("Pop from subject stack: " + edge.getRelation().toString() + " " + edge.getTarget().toString());
                            if (edge.getTarget().tag().equalsIgnoreCase("nn") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nns") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nnp") ||
                                    edge.getTarget().tag().equalsIgnoreCase("nnps") ||
                                    edge.getTarget().tag().equalsIgnoreCase("prp") ||
                                    edge.getTarget().tag().equalsIgnoreCase("prp$") ||
                                    edge.getTarget().tag().equalsIgnoreCase("pos")) {
                                if (test)
                                    System.out.println("Inserting into subj graph: " + edge.getTarget().toString());
                                subject.put(edge.getTarget().index(), edge.getTarget());
                                for(SemanticGraphEdge child : dependencies.outgoingEdgeList(edge.getTarget())) {
                                    System.out.println("Putting into subj stack " + child.getRelation().toString() + " " + child.getTarget().toString());
                                    edgeStack.push(child);
                                }
                            }
                        }

                        // Put roots in trees
                        subject.put(subjectRoot.index(), subjectRoot);
                        predicateVerb.put(predicateVerbRoot.index(), predicateVerbRoot);
                        predicateObject.put(root.index(), root);

                        // Create question
                        String question = predicateVerbRoot.value().substring(0,1).toUpperCase() + predicateVerbRoot.value().substring(1) + " ";
                        if (dependencies.getNodeByIndex(1).ner().equalsIgnoreCase("o")) {
                            if (test) System.out.println("Inserting second word into question: " + dependencies.getNodeByIndex(1).value());
                            question += dependencies.getNodeByIndex(1).value().toLowerCase() + " ";
                            subject.remove(1);
                        }
                        for (IndexedWord word : subject.values()) {
                            List<IndexedWord> newSubj = PronounReplacer.replacePronoun(word, sentences, coreferences);
                            for (IndexedWord newWord : newSubj) {
                                question += newWord.value() + " ";
                            }
                        }
                        for (IndexedWord word : predicateObject.values()) {
                            List<IndexedWord> newSubj = PronounReplacer.replacePronoun(word, sentences, coreferences);
                            for (IndexedWord newWord : newSubj) {
                                question += newWord.value() + " ";
                            }
                        }
                        question = question.trim() + "?";
                        final String questionClone = question;
                        final String sentenceClone = sentence.toString();

                        questions.add(new Question() {
                            final String q = questionClone;
                            final String a = "yes";
                            final String s = sentenceClone;

                            public String getQuestion() {
                                return q;
                            }

                            public String getAnswer() {
                                return a;
                            }

                            public String getSentence() { return s; }
                        });
                    }
                }
            }
            catch (Exception e) { FileIO.writeToLog(e); }
        }
        return questions;
    }
}
