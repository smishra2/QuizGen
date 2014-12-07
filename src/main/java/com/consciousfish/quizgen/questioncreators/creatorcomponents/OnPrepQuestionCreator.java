package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.FileIO;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

import java.util.*;

/**
 * Created by Jonathan on 2014/10/30.
 */
public class OnPrepQuestionCreator implements QuestionCreator {
    private static final boolean test = false;

    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for (CoreMap sentence : sentences) {
            try {
                if (test) System.out.println("Parsing sentence: " + sentence.toString());
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                for (SemanticGraphEdge startEdge : dependencies.findAllRelns(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)) {
                    if (test) System.out.println("startEdge found: " + startEdge.toString());
                    if (startEdge.getTarget().word().equalsIgnoreCase("on") || true) {
                        // Create tree for sentence
                        TreeMap<Integer, IndexedWord> extract = new TreeMap<Integer, IndexedWord>();
                        IndexedWord extractRoot = startEdge.getSource();
                        IndexedWord onPrep = startEdge.getTarget();

                        /*Stack<SemanticGraphEdge> edgeStack = new Stack<SemanticGraphEdge>();
                        for (SemanticGraphEdge edge : dependencies.getOutEdgesSorted(dependencies.getFirstRoot())) {
                            if (!edge.getRelation().isAncestor(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)
                                    || edge.getTarget().equals(onPrep)) {
                                edgeStack.push(edge);
                            }
                        }
                        if (test) System.out.println("Stack created with root");
                        extract.put(extractRoot.index(), extractRoot);

                        while (!edgeStack.empty()) {
                            SemanticGraphEdge edge = edgeStack.pop();
                            if (test) System.out.println("Testing edge: " + edge.toString());
                            if (!edge.getRelation().isAncestor(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)) {
                                if (!edge.getRelation().isAncestor(EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT)) {
                                    if (test)
                                        System.out.println("Inserting into extract: " + edge.getTarget().toString());
                                    extract.put(edge.getTarget().index(), edge.getTarget());
                                }
                                for (SemanticGraphEdge child : dependencies.getOutEdgesSorted(edge.getTarget())) {
                                    edgeStack.push(child);
                                }
                            }
                        }*/

                        for (IndexedWord word : dependencies.vertexSet()) {
                            if (!dependencies.descendants(onPrep).contains(word)) {
                                extract.put(word.index(), word);
                            }
                        }

                        extract.put(onPrep.index(), onPrep);

                        // Insert word "on" and root, note location of pobj
                        //extract.put(onPrep.index(), onPrep);
                        //extract.put(extractRoot.index(), extractRoot);
                        int pobjIndex = -1;
                        String prepType = "";

                        for (SemanticGraphEdge edge : dependencies.outgoingEdgeIterable(onPrep)) {
                            if (edge.getRelation().isAncestor(EnglishGrammaticalRelations.PREPOSITIONAL_OBJECT)) {
                                pobjIndex = edge.getTarget().index();
                                prepType = edge.getTarget().backingLabel().get(CoreAnnotations.NamedEntityTagAnnotation.class);
                            }
                        }

                        // Create question

                        String question = "";
                        for (Integer index : extract.keySet()) {
                            if (index.compareTo(onPrep.index()) == 0) {
                                question += extract.get(index).value() + " what ";
                            } else {
                                question += extract.get(index).value() + " ";
                            }
                        }
                        question = question.trim() + "?";
                        final String questionClone = question;
                        final String sentenceClone = sentence.toString();

                        if (!prepType.equalsIgnoreCase("date")) {
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
            }
            catch (Exception e) { FileIO.writeToLog(e); }
        }
        return questions;
    }
}
