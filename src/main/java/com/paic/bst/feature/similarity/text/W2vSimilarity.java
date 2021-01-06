package com.paic.bst.feature.similarity.text;


import com.paic.bst.feature.utils.tokenizer.Word;
import com.paic.bst.feature.embedding.w2v.Word2Vec;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: W2vSimilarity
 * date: 2020/12/31 5:46 下午
 * author: gallup
 * version: 1.0
 */
@Component
public class W2vSimilarity  extends TextSimilarity{
    @Override
    public double getSimilarityImpl(List<Word> words1, List<Word> words2){
        Word2Vec w2v = Word2Vec.getInstance(true,50);
        float[] vector1 = w2v.getAvgVector(words1);
        float[] vector2 = w2v.getAvgVector(words2);
        return w2v.cosinSimilarity(vector1,vector2);
    }
}
