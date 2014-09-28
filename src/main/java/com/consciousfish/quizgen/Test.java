package com.consciousfish.quizgen;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreLabel;

public class Test {

	public static void main(String[] args) {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        // read some text in the text variable
        String text = "A week after U.S.-led airstrikes in Syria began, lawmakers continued to question President Barack Obama's strategy for defeating the militant group ISIS, which he admitted in a televised interview Sunday was more powerful than the U.S. initially believed.\n" +
                "Echoing sentiments also expressed by James Clapper, the head of U.S. intelligence services, Obama said the government \"underestimated what had been taking place in Syria\" during its civil war, allowing Syria to become \"ground zero for jihadists around the world.\"\n" +
                "Speaking in a taped interview with CBS' \"60 Minutes,\" Obama said the terrorists were remnants of al Qaeda in Iraq, which after being diminished by U.S. forces \"went back underground.\""; // Add your text here!
        System.out.println(text);
        
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);
        
        // run all Annotators on this text
        pipeline.annotate(document);
        
        // these are all the sentences in this document
        // a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        
        for(CoreMap sentence: sentences) {
            System.out.print("Currently parsing: ");
            System.out.println(sentence.toString());

            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                System.out.print("Current word: ");

                // this is the text of the token
                String word = token.get(TextAnnotation.class);
                System.out.println(word);
                // this is the POS tag of the token
                String pos = token.get(PartOfSpeechAnnotation.class);
                System.out.println("POS: " + pos);
                // this is the NER label of the token
                String ne = token.get(NamedEntityTagAnnotation.class);
                System.out.println("NER: " + ne);
            }

        // this is the parse tree of the current sentence
        Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

        // this is the Stanford dependency graph of the current sentence
        SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
            document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
	}

}