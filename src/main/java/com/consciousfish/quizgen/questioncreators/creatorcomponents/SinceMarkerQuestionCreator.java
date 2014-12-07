package com.consciousfish.quizgen.questioncreators.creatorcomponents;

/**
 * Created by Jonathan on 2014/12/03.
 */
public class SinceMarkerQuestionCreator extends AdvModifierQuestionCreator {
    @Override
    protected String marker() {
        return "since";
    }

    @Override
    protected String replacement() {
        return "because of what ";
    }
}
