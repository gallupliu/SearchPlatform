package com.paic.bst.config;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * description: ESConfig
 * date: 2021/1/4 2:35 下午
 * author: gallup
 * version: 1.0
 */
@Slf4j
@Setter
@Component
@Primary
@ConfigurationProperties("es")
public class ESConfig {
    private String ip;
    private int port;

    @Bean
    public RestHighLevelClient getHighLevelClient(){
        HttpHost httpHost = new HttpHost(ip,port,"http");
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));
        log.info("es启动......");
        return client;
    }

}
