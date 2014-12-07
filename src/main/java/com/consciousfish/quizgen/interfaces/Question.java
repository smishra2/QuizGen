package com.consciousfish.quizgen.interfaces;

/**
 * Created by Jonathan on 2014/10/30.
 */
//public interface Question {
public abstract class Question {
    public abstract String getQuestion();
    public abstract String getAnswer();
    public abstract String getSentence();

    @Override
    public boolean equals(Object object) {
        if (object instanceof Question)
            return getQuestion().equals(((Question) (object)).getQuestion());
        else
            return false;
    }

    @Override
    public int hashCode() {
        return getQuestion().hashCode();
    }
}
