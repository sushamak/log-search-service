package com.org.logsearch.controller;

import com.org.logsearch.service.LogSearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestController
public class LogSearchController {

    @Autowired
    LogSearchService logSearchService;

    @PostMapping("/search")
    @ResponseBody
    public Response searchByPayload(@RequestParam String payload) {
        SearchResponse searchResponse = logSearchService.performSearch(payload);
        return Response.ok(searchResponse.toString(), MediaType.APPLICATION_JSON_TYPE).build();
    }

}
