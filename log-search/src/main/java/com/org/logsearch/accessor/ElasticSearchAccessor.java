package com.org.logsearch.accessor;

import org.elasticsearch.ElasticsearchParseException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.join.ParentJoinPlugin;
import org.elasticsearch.plugins.SearchPlugin;
import org.elasticsearch.search.SearchModule;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ElasticSearchAccessor {
    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient restHighLevelClient;

    @Autowired
    @Qualifier("TestIndexSearchRequest")
    SearchRequest testIndexSearchRequest;

    final Object ELASTIC_SEARCH_MODULE_LOCK = new Object();
    SearchModule searchModule;

    public SearchResponse searchByQueryPayload(String payload){
        try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
             .createParser(new NamedXContentRegistry(getSearchModule().getNamedXContents()),
                DeprecationHandler.THROW_UNSUPPORTED_OPERATION, payload)) {
            testIndexSearchRequest.source().parseXContent(parser);
            return restHighLevelClient.search(testIndexSearchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchParseException("Failed to parse content to map", e);
        }
    }

    public SearchResponse searchByQueryPayloadAndIndexName(String payload, String indexName){
        try (XContentParser parser = XContentFactory.xContent(XContentType.JSON)
                .createParser(new NamedXContentRegistry(getSearchModule().getNamedXContents()),
                        DeprecationHandler.THROW_UNSUPPORTED_OPERATION, payload)) {
            SearchRequest searchRequest = getSearchRequest(indexName);
            searchRequest.source().parseXContent(parser);
            return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchParseException("Failed to parse content to map", e);
        }
    }

    private SearchRequest getSearchRequest(String indexName) {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private SearchModule getSearchModule() {
        synchronized (ELASTIC_SEARCH_MODULE_LOCK) {
            if (searchModule == null) {
                List<SearchPlugin> plugins = new ArrayList<>();
                plugins.add(new ParentJoinPlugin());
                searchModule = new SearchModule(Settings.EMPTY, false, plugins);
            }
        }
        return searchModule;
    }
}
