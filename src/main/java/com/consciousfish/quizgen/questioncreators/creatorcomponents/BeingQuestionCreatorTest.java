package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.CoreNLPParser;
import com.consciousfish.quizgen.interfaces.Question;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class BeingQuestionCreatorTest {
    BeingQuestionCreator testUnit = new BeingQuestionCreator();
    List<CoreMap> sentences;
    Map<Integer, CorefChain> coreferences;

    @Before
    public void setUp() throws Exception {
        CoreNLPParser parser = new CoreNLPParser();
        parser.process("Bob is a dog. Bob is hungry.");
        sentences = parser.getSentences();
        coreferences = parser.getCoreferences();
    }

    @Test
    public void testCreateQuestion() throws Exception {
        List<Question> questions = testUnit.createQuestion(sentences, coreferences);
        for(Question q : questions) {
            System.out.println(q.getQuestion());
        }
    }
}