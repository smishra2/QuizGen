package com.consciousfish.quizgen;

import com.consciousfish.quizgen.interfaces.Output;
import com.consciousfish.quizgen.interfaces.Question;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.List;

/**
 * Created by Jonathan on 2014/11/05.
 */
public class JsonOutput {
    private Output output;

    public JsonOutput(Output output) {
        this.output = output;
    }

    public void sendOutput(List<Question> questions) {
        JsonArray json = new JsonArray();
        for(Question q : questions) {
            JsonObject obj = new JsonObject();
            obj.addProperty("question", q.getQuestion());
            json.add(obj);
        }
        output.output(json.toString());
    }
}
