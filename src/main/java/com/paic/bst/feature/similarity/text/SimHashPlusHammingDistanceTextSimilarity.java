package com.paic.bst.feature.similarity.text;


import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.util.List;
import com.paic.bst.feature.utils.tokenizer.Word;
/**
 * description: SimHashPlusHammingDistanceTextSimilarity
 * date: 2020/12/31 2:41 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
public class SimHashPlusHammingDistanceTextSimilarity extends TextSimilarity{

    private int hashBitCount = 128;

    public SimHashPlusHammingDistanceTextSimilarity() {
    }

    public SimHashPlusHammingDistanceTextSimilarity(int hashBitCount) {
        this.hashBitCount = hashBitCount;
    }

    public int getHashBitCount() {
        return hashBitCount;
    }

    public void setHashBitCount(int hashBitCount) {
        this.hashBitCount = hashBitCount;
    }

    /**
     * 计算相似度分值
     *
     * @param words1 词列表1
     * @param words2 词列表2
     * @return 相似度分值
     */
    @Override
    protected double getSimilarityImpl(List<Word> words1, List<Word> words2) {
        //用词频来标注词的权重
        taggingWeightByFrequency(words1, words2);
        //计算SimHash
        String simHash1 = simHash(words1);
        String simHash2 = simHash(words2);
        //计算SimHash值之间的汉明距离
        int hammingDistance = hammingDistance(simHash1, simHash2);
        if (hammingDistance == -1) {
            log.error("文本1：" + words1.toString());
            log.error("文本2：" + words2.toString());
            log.error("文本1SimHash值：" + simHash1);
            log.error("文本2SimHash值：" + simHash2);
            log.error("文本1和文本2的SimHash值长度不相等，不能计算汉明距离");
            return 0.0;
        }
        int maxDistance = simHash1.length();
        double score = (1 - hammingDistance / (double) maxDistance);
        if (log.isDebugEnabled()) {
            log.debug("文本1：" + words1.toString());
            log.debug("文本2：" + words2.toString());
            log.debug("文本1SimHash值：" + simHash1);
            log.debug("文本2SimHash值：" + simHash2);
            log.debug("hashBitCount：" + hashBitCount);
            log.debug("SimHash值之间的汉明距离：" + hammingDistance);
            log.debug("文本1和文本2的相似度分值：1 - " + hammingDistance + " / (double)" + maxDistance + "=" + score);
        }
        return score;
    }

    /**
     * 计算词列表的SimHash值
     *
     * @param words 词列表
     * @return SimHash值
     */
    private String simHash(List<Word> words) {
        float[] hashBit = new float[hashBitCount];
        words.forEach(word -> {
            float weight = word.getWeight() == null ? 1 : word.getWeight();
            BigInteger hash = hash(word.getName());
            for (int i = 0; i < hashBitCount; i++) {
                BigInteger bitMask = new BigInteger("1").shiftLeft(i);
                if (hash.and(bitMask).signum() != 0) {
                    hashBit[i] += weight;
                } else {
                    hashBit[i] -= weight;
                }
            }
        });
        StringBuffer fingerprint = new StringBuffer();
        for (int i = 0; i < hashBitCount; i++) {
            if (hashBit[i] >= 0) {
                fingerprint.append("1");
            } else {
                fingerprint.append("0");
            }
        }
        return fingerprint.toString();
    }

    /**
     * 计算词的哈希值
     *
     * @param word 词
     * @return 哈希值
     */
    private BigInteger hash(String word) {
        if (word == null || word.length() == 0) {
            return new BigInteger("0");
        }
        char[] charArray = word.toCharArray();
        BigInteger x = BigInteger.valueOf(((long) charArray[0]) << 7);
        BigInteger m = new BigInteger("1000003");
        BigInteger mask = new BigInteger("2").pow(hashBitCount).subtract(new BigInteger("1"));
        long sum = 0;
        for (char c : charArray) {
            sum += c;
        }
        x = x.multiply(m).xor(BigInteger.valueOf(sum)).and(mask);
        x = x.xor(new BigInteger(String.valueOf(word.length())));
        if (x.equals(new BigInteger("-1"))) {
            x = new BigInteger("-2");
        }
        return x;
    }

    /**
     * 计算等长的SimHash值的汉明距离
     * 如不能比较距离（比较的两段文本长度不相等），则返回-1
     *
     * @param simHash1 SimHash值1
     * @param simHash2 SimHash值2
     * @return 汉明距离
     */
    private int hammingDistance(String simHash1, String simHash2) {
        if (simHash1.length() != simHash2.length()) {
            return -1;
        }
        int distance = 0;
        int len = simHash1.length();
        for (int i = 0; i < len; i++) {
            if (simHash1.charAt(i) != simHash2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    public static void main(String[] args) throws Exception {
        String text1 = "我爱购物";
        String text2 = "我爱读书";
        String text3 = "他是黑客";
        TextSimilarity textSimilarity = new SimHashPlusHammingDistanceTextSimilarity();
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
