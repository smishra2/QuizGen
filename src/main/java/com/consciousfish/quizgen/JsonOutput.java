package com.consciousfish.quizgen;

import com.consciousfish.quizgen.interfaces.Output;
import com.consciousfish.quizgen.interfaces.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


import java.util.Collection;
import java.util.List;

/**
 * Created by Jonathan on 2014/11/05.
 */
public class JsonOutput {
    private Output output;

    public JsonOutput(Output output) {
        this.output = output;
    }

    public void sendOutput(Collection<Question> questions) {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        for(Question q : questions) {
            JsonObject obj = new JsonObject();
            obj.addProperty("question", q.getQuestion());
            obj.addProperty("sentence", q.getSentence());
            array.add(obj);
        }
        json.add("questions", array);

        /*JsonObject json = new JsonObject();
        JsonArray questionsArray = new JsonArray();
        for (Question q : questions) {
            JsonPrimitive prim = new JsonPrimitive(q.getQuestion());
            questionsArray.add(prim);
        }
        json.add("question", questionsArray);*/
        output.output(json.toString());
    }
}
