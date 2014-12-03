package com.consciousfish.quizgen.questioncreators.creatorcomponents;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Jonathan on 2014/12/02.
 */
public class PronounReplacer {
    private final static boolean test = true;
    public static List<IndexedWord> replacePronoun(IndexedWord pronoun, List<CoreMap> sentences, Map<Integer, CorefChain> coreferences) {
        if (test) System.out.println("replacePronoun called");
        boolean found = false;
        CorefChain.CorefMention coref = null;
        ArrayList<IndexedWord> result = new ArrayList<IndexedWord>();

        for (CorefChain chain : coreferences.values()) {
            for (CorefChain.CorefMention mention : chain.getMentionsInTextualOrder()) {
                if (test) System.out.println(mention.toString());
                if (mention.sentNum == pronoun.sentIndex() && mention.startIndex == pronoun.index()) {
                    coref = chain.getRepresentativeMention();
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        if (coref != null) {
            for (CoreMap s : sentences) {
                int sentPos = s.get(CoreAnnotations.SentenceIndexAnnotation.class);
                if (test) System.out.println("sentPos: " + sentPos + " = coref.sentNum: " + coref.sentNum);
                if (sentPos + 1 == coref.sentNum) {
                    SemanticGraph sentTree = s.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
                    for (int i = 1; i < coref.endIndex; i++) {
                        if (i >= coref.startIndex) {
                            result.add(sentTree.getNodeByIndex(i));
                        }
                    }
                }
            }
        }
        else {
            result.add(pronoun);
        }
        return result;
    }
}
