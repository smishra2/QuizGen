package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.FileIO;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Jonathan on 2014/12/03.
 */
public abstract class AdvModifierQuestionCreator implements QuestionCreator {
    private static final boolean test = true;

    protected abstract String marker();
    protected abstract String replacement();

    public final List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for (CoreMap sentence : sentences) {
            try {
                SemanticGraph dependences = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
                for (SemanticGraphEdge advclEdge : dependences.findAllRelns(EnglishGrammaticalRelations.ADV_CLAUSE_MODIFIER)) {
                    // Check if because is used
                    for (SemanticGraphEdge markerEdge : dependences.outgoingEdgeList(advclEdge.getTarget())) {
                        if (!markerEdge.getRelation().isAncestor(EnglishGrammaticalRelations.MARKER) || !markerEdge.getTarget().value().equalsIgnoreCase(marker())) {
                            continue;
                        }

                        // Make sentence
                        TreeMap<Integer, IndexedWord> extract = new TreeMap<Integer, IndexedWord>();
                        IndexedWord markerWord = markerEdge.getTarget();

                        extract.put(markerWord.index(), markerWord);

                        // Go through tree using DFS, ignore advcl children except for marker.
                        Stack<IndexedWord> wordStack = new Stack<IndexedWord>();
                        wordStack.push(dependences.getFirstRoot());
                        while (!wordStack.empty()) {
                            IndexedWord currentWord = wordStack.pop();
                            extract.put(currentWord.index(), currentWord);
                            if (test) System.out.println("Adding [" + currentWord.toString() + "] to sentence.");
                            for (SemanticGraphEdge outEdge : dependences.outgoingEdgeList(currentWord)) {
                                if (!outEdge.getRelation().isAncestor(EnglishGrammaticalRelations.ADV_CLAUSE_MODIFIER)) {
                                    wordStack.push(outEdge.getTarget());
                                }
                            }
                        }

                        // Create question
                        String question = "";
                        for (Integer index : extract.keySet()) {
                            if (index.compareTo(markerWord.index()) == 0) {
                                question += replacement();
                            }
                            else {
                                question += extract.get(index).value() + " ";
                            }
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
            catch (Exception e) { e.printStackTrace(); }
        }
        return questions;
    }
}
