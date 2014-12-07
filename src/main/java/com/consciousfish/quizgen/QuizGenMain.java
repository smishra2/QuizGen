package com.consciousfish.quizgen;

import com.consciousfish.quizgen.interfaces.Output;
import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.questioncreators.QuestionCreatorCollection;
import com.consciousfish.quizgen.questioncreators.creatorcomponents.*;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.InputStream;
import java.util.*;

/**
 * Created by Sachit on 9/28/2014.
 * This class acts as the entry point for QuizGen. It takes the entire body of
 * text as the argument and parses it. It then passes the Annotation to
 * respective question creators, which all create different questions
 * dependent on their purpose. It then pulls the questions from each creator
 * and outputs them to console.
 */
public class QuizGenMain {
    public static void main(String[] args) {
        System.out.println("Loading questions...");
        System.out.println();

        String input = FileIO.read();

        CoreNLPParser parser = new CoreNLPParser();

        QuestionCreatorCollection questionCreator = new QuestionCreatorCollection(new AfterQuestionCreator(),
                new ApposModifQuestionCreator(), new BecauseQuestionCreator(), new BeingQuestionCreator(),
                new ModalQuestionCreator(), new NamedEntityQuestionCreator(), new OnPrepQuestionCreator(),
                new SinceMarkerQuestionCreator());

        parser.process(input);
        Set<Question> questions = questionCreator.createQuestion(parser.getSentences(), parser.getCoreferences());

        JsonOutput output = new JsonOutput(new Output() {
            public void output(String out) {
                FileIO.write(out);
            }
        });
        output.sendOutput(questions);
    }
}
