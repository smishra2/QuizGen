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
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArraySet;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Sachit on 12/2/2014.
 */
public class ApposModifQuestionCreator implements QuestionCreator {
    private static final boolean test = false;

    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if(test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for(CoreMap sentence : sentences) {
            try {
                if (test)
                    System.out.println("Parsing sentence: " + sentence.toString());
                Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

                for (SemanticGraphEdge startEdge : dependencies.findAllRelns(EnglishGrammaticalRelations.APPOSITIONAL_MODIFIER)) {
                    if (test)
                        System.out.println("startEdge found: " + startEdge.toString());

                    String question = "";

                    // First check if source is NE, if so, use Who
                    if (startEdge.getTarget().ner().equalsIgnoreCase("PERSON"))
                        question = "Who is";
                    else
                        question = "What is";

                    // Assemble treemap of all words you'll need
                    Set<IndexedWord> words = assembleWords(startEdge.getTarget(),
                            dependencies);
                    TreeMap<Integer, String> mappedWords = new TreeMap<Integer, String>();
                    for (IndexedWord word : words) {
                        // Use pronoun replacer if necessary
                        if (word.tag().equalsIgnoreCase("prp") ||
                                word.tag().equalsIgnoreCase("prp$")) {
                            List<IndexedWord> newWords =
                                    PronounReplacer.replacePronoun(
                                    word, sentences, coreferences);
                            String wholeNewWord = "";
                            for (IndexedWord newWord : newWords)
                                wholeNewWord += " " + newWord.originalText();
                            wholeNewWord = wholeNewWord.trim();
                            mappedWords.put(word.index(), wholeNewWord);
                        }
                        else { // not a pronoun
                            mappedWords.put(word.index(), word.originalText());
                        }
                    }

                    for (Integer index : mappedWords.keySet())
                        question += " " + mappedWords.get(index);

                    question += "?";

                    final String questionclone = question;
                    final String sentenceClone = sentence.toString();

                    questions.add(new Question() {
                        final String q = questionclone;
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
        set.add(root);
        for (SemanticGraphEdge edge : dependencies.outgoingEdgeIterable(root)) {
            set.addAll(assembleWords(edge.getTarget(), dependencies));
        }
        return set;
    }
}

