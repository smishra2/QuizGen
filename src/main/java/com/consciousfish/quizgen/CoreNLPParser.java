package com.consciousfish.quizgen;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

/**
 * Created by Jonathan on 2014/11/05.
 */
public class CoreNLPParser {
    private List<CoreMap> sentences;
    private Map<Integer, CorefChain> coreferences;
    private String text;

    StanfordCoreNLP pipeline;

    public CoreNLPParser() {
        sentences = new ArrayList<CoreMap>();
        coreferences = new HashMap<Integer, CorefChain>();
        text = "";

        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
    }

    public void process(String text) {
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        coreferences = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
    }

    public List<CoreMap> getSentences() {
        return sentences;
    }

    public Map<Integer, CorefChain> getCoreferences() {
        return coreferences;
    }

    public String getText() {
        return text;
    }
}