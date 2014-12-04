package com.consciousfish.quizgen.questioncreators.creatorcomponents;

/**
 * Created by Jonathan on 2014/12/03.
 */
public class BecauseQuestionCreator extends AdvModifierQuestionCreator {

    @Override
    protected String marker() {
        return "because";
    }

    @Override
    protected String replacement() {
        return "because of what ";
    }
}
