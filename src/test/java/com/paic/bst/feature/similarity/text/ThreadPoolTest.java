package com.paic.bst.feature.similarity.text;

import org.junit.Test;
import com.paic.bst.feature.similarity.StringSimilarity;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * description: ThreadPoolTest
 * date: 2021/2/25 10:05 上午
 * author: gallup
 * version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ThreadPoolTest {
    @Autowired
    StringSimilarity stringSimilarity;

    private static final int BATCH_SIZE = 20;

    private  Future<List> noWaitExec(final List<String> batch,final String keyword,List<String> features, ExecutorService pool) {
        return pool.submit(new Callable<List>() {
            public List call() throws Exception {
                List result = new ArrayList<>(batch.size());
                for (String string : batch) {
                    result.add(process(keyword,string,features));
                }
                return result;
            }

        });
    }

    private  List<Integer> process(String string,String id,List<String> features) {
        // Your process ....
        return stringSimilarity.getDistance("张仲景六味地黄丸360粒",id,features);
    }

    public void threaddemo(List<String> features){
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<String> batch = new ArrayList<>(BATCH_SIZE);
        List<String> titiles = new ArrayList<>();
        for (int i = 0; i < 3000; i++) {
            titiles.add("张仲景六味地黄丸360粒");
        }

        List<Future> results = new LinkedList<>();

        for (String id:titiles){
            batch.add(id);
            if (batch.size() >= BATCH_SIZE) {
//                System.out.println(0);
                Future<List> f = noWaitExec(batch,"张仲景六味地黄丸360粒",features, pool);
//                System.out.println(f);
                results.add(f);
                batch = new ArrayList<>(BATCH_SIZE);
            }

        }

        Future<List> f = noWaitExec(batch, "张仲景六味地黄丸360粒",features,pool);
        results.add(f);
        List<List<Integer>> total = new ArrayList<>();
//        for (Future future : results) {
//            try {
//                Object obj = future.get();
//                // Use your results here
//                if (obj instanceof ArrayList<?>) {
//                    for (Object o : (List<?>) obj) {
////                        total.add(ArrayList.class.cast(o));
//                        List<Integer> res = new ArrayList<>();
//                        if(o instanceof ArrayList<?>){
//                            for (Object oj : (List<?>) o){
//                                res.add(Integer.class.cast(oj));
//                            }
//                        }
//                        total.add(res);
////
//                    }
//                }
//
//            } catch (Exception e) {
//                // Manage this....
//            }
//        }
        System.out.println(total.size());

    }
    @Test
    public void test1() throws IOException {

        List<String> featureSimi = new ArrayList<>();
        List<String> featureDistance = new ArrayList<>();
        featureDistance.add("lcs");
        featureDistance.add("levenshtein_distance");
        featureDistance.add("hamming_distance");
        featureSimi.add("cosine");
        featureSimi.add("hamming");
        featureSimi.add("jaro_similarity");
        featureSimi.add("jaroWinkler");
        featureSimi.add("damerau");



        // 开始时间
        long start = System.currentTimeMillis();
        threaddemo(featureDistance);
        System.err.println("执行任务test1消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");


    }
    @Test
    public void test2() {

        long star1 = System.currentTimeMillis();
        List<List<Integer>> total = new ArrayList<>();
        List<String> featureSimi = new ArrayList<>();
        List<String> featureDistance = new ArrayList<>();
        featureDistance.add("lcs");
        featureDistance.add("levenshtein_distance");
        featureDistance.add("hamming_distance");
        featureSimi.add("cosine");
        featureSimi.add("hamming");
        featureSimi.add("jaro_similarity");
        featureSimi.add("jaroWinkler");
        featureSimi.add("damerau");
        List<String> res = new ArrayList<>();
        List<String> titiles = new ArrayList<>();
        for (int i = 0; i < 3000; i++) {
//            querys.add("六味地黄丸360粒");
            titiles.add("张仲景六味地黄丸360粒");
        }
        for (String id:titiles){
            res.add(id);
            List<Integer> result = stringSimilarity.getDistance("张仲景六味地黄丸360粒",id,featureDistance);
            total.add(result);
        }
        System.out.println(total.size());
        System.err.println("执行任务test2消耗了 ：" + (System.currentTimeMillis() - star1) + "毫秒");
    }



}
