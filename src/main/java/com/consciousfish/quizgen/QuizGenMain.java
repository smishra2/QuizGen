package com.consciousfish.quizgen;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Sachit on 9/28/2014.
 * This class acts as the entry point for QuizGen. It takes the entire body of
 * text as the argument and parses it. It then passes the Annotation to
 * respective question creators, which all create different questions
 * dependent on their purpose. It then pulls the questions from each creator
 * and outputs them to console.
 * NOTE: This design is highly tentative at the moment.
 */
public class QuizGenMain {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading questions...");
        System.out.println();

        String input = FileIO.read();

        /*
         * First, we initalize the parser and annotate the document.
         */
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(input);

        // run all Annotators on this text
        pipeline.annotate(document);

        /*
         * Next, we run through the question creators and asssemble the list
         * of questions.
         */

        List<String> questions = new ArrayList<String>();

        //questions.add(NamedEntityQuestionCreator.create(document));

        System.out.println("Here are your questions: ");
        String[] questionsArray = new String[questions.size()];
        int i = 0;
        for (String question : questions) {
            System.out.println(question);
            questionsArray[i] = question;
            i++;
        }

        FileIO.write(questionsArray);
    }
}
