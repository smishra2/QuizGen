package com.consciousfish.quizgen.interfaces;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Jonathan on 2014/10/30.
 */
public interface QuestionCreator {
    public Set<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences);
}
