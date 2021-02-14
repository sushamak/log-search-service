package com.org.logsearch.config;

import com.org.logsearch.Utils.ElasticUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Value("${elastic.host}")
    String host;

    @Value("${elastic.username}")
    String elasticUser;

    @Value("${elastic.password}")
    String elasticPassword;

    @Value("${elastic.test.indexName}")
    String testIndexName;

    @Value("${elastic.rest.client.connection.timeout}")
    int connectionTimeOut;

    @Value("${elastic.rest.client.socket.timeout}")
    int socketTimeOut;

    @Bean("restHighLevelClient")
    public RestHighLevelClient getHighLevelClient() {
        return ElasticUtils.getHighLevelClient(host, connectionTimeOut, socketTimeOut);
    }

    @Bean("TestIndexSearchRequest")
    public SearchRequest getTestIndexSearchRequest(){
        SearchRequest searchRequest = new SearchRequest(testIndexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder);
        return searchRequest;

    }


}
