package com.paic.bst.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.paic.bst.feature.utils.tokenizer.Word;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description: ESUtil
 * date: 2020/11/16 4:40 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
@Setter
@Component
public class ESUtil {

    @Autowired
    private RestHighLevelClient client;

    public static final String DEFAULT_ANALYZER = "hanlp";
    public static final String INDEX_ANALYZER = "hanlp_index";

    public static ESUtil esUtil;
    @PostConstruct
    public void init(){
        esUtil = this;
        esUtil.client = this.client;
    }

    public List<String> getIkAnalyzeSearchTerms(String index,String text) {

        // 调用 IK 分词分词
        AnalyzeRequest.buildCustomAnalyzer(index,"ik_smart");
//        AnalyzeRequest an = new AnalyzeRequest().text(text).analyzer("ik_smart");

        Request request = new Request("GET","spu/_analyze");

        JSONObject entity = new JSONObject();
        entity.put("analyzer", "ik_max_word");  //ik_max_word   //ik_smart
        entity.put("text", text);
        request.setJsonEntity(entity.toJSONString());
        List<String> result = new ArrayList<>();
        try {
            Response response = client.getLowLevelClient().performRequest(request);
            JSONObject tokens = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));
            JSONArray arrays = tokens.getJSONArray("tokens");
            StringBuilder sb = new StringBuilder();
            if(arrays.size()>0) {
                for (int i = 0; i < arrays.size(); i++) {
                    JSONObject job = arrays.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
                    sb.append(job.get("token")).append(" ");
                    result.add(job.get("token").toString());
                }
            }
            log.info(sb.toString());

        }catch (IOException e){
            log.error("",e);
        }

        return result;

    }

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


    /**
     * 根据索引名以及ID查询数据
     * @param index 索引名称
     @Component
@Configuration  * @param id    数据Ｄ
     * @return
     */
    public String getDocument(String index, String id){
        GetResponse getResponse = null;
        try{

            GetRequest getRequest = new GetRequest(index,"_doc", id );
            getResponse = client.get(getRequest, RequestOptions.DEFAULT);
            System.out.println(JSONObject.toJSONString(getResponse));
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(getResponse);
    }

    /**
     * 进行全文检索
     * @param index
     * @param value
     * @param current
     * @param size
     * @return
     */
    public String keywordSearch(String index, String value,
                                int current, int size){
        List<Map<String, Object>> result = null;
        try{
            SearchRequest searchRequest = new SearchRequest();
            searchRequest.indices(index);

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //支持全词搜索的字段有：keywordName，keywordAuthor"
            searchSourceBuilder.query(QueryBuilders
                    .multiMatchQuery(value, "name"));
            searchSourceBuilder.from(current);
            searchSourceBuilder.size(size);

            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(JSONObject.toJSONString(searchResponse));
            //处理返回结果
            result = dealResult(searchResponse.getHits());
            client.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return JSONObject.toJSONString(result);
    }

    public List<Word> esSegment(String query){
        return esSegment(query,DEFAULT_ANALYZER);
    }

    public List<Word> esSegment(String query,String analyzer){
        List<Word> result = new ArrayList<>();

        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer(analyzer,query);
        try{
            AnalyzeResponse reponse = client.indices().analyze(request, RequestOptions.DEFAULT);
            if(reponse != null){
                List<AnalyzeResponse.AnalyzeToken> tokens = reponse.getTokens();
                if(!CollectionUtils.isEmpty(tokens)){
                    for(AnalyzeResponse.AnalyzeToken token:tokens){
                        Word word = new Word(token.getTerm(),token.getType());
                        result.add(word);
                    }
                }
            }
        }catch (IOException e){
            log.error("call es to segment failed!",e);
        }

        return result;
    }

    /**
     * 分页查询索引所有数据
     * @param index
     * @param current
     * @param size
     * @return
     * @throws IOException
     */
    public String searchAll(String index, int current, int size) throws IOException {
        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(current);
        searchSourceBuilder.size(size);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        System.out.println(JSONObject.toJSONString(searchResponse));
        //处理返回结果
        SearchHits hits = searchResponse.getHits();
        client.close();
        return JSONObject.toJSONString(hits);
    }

    private List<Map<String, Object>> dealResult(SearchHits hits){
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : hits.getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            result.add(map);
        }
        return result;
    }





}
