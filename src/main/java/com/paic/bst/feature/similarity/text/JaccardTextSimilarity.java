package com.paic.bst.feature.similarity.text;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import com.paic.bst.feature.utils.tokenizer.Word;
/**
 * description: JaccardTextSimilarity
 * 文本相似度计算
 * 判定方式：Jaccard相似性系数（Jaccard similarity coefficient） ，通过计算两个集合交集的大小除以并集的大小来评估他们的相似度
 * 算法步骤描述：
 * 1、分词
 * 2、求交集（去重），计算交集的不重复词的个数 intersectionSize
 * 3、求并集（去重），计算并集的不重复词的个数 unionSize
 * 4、2中的值除以3中的值 intersectionSize/(double)unionSize
 * 完整计算公式：
 * double score = intersectionSize/(double)unionSize;
 * date: 2020/12/31 2:33 下午
 * author: gallup
 * version: 1.0
 */
public class JaccardTextSimilarity extends TextSimilarity{
    /**
     * 判定相似度的方式：Jaccard相似性系数
     * @param words1 词列表1
     * @param words2 词列表2
     * @return 相似度分值
     */
    @Override
    protected double getSimilarityImpl(List<Word> words1, List<Word> words2) {
        if (words1.isEmpty() && words2.isEmpty()) {
            return 1.0;
        }
        //HashSet的contains性能要大于ArrayList的contains
        Set<Word> words2Set = new HashSet<>();
        words2Set.addAll(words2);
        //求交集
        Set<String> intersectionSet = new ConcurrentSkipListSet<>();
        words1.parallelStream().forEach(word -> {
            if (words2Set.contains(word)) {
                intersectionSet.add(word.getName());
            }
        });
        //交集的大小
        int intersectionSize = intersectionSet.size();
        //求并集
        Set<String> unionSet = new HashSet<>();
        words1.forEach(word -> unionSet.add(word.getName()));
        words2.forEach(word -> unionSet.add(word.getName()));
        //并集的大小
        int unionSize = unionSet.size();
        //相似度分值
        double score = intersectionSize / (double) unionSize;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("交集的大小：" + intersectionSize);
            LOGGER.debug("并集的大小：" + unionSize);
            LOGGER.debug("相似度分值=" + intersectionSize + "/(double)" + unionSize + "=" + score);
        }
        return score;
    }

    public static void main(String[] args) {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity textSimilarity = new JaccardTextSimilarity();
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
