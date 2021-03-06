package com.paic.bst.feature.similarity.text;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.paic.bst.feature.utils.AtomicFloat;
import com.paic.bst.feature.utils.tokenizer.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * description: ManhattanDistanceTextSimilarity
 * date: 2020/12/31 2:40 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
@Component
public class ManhattanDistanceTextSimilarity extends TextSimilarity{
    /**
     * 判定相似度的方式：曼哈顿距离
     * 曼哈顿距离原理：
     * 设A(x1, y1)，B(x2, y2)是平面上任意两点
     * 两点间的距离dist(A,B)=|x1-x2|+|y1-y2|
     * @param words1 词列表1
     * @param words2 词列表2
     * @return 相似度分值
     */
    @Override
    protected double getSimilarityImpl(List<Word> words1, List<Word> words2) {
        //用词频来标注词的权重
        taggingWeightByFrequency(words1, words2);
        //构造权重快速搜索容器
        Map<String, Float> weights1 = getFastSearchMap(words1);
        Map<String, Float> weights2 = getFastSearchMap(words2);
        //所有的不重复词
        Set<Word> words = new HashSet<>();
        words.addAll(words1);
        words.addAll(words2);
        //向量的维度为words的大小，每一个维度的权重是词频
        //manhattanDistance=|x1-x2|+|y1-y2|
        AtomicFloat manhattanDistance = new AtomicFloat();
        //计算
        words
                .parallelStream()
                .forEach(word -> {
                    Float x1 = weights1.get(word.getName());
                    Float x2 = weights2.get(word.getName());
                    if (x1 == null) {
                        x1 = 0f;
                    }
                    if (x2 == null) {
                        x2 = 0f;
                    }
                    //|x1-x2|
                    float oneOfTheDimension = Math.abs(x1 - x2);
                    //+
                    manhattanDistance.addAndGet(oneOfTheDimension);
                });
        double score = 0;
        if (manhattanDistance.get() == 0) {
            //距离为0，表示完全相同
            score = 1.0;
        } else {
            //使用BigDecimal保证精确计算浮点数
            //score = 1 / (double)(manhattanDistance.get()+1);
            score = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(manhattanDistance.get() + 1), 9, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        if (log.isDebugEnabled()) {
            log.debug("文本1和文本2的曼哈顿距离：" + manhattanDistance.get());
            log.debug("文本1和文本2的相似度分值：1 / (double)(" + manhattanDistance.get() + "+1)=" + score);
        }
        return score;
    }

    public static void main(String[] args) {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity textSimilarity = new ManhattanDistanceTextSimilarity();
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
