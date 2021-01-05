package com.paic.bst.util.analyzer;


import com.paic.bst.util.ESUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: ikAnalyzerUtils
 * date: 2021/1/1 4:14 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
public class IkAnalyzerUtils {

    @Autowired
    private ESUtil esUtil;

    @Autowired
    private RestHighLevelClient client;

    public static IkAnalyzerUtils ikAnalyzerUtils;

    @PostConstruct
    public void init(){
        ikAnalyzerUtils = this;
        ikAnalyzerUtils.client = this.client;
        IkAnalyzerUtils ikAnalyzerUtils = new IkAnalyzerUtils();
        ikAnalyzerUtils.getTokensTest("中国平安技术（深圳）责任有限公司");
        log.info("ik启动......");
    }

//    @Autowired
//    private OkHttpUtils okHttpUtils;
//    @Autowired
//    private RedisUtils redisUtils;

    private float max_weight = 5;

    public static final String DEFAULT_ANALYZER = "ik_smart";
    public static final String INDEX_ANALYZER = "ik_max_word";
    public static final String DEFAULT_RESULT = "result";
    public static final String VECTOR_RESULT = "dense_vector";
    public static final String VECTOR_WEIGHT = "vector_weight";
    public static final String WINDOW_SIZE = "window_size";
    public static final String THRESHOLD = "threshold";
    public static final String NER_KEY_FORMAT = "ik_smart";
    public static final String VECTOR_KEY_FORMAT = "ik_smart";
    public static final long TIME_OUT = 2 * 60 * 60;
    public static final String NER_STATUS = "200";
    public static final String VECTOR_SIMILAR_TABLE = "table";
    public static final String VECTOR_SIMILAR_QUERY_TEXT = "query_text";
    public static final String VECTOR_SIMILAR_TOPK = "topk";
    public static final long AI_TIMEOUT_MIN = 1000L;

    public List<String> getTokensTest(String query){
        return esUtil.getTokens(query);
    }
    public List<String> getTokens(String query){
        return getTokens(query,DEFAULT_ANALYZER);
    }

    public List<String> getTokens(String query,String analyzer){
        List<String> result = new ArrayList<>();
        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer(analyzer,query);
        try{
            AnalyzeResponse reponse = client.indices().analyze(request, RequestOptions.DEFAULT);
            if(reponse != null){
                List<AnalyzeResponse.AnalyzeToken> tokens = reponse.getTokens();
                if(!CollectionUtils.isEmpty(tokens)){
                    for(AnalyzeResponse.AnalyzeToken token:tokens){
                        result.add(token.getTerm());
                    }
                }
            }
        }catch (IOException e){
            log.error("call es to segment failed!",e);
        }
        return result;
    }

    public List<AnalyzeResponse.AnalyzeToken> getAnalyzerTokens(String query,String analyzer){
        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer(analyzer,query);
        try{
            AnalyzeResponse response = client.indices().analyze(request,RequestOptions.DEFAULT);
            if(response != null){
                List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
                return tokens;
            }
        }catch (IOException e){
            log.error("call es to segment failed!",e);
        }
        return null;
    }

//    public static void main(String[] args){
//        IkAnalyzerUtils ikAnalyzerUtils = new IkAnalyzerUtils();
//        ikAnalyzerUtils.getTokensTest("中国平安技术（深圳）责任有限公司");
//
//
//    }


}
