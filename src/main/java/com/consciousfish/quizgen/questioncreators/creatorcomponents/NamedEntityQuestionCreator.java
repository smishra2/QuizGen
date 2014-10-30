package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sachit on 9/28/2014.
 */
public class NamedEntityQuestionCreator implements QuestionCreator {
    @Override
    public List<Question> createQuestion(CoreMap sentence, Map<Integer, CorefChain> coreferences) {
        // TODO: implement me lol
        Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
        SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        return new ArrayList<Question>();
    }
}
