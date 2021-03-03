package com.paic.bst.feature.similarity;

import com.paic.bst.feature.similarity.StringSimilarity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * description: AsyncTask
 * date: 2021/3/3 2:01 下午
 * author: gallup
 * version: 1.0
 */
@Component
@Slf4j
public class AsyncTask {
    @Autowired
    StringSimilarity stringSimilarity;

    @Async("asyncTaskExecutor")
    public CompletableFuture<DataModel> getSortByCompletableFuture(String id,List<String> featureDistance) {
        DataModel data = new DataModel();
        List<Integer> result = stringSimilarity.getDistance("张仲景六味地黄丸360粒", id, featureDistance);
        data.setV1(result.get(0));
        data.setV1(result.get(1));
        data.setV1(result.get(2));

        return CompletableFuture.completedFuture(data);
    }
}
