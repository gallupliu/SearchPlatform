package com.paic.bst.feature.similarity;

import com.paic.bst.feature.similarity.text.*;
import com.paic.bst.util.analyzer.HanlpAnalyzerUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * description: Demo
 * date: 2020/12/11 10:40 上午
 * author: gallup
 * version: 1.0
 */
public class Demo {
    @Autowired
    HanlpAnalyzerUtils hanlpAnalyzerUtils;

    public static  void test(){

        String keywords = "深圳保障房";

        List<String> docs = new ArrayList<String>();
        docs.add("深圳保障房计划给出最新公租房、安居房消息！没房的赶紧来看！");
        docs.add("在深圳，有多少人每个月最大的一笔支出就是房租。所以大家都挺关心公租房消息的，毕竟公租房能让房租这笔支出少一些又或者是期待安居房能让自己有点买房的机会。");
        docs.add("深圳的保障房工作近几年进展就很不错。从最初的廉租房、公租房、经济适用住房，发展到今天的公租房、安居房和人才住房。");
        docs.add("保障群体从最初的户籍低收入家庭，扩展到现在的户籍中低收入家庭、人才家庭，以及为城市提供基本公共服务的公交司机、环卫工人和先进制造业职工等群体");
        docs.add("好消息，新版租房合同来袭，在深圳租房的你有福了！");
        for(String doc:docs){
            TextSimilarity cosineSimilarity = new CosineSimilarity();
            double scoreCosine = cosineSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreCosine);

            TextSimilarity diceSimilarity = new DiceTextSimilarity();
            double scoreDice = diceSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreDice);

            TextSimilarity editDistanceSimilarity = new EditDistanceSimilarity();
            double scoreeditDistance = editDistanceSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreeditDistance);

            TextSimilarity euclideanSimilarity = new EuclideanDistanceTextSimilarity();
            double scoreEuclid = euclideanSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreEuclid);

            TextSimilarity jaccardTextSimilarity = new JaccardTextSimilarity();
            double scorejaccard = jaccardTextSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scorejaccard);

            TextSimilarity jaroDistanceTextSimilarity = new JaroDistanceTextSimilarity();
            double scoreJaroDistance = jaroDistanceTextSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreJaroDistance);

//            TextSimilarity jaroWinklerDistanceSimilarity = new JaroWinklerDistanceTextSimilarity();
//            double scoreJaroWinkler = jaroWinklerDistanceSimilarity.getSimilarity(keywords, doc);
//            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreJaroWinkler);

            TextSimilarity lcsSimilarity = new LongestCommonSubsequenceSimilarity();
            double scoreLcs = lcsSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreLcs);

            TextSimilarity mhtSimilarity = new ManhattanDistanceTextSimilarity();
            double scoreMht = mhtSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreMht);

            TextSimilarity SimHashSimilarity = new SimHashPlusHammingDistanceTextSimilarity();
            double scoreSimHash = SimHashSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreSimHash);

            TextSimilarity w2vSimilarity = new W2vSimilarity();
            double scoreW2v = w2vSimilarity.getSimilarity(keywords, doc);
            System.out.println(keywords + " 和 " + doc + " 的相似度分值：" + scoreW2v);

        }

    }

    public static void threaddemo(){
        // 开始时间
        long start = System.currentTimeMillis();
        // Fixed thread number
        ExecutorService service = Executors.newFixedThreadPool(10);

        // Or un fixed thread number
        // The number of threads will increase with tasks
        // ExecutorService service = Executors.newCachedThreadPool(10);

        List<String> querys = new ArrayList<>();
        List<String> res= new ArrayList<>();
        List<String> titiles = new ArrayList<>();
        for (int i = 0; i < 3000; i++) {
//            querys.add("六味地黄丸360粒");
            titiles.add("张仲景六味地黄丸360粒");
        }
        for (String id :titiles ) {
            service.execute(new PredictTask(id,res));
        }

        // shutdown
        // this will get blocked until all task finish
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(res.size());
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }


    public static class PredictTask implements Runnable {

        String ids;
        List<String> res;

        public PredictTask(String ids,List<String> result) {
            this.ids = ids;
            this.res = result;
        }

        @Override
        public void run() {

            res.add(ids+"test");


        }
    }

    public static void threadpool() throws ExecutionException, InterruptedException {
        // 开始时间
        long start = System.currentTimeMillis();
        List<String> list = new ArrayList<String>();

        for (int i = 1; i <= 3000; i++) {
            list.add(i + "");
        }
//        List<String> querys = new ArrayList<>();
//        List<String> titiles = new ArrayList<>();
        for(int i =0;i<300;i++) {
//            querys.add("六味地黄丸360粒");
            list.add("张仲景六味地黄丸360粒");
        }

        // 每500条数据开启一条线程
        int threadSize = 500;
        // 总数据条数
        int dataSize = list.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;

        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<String> cutList = null;

        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = list.subList(threadSize * i, dataSize);
            } else {
                cutList = list.subList(threadSize * i, threadSize * (i + 1));
            }
            // System.out.println("第" + (i + 1) + "组：" + cutList.toString());
            final List<String> listStr = cutList;
            task = new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    System.out.println(Thread.currentThread().getName() + "线程：" + listStr);
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }

        List<Future<Integer>> results = exec.invokeAll(tasks);

        for (Future<Integer> future : results) {
            System.out.println(future.get());
        }

        // 关闭线程池
        exec.shutdown();
        System.out.println("线程任务执行结束");
        System.err.println("执行任务消耗了 ：" + (System.currentTimeMillis() - start) + "毫秒");
    }


    public static void main(String[] args) {
//        try{
//            threadpool();
//        }catch (ExecutionException e){
//            System.out.println(e);
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
        threaddemo();

    }

}
