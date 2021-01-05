package com.paic.bst.feature.similarity.text;


import com.paic.bst.feature.utils.tokenizer.Word;

import java.util.List;

/**
 * description: LongestCommonSubsequenceSimilarity
 * date: 2020/12/31 4:14 下午
 * author: gallup
 * version: 1.0
 */
public class LongestCommonSubsequenceSimilarity extends TextSimilarity{


    public double getSimilarityImpl(List<Word> words1, List<Word> words2){
        //文本1
        StringBuilder text1 = new StringBuilder();
        words1.forEach(word -> text1.append(word.getName()));
        //文本2
        StringBuilder text2 = new StringBuilder();
        words2.forEach(word -> text2.append(word.getName()));

        return text1.length() + text1.length() - 2 * length(text1.toString(), text2.toString());
    }


    /**
     * Return the length of Longest Common Subsequence (LCS) between strings s1
     * and s2.
     *
     * @param s1 The first string to compare.
     * @param s2 The second string to compare.
     * @return the length of LCS(s1, s2)
     * @throws NullPointerException if s1 or s2 is null.
     */
    public final int length(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }


        int s1_length = s1.length();
        int s2_length = s2.length();
        char[] x = s1.toCharArray();
        char[] y = s2.toCharArray();

        int[][] c = new int[s1_length + 1][s2_length + 1];

        for (int i = 1; i <= s1_length; i++) {
            for (int j = 1; j <= s2_length; j++) {
                if (x[i - 1] == y[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1;

                } else {
                    c[i][j] = Math.max(c[i][j - 1], c[i - 1][j]);
                }
            }
        }

        return c[s1_length][s2_length];
    }

    public static void main(String[] args) {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity textSimilarity = new EuclideanDistanceTextSimilarity();
        double score1pk1 = textSimilarity.getSimilarity(text1, text1);
        double score1pk2 = textSimilarity.getSimilarity(text1, text2);
        double score1pk3 = textSimilarity.getSimilarity(text1, text3);
        double score2pk2 = textSimilarity.getSimilarity(text2, text2);
        double score2pk3 = textSimilarity.getSimilarity(text2, text3);
        double score3pk3 = textSimilarity.getSimilarity(text3, text3);
        System.out.println(text1 + " 和 " + text1 + " 的相似度分值：" + score1pk1);
        System.out.println(text1 + " 和 " + text2 + " 的相似度分值：" + score1pk2);
        System.out.println(text1 + " 和 " + text3 + " 的相似度分值：" + score1pk3);
        System.out.println(text2 + " 和 " + text2 + " 的相似度分值：" + score2pk2);
        System.out.println(text2 + " 和 " + text3 + " 的相似度分值：" + score2pk3);
        System.out.println(text3 + " 和 " + text3 + " 的相似度分值：" + score3pk3);
    }

}
