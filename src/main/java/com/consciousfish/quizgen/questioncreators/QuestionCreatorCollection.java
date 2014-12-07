package com.consciousfish.quizgen.questioncreators;

import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Jonathan on 2014/10/30.
 */
public class QuestionCreatorCollection implements QuestionCreator {
    ArrayList<QuestionCreator> listeners;
    public QuestionCreatorCollection(QuestionCreator... creatorListeners) {
        listeners = new ArrayList<QuestionCreator>(Arrays.asList(creatorListeners));
    }

    public Set<Question> createQuestion(List<CoreMap> sentence, Map<Integer, CorefChain> coreferences) {
        Set<Question> questions = new HashSet<Question>();
        for(QuestionCreator creator : listeners) {
            questions.addAll(creator.createQuestion(sentence, coreferences));
        }
        return questions;
    }
}
