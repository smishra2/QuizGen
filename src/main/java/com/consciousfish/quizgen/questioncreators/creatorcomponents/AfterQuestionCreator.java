package com.consciousfish.quizgen.questioncreators.creatorcomponents;

/**
 * Created by Jonathan on 2014/12/03.
 */
public class AfterQuestionCreator extends AdvModifierQuestionCreator {
    @Override
    protected String marker() {
        return "after";
    }

    @Override
    protected String replacement() {
        return "after what ";
    }
}
