package com.tonyking.sample.junit.support;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestData {

    private String fileName;
    private String url;
    private String method;
    private String description;
    private String requestJson;
    private Map<String, String> headers = new HashMap<>();
    private Integer httpResponseStatus;
    private String expectedResponseJson;

    public IntegrationTestData(String fileName, String url, String method, String description, String requestJson, Map<String, String> headers, Integer httpResponseStatus, String expectedResponseJson) {
        this.fileName = fileName;
        this.url = url;
        this.method = method;
        this.description = description;
        this.requestJson = requestJson;
        this.headers = headers;
        this.httpResponseStatus = httpResponseStatus;
        this.expectedResponseJson = expectedResponseJson;
    }

    public String getFileName() {
        return fileName;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public String getDescription() {
        return description;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Integer getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public String getExpectedResponseJson() {
        return expectedResponseJson;
    }
}