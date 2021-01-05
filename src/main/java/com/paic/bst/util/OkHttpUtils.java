package com.paic.bst.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * description: OkHttpUtils
 * date: 2021/1/1 3:15 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
@Component
@Service
public class OkHttpUtils {
//    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
//    private static final MediaType XML = MediaType.parse("application/xml;charset=utf-8");
//
//    @Autowired
//    private OkHttpClient okHttpClient;
//
//    public String doGet(String url){ return doGet(url,null,null);}
//
//    public String doGet(String url, Map<String,String> params){return doGet(url,params,null);}
//
//    public String doGet(String url,String[] headers){return doGet(url,null,headers);}
//
//    public String doGet(String url,Map<String,String> params,String[] headers){
//        StringBuilder sb = new StringBuilder(url);
//        if(params != null && params.keySet().size() > 0){
//            boolean firstFlag = true;
//            for(String key:params.keySet()) {
//                if (firstFlag){
//                    sb.append("?").append(key).append("=").append(params.get(key));
//                    firstFlag = false;
//                }else{
//                    sb.append("&").append(key).append("=").append(params.get(key));
//                }
//            }
//        }
//
//        Request.Builder builder = new Request.Builder();
//        if( headers != null && headers.length >0){
//            if(headers.length%2 == 0){
//                for(int i = 0;i< headers.length; i= i+2){
//                    builder.addHeader(headers[i],headers[i+1]);
//                }
//            }else{
//                log.error("headers's length[{}] is error",headers.length);
//            }
//        }
//
//        Request request = builder.url(sb.toString()).build();
//        log.info("do get request and url[{}]",sb.toString());
//        return execute(request);
//    }
//
//    public String doPost(String url,Map<String,String> params){
//        FormBody.Builder builder = new FormBody.Builder();
//
//        if (params != null && params.keySet().size() > 0){
//            for(String key:params.keySet()){
//                builder.add(key,params.get(key));
//            }
//        }
//
//        Request request = new Request.Builder().url(url).post(builder.build()).build();
//        log.info("do post request and url[{}]",url);
//        return execute(request);
//    }
//
//    public String doPostJson(String url,String json){
//        log.info("do post request and url[{}]",url);
//        return executePost(url,json,JSON);
//    }
//
//    public String doPostXml(String url,String xml){
//        log.info("do post request and url[{}]",url);
//        return executePost(url,xml,XML);
//    }
//
//    public String executePost(String url,String data,MediaType contentType){
//        RequestBody requestBody = RequestBody.create(contentType,data);
//        Request request = new Request.Builder().url(url).post(requestBody).build();
//
//        return execute(request);
//    }
//
//    private String execute(Request request){
//        Response response = null;
//        try{
//            response = okHttpClient.newCall(request).execute();
//            if(response.isSuccessful()){
//                return response.body().string();
//            }
//        }catch (Exception e){
//            log.error(ExceptionUtils.getMessage(e));
//        }finally {
//            if (response != null){
//                response.close();
//            }
//        }
//        return "";
//    }

}
