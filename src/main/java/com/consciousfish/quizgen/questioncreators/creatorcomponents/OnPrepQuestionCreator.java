package com.consciousfish.quizgen.questioncreators.creatorcomponents;

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
    private static final boolean test = true;

    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if(test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for(CoreMap sentence : sentences) {
            if(test) System.out.println("Parsing sentence: " + sentence.toString());
            Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

            for (SemanticGraphEdge startEdge : dependencies.findAllRelns(EnglishGrammaticalRelations.PREPOSITIONAL_MODIFIER)) {
                if (test) System.out.println("startEdge found: " + startEdge.toString());
                if (startEdge.getTarget().word().equalsIgnoreCase("on") || true) {
                    // Create tree for sentence
                    TreeMap<Integer, IndexedWord> extract = new TreeMap<Integer, IndexedWord>();
                    IndexedWord extractRoot = startEdge.getSource();
                    IndexedWord onPrep = startEdge.getTarget();

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
                        }
                        else {
                            question += extract.get(index).value() + " ";
                        }
                    }
                    question = question.trim() + "?";
                    final String questionClone = question;

                    if (!prepType.equalsIgnoreCase("date")) {
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
        }
        return questions;
    }
}
