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
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Sachit on 9/28/2014.
 */
public class NamedEntityQuestionCreator implements QuestionCreator {
    private static final boolean test = true;

    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if(test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for(CoreMap sentence : sentences) {
            try {
                if (test)
                    System.out.println("Parsing sentence: " + sentence.toString());
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                HashMap<Integer, Boolean> wordUsed = new HashMap<Integer, Boolean>();
                String question = null;
                for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                    if ("o".equalsIgnoreCase(ne)) // Not a named entity
                        continue;
                    if (wordUsed.containsKey(token.index())) // Word's been used
                        continue;

                    switch (ne) {
                        case "PERSON":
                            question = "Who is";
                            break;
                        case "LOCATION":
                            question = "Where is";
                            break;
                        default: // We're not dealing w/ other NER types
                            continue;
                    }

                    // Get the indexed word corresponding to this token
                    IndexedWord entity = dependencies.getNodeByIndex(token.index());

                    // Get the related (compounding noun) words, sort them,
                    // then add to question (use BFS sorta)
                    Set<IndexedWord> relatedWords = assembleWords(entity, dependencies);
                    TreeMap<Integer, String> mappedWords = new TreeMap<Integer, String>();
                    for (IndexedWord word : relatedWords)
                        mappedWords.put(word.index(), word.originalText());

                    for (Integer index : mappedWords.keySet()) {
                        question += " " + mappedWords.get(index);
                        wordUsed.put(index, true);
                    }
                    question += "?";

                    if (question != null) {
                        final String questionclone = question;

                        questions.add(new Question() {
                            final String q = questionclone;
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
            catch (Exception e) {
                e.printStackTrace();
                FileIO.writeToLog(e);
            }
        }
        return questions;
    }

    private Set<IndexedWord> assembleWords(IndexedWord root,
                                           SemanticGraph dependencies) {
        Set<IndexedWord> set = new HashSet<IndexedWord>();
        Queue<IndexedWord> queue = new LinkedList<IndexedWord>();
        queue.add(root);
        while (!queue.isEmpty()) {
            IndexedWord word = queue.remove();
            set.add(word);
            // Look through all edges for compound nouns
            for (SemanticGraphEdge edge : dependencies.incomingEdgeIterable(word)) {
                // Only add other words which add to compound noun
                if ("nn".equalsIgnoreCase(edge.getRelation().getShortName()) &&
                        !set.contains(edge.getSource()))
                    queue.add(edge.getSource());
            }
            for (SemanticGraphEdge edge : dependencies.outgoingEdgeIterable(word)) {
                // Only add other words which add to compound noun
                if ("nn".equalsIgnoreCase(edge.getRelation().getShortName()) &&
                        !set.contains(edge.getTarget()))
                    queue.add(edge.getTarget());
            }
        }
        return set;
    }
}
