package com.paic.bst.feature.similarity.text;

import com.paic.bst.feature.similarity.DataModel;
import com.paic.bst.feature.similarity.AsyncTask;
import com.paic.bst.feature.similarity.FutureTaskWorker;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * description: ThreadPoolTest
 * date: 2021/2/25 10:05 上午
 * author: gallup
 * version: 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class ThreadPoolTest {
    @Autowired
    StringSimilarity stringSimilarity;

    @Autowired
    private AsyncTask asyncTask;

    private static final int BATCH_SIZE = 20;

    private Future<List> noWaitExec(final List<String> batch, final String keyword, List<String> features, ExecutorService pool) {
        return pool.submit(new Callable<List>() {
            public List call() throws Exception {
                List<DataModel> result = new ArrayList<>(batch.size());
                for (String string : batch) {
                    result.add(process(keyword, string, features));
                }
                return result;
            }

        });
    }

    private DataModel process(String string, String id, List<String> features) {
        DataModel data = new DataModel();
        List<Integer> scores = stringSimilarity.getDistance("张仲景六味地黄丸360粒", id, features);
        data.setV1(scores.get(0).intValue());
        data.setV1(scores.get(1).intValue());
        data.setV1(scores.get(2).intValue());
        return data;
    }

    public void threaddemo(List<String> features) {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        List<String> batch = new ArrayList<>(BATCH_SIZE);
        List<String> titiles = new ArrayList<>();
        for (int i = 0; i < 3000; i++) {
            titiles.add("张仲景六味地黄丸360粒");
        }

        List<Future> results = new LinkedList<>();

        for (String id : titiles) {
            batch.add(id);
            if (batch.size() >= BATCH_SIZE) {
//                System.out.println(0);
                Future<List> f = noWaitExec(batch, "张仲景六味地黄丸360粒", features, pool);
//                System.out.println(f);
                results.add(f);
                batch = new ArrayList<>(BATCH_SIZE);
            }

        }

        Future<List> f = noWaitExec(batch, "张仲景六味地黄丸360粒", features, pool);
        results.add(f);
        List<DataModel> total = new ArrayList<>();
        for (Future future : results) {
            try {
                Object obj = future.get();
                // Use your results here
                if (obj instanceof ArrayList<?>) {
                    for (Object o : (List<?>) obj) {
                        total.add(DataModel.class.cast(o));
//
                    }
                }

            } catch (Exception e) {
                // Manage this....
            }
        }
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
        List<DataModel> total = new ArrayList<>();
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
        for (String id : titiles) {
            res.add(id);
            List<Integer> result = stringSimilarity.getDistance("张仲景六味地黄丸360粒", id, featureDistance);

            DataModel data = new DataModel();
            data.setV1(result.get(0));
            data.setV1(result.get(1));
            data.setV1(result.get(2));
            total.add(data);

        }
        System.out.println(total.size());
        System.err.println("执行任务test2串行消耗了 ：" + (System.currentTimeMillis() - star1) + "毫秒");
    }

    @Test
    public void getAllResult() {
        long begin = System.currentTimeMillis();
        List<DataModel> total = new ArrayList<>();
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
//        FutureTaskWorker<Long, String> futureTaskWorker = new FutureTaskWorker<>(titiles, (String e) -> getSortResult(e,featureDistance));
//
//        List<String> allResult = futureTaskWorker.getAllResult();
        List<CompletableFuture<DataModel>> completableFutures =
                titiles.stream().map(lang -> getSortResult(lang,featureDistance)).collect(Collectors.toList());
        List<DataModel> sortResult = completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
//        CompletableFuture<Void> allFutures = CompletableFuture
//                .allOf(completableFutures.toArray(new CompletableFuture[completableFutures.size()]));
//        CompletableFuture<List<DataModel>> allCompletableFuture = allFutures.thenApply(future -> {
//            return completableFutures.stream()
//                    .map(completableFuture -> completableFuture.join())
//                    .collect(Collectors.toList());
//        });
//        CompletableFuture completableFuture = allCompletableFuture.thenApply(greets -> {
//            return greets.stream()                             .map(GreetHolder::getGreet).collect(Collectors.toList());
//        });
        System.out.println(sortResult.size());
        long end = System.currentTimeMillis();

        System.out.println("getAllResult结束耗时:" + (end - begin));
    }


    @Test
    public void CompletableFutureTest()throws InterruptedException, ExecutionException {
        long begin = System.currentTimeMillis();
        List<DataModel> total = new ArrayList<>();
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

        List<DataModel> sortResult = batchGetSortResult(titiles,featureDistance);
        System.out.println(sortResult.size());
        long end =System.currentTimeMillis();
        System.out.println("CompletableFuture耗时--》"+ (end-begin)+"ms");
    }

    private CompletableFuture<DataModel> getSortResult(String id,List<String> featureDistance) {
        return CompletableFuture.supplyAsync(() -> {
            List<Integer> result = stringSimilarity.getDistance("张仲景六味地黄丸360粒", id, featureDistance);

            DataModel data = new DataModel();
            data.setV1(result.get(0));
            data.setV1(result.get(1));
            data.setV1(result.get(2));
            return data;
        });
    }

    private List<DataModel> batchGetSortResult(List<String> ids,List<String> featureDistance) throws InterruptedException, ExecutionException{
        List<CompletableFuture<DataModel>>  sortFutrues = ids.stream().map(id->asyncTask.getSortByCompletableFuture(id,featureDistance)).collect(Collectors.toList());
        return sortFutrues.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }


}
