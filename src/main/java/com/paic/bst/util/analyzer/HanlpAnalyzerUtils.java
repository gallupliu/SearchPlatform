package com.paic.bst.util.analyzer;


import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * description: hanlpAnalyzerUtils
 * date: 2021/1/1 4:14 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
@Component
public class HanlpAnalyzerUtils {
    @Autowired
    private RestHighLevelClient client;
    public static HanlpAnalyzerUtils hanlpAnalyzerUtils;

    @PostConstruct
    public void init(){
        hanlpAnalyzerUtils = this;
        hanlpAnalyzerUtils.client = this.client;
    }

//    private static final Logger log = LoggerFactory.getLogger(HanlpAnalyzerUtils.class);
    public static final String DEFAULT_ANALYZER = "hanlp";
    public static final String INDEX_ANALYZER = "hanlp_index";

    public  List<String> getTokens(String query){
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

    public static void main(String[] args) {
        HanlpAnalyzerUtils hanlpAnalyzerUtils = new HanlpAnalyzerUtils();

        hanlpAnalyzerUtils.getTokens("中国平安技术（深圳）责任有限公司");
    }
}
