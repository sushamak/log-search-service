package com.org.logsearch.service;

import com.org.logsearch.accessor.ElasticSearchAccessor;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogSearchService {
    @Autowired
    ElasticSearchAccessor elasticSearchAccessor;
    public SearchResponse performSearch(String payload) {
     return elasticSearchAccessor.searchByQueryPayload(payload);
    }
}
