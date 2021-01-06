package com.paic.bst;


import com.paic.bst.feature.similarity.text.*;
import com.paic.bst.util.analyzer.HanlpAnalyzerUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.List;

/**
 * description: PreProceessor
 * date: 2021/1/5 10:20 下午
 * author: gallup
 * version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PreProceessor {
    @Autowired
    HanlpAnalyzerUtils hanlpAnalyzerUtils;

    @Autowired
    CosineSimilarity cosineSimilarity;

    @Autowired
    DiceTextSimilarity diceTextSimilarity;

    @Autowired
    EditDistanceSimilarity editDistanceSimilarity;

    @Autowired
    EuclideanDistanceTextSimilarity euclideanSimilarity;

    @Autowired
    JaccardTextSimilarity jaccardTextSimilarity;

    @Autowired
    JaroDistanceTextSimilarity jaroDistanceTextSimilarity;

    @Autowired
    LongestCommonSubsequenceSimilarity lcsSimilarity;

    @Autowired
    ManhattanDistanceTextSimilarity mhtSimilarity;

    @Autowired
    SimHashPlusHammingDistanceTextSimilarity SimHashSimilarity;

    @Autowired
    W2vSimilarity w2vSimilarity;

    @Test
    public void testSave() {

        hanlpAnalyzerUtils.esSegment("深圳保障房");

        String keywords = "深圳保障房";

        List<String> docs = new ArrayList<String>();
        docs.add("深圳保障房计划给出最新公租房、安居房消息！没房的赶紧来看！");
        docs.add("在深圳，有多少人每个月最大的一笔支出就是房租。所以大家都挺关心公租房消息的，毕竟公租房能让房租这笔支出少一些又或者是期待安居房能让自己有点买房的机会。");
        docs.add("深圳的保障房工作近几年进展就很不错。从最初的廉租房、公租房、经济适用住房，发展到今天的公租房、安居房和人才住房。");
        docs.add("保障群体从最初的户籍低收入家庭，扩展到现在的户籍中低收入家庭、人才家庭，以及为城市提供基本公共服务的公交司机、环卫工人和先进制造业职工等群体");
        docs.add("好消息，新版租房合同来袭，在深圳租房的你有福了！");
        for (String doc : docs) {
//            TextSimilarity cosineSimilarity = new CosineSimilarity();
            double scoreCosine = cosineSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreCosine);

//            TextSimilarity diceSimilarity = new DiceTextSimilarity();
            double scoreDice = diceTextSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreDice);
//
//            TextSimilarity editDistanceSimilarity = new EditDistanceSimilarity();
            double scoreeditDistance = editDistanceSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreeditDistance);
//
//            TextSimilarity euclideanSimilarity = new EuclideanDistanceTextSimilarity();
            double scoreEuclid = euclideanSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreEuclid);
//
//            TextSimilarity jaccardTextSimilarity = new JaccardTextSimilarity();
            double scorejaccard = jaccardTextSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scorejaccard);
//
//            TextSimilarity jaroDistanceTextSimilarity = new JaroDistanceTextSimilarity();
            double scoreJaroDistance = jaroDistanceTextSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreJaroDistance);

//            TextSimilarity jaroWinklerDistanceSimilarity = new JaroWinklerDistanceTextSimilarity();
//            double scoreJaroWinkler = jaroWinklerDistanceSimilarity.getSimilarity(keywords, doc);
//            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreJaroWinkler);

//            TextSimilarity lcsSimilarity = new LongestCommonSubsequenceSimilarity();
            double scoreLcs = lcsSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreLcs);
//
//            TextSimilarity mhtSimilarity = new ManhattanDistanceTextSimilarity();
            double scoreMht = mhtSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreMht);
//
//            TextSimilarity SimHashSimilarity = new SimHashPlusHammingDistanceTextSimilarity();
            double scoreSimHash = SimHashSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreSimHash);
//
//            TextSimilarity w2vSimilarity = new W2vSimilarity();
            double scoreW2v = w2vSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreW2v);
        }
    }
}
