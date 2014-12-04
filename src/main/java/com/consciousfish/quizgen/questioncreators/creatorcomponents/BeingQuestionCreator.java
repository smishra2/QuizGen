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
    private static final boolean test = false;

    // Naive implementation. Only sentence rearrangement, yes is always the answer.
    // Assuming all sentences with nn roots are being sentences with the root being the object of the being verb
    // ex. Bob is sick. Sick = nn root. Bob = nsubj related nnp/nn child, is = cop related vbz child
    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for (CoreMap sentence : sentences) {
            try {
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

                for (IndexedWord root : dependencies.getRoots()) {
                    if (test) System.out.println("root found: " + root.toString());
                    if (root.tag().equalsIgnoreCase("nn") || root.tag().equalsIgnoreCase("jj")) {
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
                        Iterator<SemanticGraphEdge> relationIter = dependencies.outgoingEdgeIterator(root);
                        while (relationIter.hasNext()) {
                            SemanticGraphEdge edge = relationIter.next();
                            if (test) System.out.println("Edge found: " + edge.toString());

                            String relation = edge.getRelation().getShortName();
                            if (relation.equalsIgnoreCase("nsubj")) {
                                subjectRoot = edge.getTarget();
                            } else if (relation.equalsIgnoreCase("cop")) {
                                predicateVerbRoot = edge.getTarget();
                            } else if (relation.equalsIgnoreCase("nn") || relation.equalsIgnoreCase("amod") ||
                                    relation.equalsIgnoreCase("det")) {
                                if (test)
                                    System.out.println("Inserting into predicateObject graph: " + edge.getTarget().toString());
                                predicateObject.put(edge.getTarget().index(), edge.getTarget());
                            }
                        }

                        // Create subject
                        relationIter = dependencies.outgoingEdgeIterator(subjectRoot);
                        while (relationIter.hasNext()) {
                            SemanticGraphEdge edge = relationIter.next();

                            String relation = edge.getRelation().getShortName();
                            if (relation.equalsIgnoreCase("nn")) {
                                if (test)
                                    System.out.println("Inserting into subject graph: " + edge.getTarget().toString());
                                subject.put(edge.getTarget().index(), edge.getTarget());
                            }
                        }

                        // Put roots in trees
                        subject.put(subjectRoot.index(), subjectRoot);
                        predicateVerb.put(predicateVerbRoot.index(), predicateVerbRoot);
                        predicateObject.put(root.index(), root);

                        // Create question
                        String question = "" + predicateVerbRoot.value() + " ";
                        for (IndexedWord word : subject.values()) {
                            question += word.value() + " ";
                        }
                        for (IndexedWord word : predicateObject.values()) {
                            question += word.value() + " ";
                        }
                        question = question.trim() + "?";
                        final String questionClone = question;

                        questions.add(new Question() {
                            final String q = questionClone;
                            final String a = "yes";

                            public String getQuestion() {
                                return q;
                            }

                            public String getAnswer() {
                                return a;
                            }
                        });
                    }
                }
            }
            catch (Exception e) { FileIO.writeToLog(e.getMessage()); }
        }
        return questions;
    }
}
