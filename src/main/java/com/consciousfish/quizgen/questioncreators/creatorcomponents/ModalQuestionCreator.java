package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.FileIO;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotation;
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
public class ModalQuestionCreator implements QuestionCreator {
    private static final boolean test = false;

    public Set<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("createQuestion called");
        Set<Question> questions = new HashSet<Question>();
        for (CoreMap sentence : sentences) {
            try {
                if (test) System.out.println("Parsing sentence: " + sentence.toString());
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                for (SemanticGraphEdge startEdge : dependencies.getOutEdgesSorted(dependencies.getFirstRoot())) {
                    if (startEdge.getRelation().compareTo(EnglishGrammaticalRelations.AUX_MODIFIER) != 0) {
                        continue;
                    }
                    if (test) System.out.println("startEdge found: " + startEdge.toString());

                    // Create tree for sentence
                    TreeMap<Integer, IndexedWord> extract = new TreeMap<Integer, IndexedWord>();
                    IndexedWord aux = startEdge.getTarget();
                    List<IndexedWord> pronouns = new ArrayList<IndexedWord>();

                    for (IndexedWord word : dependencies.vertexSet()) {
                        if (test) System.out.println("Checking word: " + word.toString());
                        if (!word.equals(aux)) {
                            extract.put(word.index(), word);
                        }
                        if (word.tag().equalsIgnoreCase("prp")) {
                            pronouns.add(word);
                        }
                    }

                    Map<IndexedWord, List<IndexedWord>> nouns = new HashMap<IndexedWord, List<IndexedWord>>();
                    for (IndexedWord pronoun : pronouns) {
                        nouns.put(pronoun, PronounReplacer.replacePronoun(pronoun, sentences, coreferences));
                    }

                    // Create question
                    String question = aux.value().substring(0, 1).toUpperCase() + aux.value().substring(1) + " ";
                    for (Integer index : extract.keySet()) {
                        if (extract.firstKey() == index) {
                            String ner = extract.get(index).backingLabel().get(CoreAnnotations.NamedEntityTagAnnotation.class);
                            if (test) System.out.println("Checking word: " + extract.get(index).toString() + " ner: " + ner);
                            if (ner.equalsIgnoreCase("o") && nouns.containsKey(extract.get(index))) {
                                for (IndexedWord word : nouns.get(extract.get(index))) {
                                    if (word.equals(extract.get(index))) {
                                        question += word.value().toLowerCase() + " ";
                                        break;
                                    } else {
                                        question += word.value() + " ";
                                    }
                                }
                            }
                            else {
                                question += extract.get(index).value().toLowerCase() + " ";
                            }
                        } else {
                            question += extract.get(index).value() + " ";
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
            catch (Exception e) { FileIO.writeToLog(e); }
        }
        return questions;
    }
}
