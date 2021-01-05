package com.paic.bst.feature;


import java.io.File;
import java.nio.file.Path;

/**
 * description: Example
 * date: 2020/11/23 3:09 下午
 * author: gallup
 * version: 1.0
 */
public class Example {
    public static void main(String []args){
        String path = BertModel.class.getResource("/distilbert_squad/").getPath();
        File file = new File(BertModel.class.getClass().getResource("/bert/exported").getPath());
        Path p1 = file.toPath();
        Bert model = Bert.load(p1);
        float[][] tokens = model.embedTokens("打土豪分田地");
        System.out.print(tokens);
        float[][] embedings = model.embedSequences("好好学习，天天向上");
        System.out.print(embedings);
    }

}
