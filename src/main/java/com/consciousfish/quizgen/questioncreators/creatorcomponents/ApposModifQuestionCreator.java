package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import com.consciousfish.quizgen.interfaces.Question;
import com.consciousfish.quizgen.interfaces.QuestionCreator;
import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Sachit on 12/2/2014.
 */
public class ApposModifQuestionCreator  implements QuestionCreator {
    private static final boolean test = true;

    public List<Question> createQuestion(List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if(test) System.out.println("createQuestion called");
        List<Question> questions = new ArrayList<Question>();
        for(CoreMap sentence : sentences) {
            if(test) System.out.println("Parsing sentence: " + sentence.toString());
            Tree parseTree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);

            for (SemanticGraphEdge startEdge : dependencies.findAllRelns(EnglishGrammaticalRelations.APPOSITIONAL_MODIFIER)) {
                if (test) System.out.println("startEdge found: " + startEdge.toString());

                String question = "";

                // First check if source is NE, if so, use Who
                if (startEdge.getSource().ner().equalsIgnoreCase("PERSON"))
                   question = "Who is ";
                else
                    question = "What is ";

                int openIndex = startEdge.getTarget().beginPosition();
                while (sentence.toString().charAt(openIndex) != ',' &&
                        sentence.toString().charAt(openIndex) != '(')
                    openIndex--;

                int closeIndex = startEdge.getTarget().endPosition();
                while (sentence.toString().charAt(closeIndex) != ',' &&
                        sentence.toString().charAt(closeIndex) != ')')
                    closeIndex++;

                question += sentence.toString().substring(openIndex + 1, closeIndex).trim();
                question += "?";

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
        return questions;
    }
}

