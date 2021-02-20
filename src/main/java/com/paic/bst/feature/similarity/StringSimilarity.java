package com.paic.bst.feature.similarity;

import com.github.vickumar1981.stringdistance.util.StringDistance;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * description: StringSimilarity
 * date: 2021/2/20 1:44 下午
 * author: gallup
 * version: 1.0
 */
public class StringSimilarity {

    public List<Double> getSimilarity(String query, String item, List<String> features) {
        List<Double> results = new ArrayList<>();
        if(StringUtils.isBlank(query) || StringUtils.isBlank(item)){
            for(int i =0;i<features.size();i++){
                results.add(0.0);
            }
        }else{
            for (String feature : features) {
                switch (feature) {
                    case "cosine":
                        Double cosSimilarity = StringDistance.cosine(query, item);
                        results.add(cosSimilarity);
                        break;
                    case "damerau":
                        Double damerau = StringDistance.damerau(query, item);
                        results.add(damerau);
                        break;
                    case "diceCoefficient":
                        Double diceCoefficient = StringDistance.diceCoefficient(query, item);
                        results.add(diceCoefficient);
                        break;
                    case "hamming":
                        Double hamming = StringDistance.hamming(query, item);
                        results.add(hamming);
                        break;
                    case "jaccard":
                        Double jaccard = StringDistance.jaccard(query, item);
                        results.add(jaccard);
                        break;
                    case "jaro":
                        Double jaro = StringDistance.jaro(query, item);
                        results.add(jaro);
                        break;
                    case "jaroWinkler":
                        Double jaroWinkler = StringDistance.jaroWinkler(query, item);
                        results.add(jaroWinkler);
                        break;
                    case "levenshtein":
                        Double levenshtein = StringDistance.levenshtein(query, item);
                        results.add(levenshtein);
                        break;
                    case "needlemanWunsch":
                        Double needlemanWunsch = StringDistance.needlemanWunsch(query, item);
                        results.add(needlemanWunsch);
                        break;
                    case "nGram":
                        Double ngramSimilarity = StringDistance.nGram(query, item);
                        results.add(ngramSimilarity);
                        break;
                    case "bigram":
                        Double bigramSimilarity = StringDistance.nGram(query, item, 2);
                        results.add(bigramSimilarity);
                        break;
                    case "overlap":
                        Double overlap = StringDistance.overlap(query, item);
                        results.add(overlap);
                        break;
                    case "overlapBiGram":
                        Double overlapBiGram = StringDistance.overlap(query, item, 2);
                        results.add(overlapBiGram);
                        break;
                    case "smithWaterman":
                        Double smithWaterman = StringDistance.smithWaterman(query, item);
                        results.add(smithWaterman);
                        break;
                    case "smithWatermanGotoh":
                        Double smithWatermanGotoh = StringDistance.smithWatermanGotoh(query, item);
                        results.add(smithWatermanGotoh);
                        break;
                    case "tversky":
                        Double tversky = StringDistance.tversky(query, item, 0.5);
                        results.add(tversky);
                        break;
                    default:
                        break;

                }
            }
        }


        return results;
    }

    public List<Integer> getDistance(String query, String item, List<String> features) {
        // Distances between two strings
        List<Integer> results = new ArrayList<>();
        if(StringUtils.isBlank(query) || StringUtils.isBlank(item)){
            for(int i =0;i<features.size();i++){
                results.add(0);
            }

        }else{
            for (String feature : features) {
                switch (feature) {
                    case "damerauDist":
                        Integer damerauDist = StringDistance.damerauDist(query, item);
                        results.add(damerauDist);
                        break;
                    case "hammingDist":
                        Integer hammingDist = StringDistance.hammingDist(query, item);
                        results.add(hammingDist);
                        break;
                    case "levenshteinDist":
                        Integer levenshteinDist = StringDistance.levenshteinDist(query, item);
                        results.add(levenshteinDist);
                        break;
                    case "longestCommonSeq":
                        Integer longestCommonSeq = StringDistance.longestCommonSeq(query, item);
                        results.add(longestCommonSeq);
                        break;
                    case "ngramDist":
                        Integer ngramDist = StringDistance.nGramDist(query, item);
                        results.add(ngramDist);
                        break;
                    case "bigramDist":
                        Integer bigramDist = StringDistance.nGramDist(query, item, 2);
                        results.add(bigramDist);
                        break;
                    default:
                        break;
                }

            }
        }

        return results;
    }

    public static void main(String[] args) {
        StringSimilarity ss = new StringSimilarity();
        List<String> featureSimi = new ArrayList<>();
        List<String> featureDistance = new ArrayList<>();
        featureDistance.add("longestCommonSeq");
        featureSimi.add("cosine");
        featureSimi.add("hamming");
        featureSimi.add("jaro");
        featureSimi.add("jaroWinkler");
        featureSimi.add("damerau");
//        List<Double> result = ss.getSimilarity("hello", "chello", featureSimi);
//        List<Integer> res = ss.getDistance("martha", "marhta", featureDistance);
        List<Double> result = ss.getSimilarity("hello", "", featureSimi);
        List<Integer> res = ss.getDistance("martha", "", featureDistance);
        System.out.println(result);
        System.out.println(res);

    }
}
