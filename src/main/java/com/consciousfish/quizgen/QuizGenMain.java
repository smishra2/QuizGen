package com.consciousfish.quizgen;

import com.consciousfish.quizgen.interfaces.Output;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.questioncreators.QuestionCreatorCollection;
import com.consciousfish.quizgen.questioncreators.creatorcomponents.BeingQuestionCreator;
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
    public static void main(String[] args) {
        System.out.println("Loading questions...");
        System.out.println();

        //String input = FileIO.read();
        String input = "Bob is a dog. Bob is hungry.";

        CoreNLPParser parser = new CoreNLPParser();
        parser.process(input);

        QuestionCreatorCollection questionCreator = new QuestionCreatorCollection(new BeingQuestionCreator());
        List<Question> questions = questionCreator.createQuestion(parser.getSentences(), parser.getCoreferences());

        JsonOutput output = new JsonOutput(new Output() {
            @Override
            public void output(String out) {
                System.out.println(out);
                FileIO.write(out);
            }
        });
        output.sendOutput(questions);
    }
}
