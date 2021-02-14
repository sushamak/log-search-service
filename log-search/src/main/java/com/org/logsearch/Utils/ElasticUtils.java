package com.org.logsearch.Utils;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.List;

public class ElasticUtils {

    public static RestHighLevelClient getHighLevelClient(String host, String elasticUser, String elasticPassword, int connectTimeout, int socketTimeout) {
        List<HttpHost> httpHostList = new ArrayList<>();
        String[] addr = host.split(",");
        for (String add : addr) {
            String[] pair = add.split(":");
            Integer port = Integer.valueOf(pair[1]);
            httpHostList.add(new HttpHost(pair[0], port));
        }
        CredentialsProvider credentialProvider = new BasicCredentialsProvider();
        credentialProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticUser, elasticPassword));
        return new RestHighLevelClient(
                RestClient.builder(httpHostList.toArray(new HttpHost[httpHostList.size()]))
                        .setRequestConfigCallback(requestConfigBuilder ->
                                requestConfigBuilder.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout))
                        .setHttpClientConfigCallback(configBuilder ->
                                configBuilder.setDefaultCredentialsProvider(credentialProvider))
        );
    }

    public static RestHighLevelClient getHighLevelClient(String host, int connectTimeout, int socketTimeout) {
        List<HttpHost> httpHostList = new ArrayList<>();
        String[] addr = host.split(",");
        for (String add : addr) {
            String[] pair = add.split(":");
            Integer port = Integer.valueOf(pair[1]);
            httpHostList.add(new HttpHost(pair[0], port));
        }
        return new RestHighLevelClient(
                RestClient.builder(httpHostList.toArray(new HttpHost[httpHostList.size()]))
                        .setRequestConfigCallback(requestConfigBuilder ->
                                requestConfigBuilder.setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout))
        );
    }
}
