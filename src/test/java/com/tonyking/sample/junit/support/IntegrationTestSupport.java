package com.tonyking.sample.junit.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class IntegrationTestSupport {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTestSupport.class);

    public static final ObjectMapper objectMapper = new ObjectMapper();

    public static final RestTemplate restTemplate = new RestTemplate();
    public static final String xAppCorrelationId ="x-appCorrelationId";

    static {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return true;
            }

            @Override
            public void handleError(ClientHttpResponse response) {

            }
        });
    }

    public static String readFileAsString(String path) throws URISyntaxException, IOException {
        return Files.lines(Paths.get(IntegrationTestSupport.class.getResource(path).toURI())).collect(Collectors.joining("\n"));
    }

    public static <T> T readObjectFromJson(String path, Class<T> clazz) throws IOException {
        return objectMapper.readValue(IntegrationTestSupport.class.getResourceAsStream(path), clazz);
    }

    public static IntegrationTestData dataFromFile(String file) throws IOException {
        JsonNode jsonNode = objectMapper.readValue(IntegrationTestSupport.class.getResourceAsStream(file), JsonNode.class);
        return getIntegrationTestData(file, jsonNode);
    }

    public static List<IntegrationTestData> dataFromFolder(String folderPath) throws URISyntaxException, IOException {
        List<IntegrationTestData> list = new ArrayList<>();
        Files.walk(Paths.get(IntegrationTestSupport.class.getClassLoader().getResource(folderPath).toURI()))
                .filter(Files::isRegularFile)
                .forEach(each -> list.add(buildData(each)));
        return list;
    }

    private static IntegrationTestData buildData(Path each){
        try {
            JsonNode jsonNode = objectMapper.readValue(each.toFile(), JsonNode.class);
            return getIntegrationTestData(each.toString(), jsonNode);
        } catch (Exception e) {
            logger.error("Building IntegrationTestData error {}", each.toString());
            return null;
        }
    }

    private static IntegrationTestData getIntegrationTestData(String fileName, JsonNode jsonNode) throws JsonProcessingException {
        return new IntegrationTestData(
                fileName,
                jsonNode.get("url").asText(),
                jsonNode.get("method").asText().toUpperCase(),
                jsonNode.get("description").asText(),
                objectMapper.writeValueAsString(jsonNode.get("requestBodyObject")),
                objectMapper.convertValue(jsonNode.get("headers"), HashMap.class),
                jsonNode.get("expectedHttpStatus").asInt(),
                jsonNode.get("expectedResponseBody") instanceof NullNode ? null: objectMapper.writeValueAsString(jsonNode.get("expectedResponseBody"))
        );
    }

    public static void testAndVerify(IntegrationTestData integrationTestData, Logger logger) throws Exception {
        logger.error("================test case started=============");
        logger.info("Testing with input file: {}", integrationTestData.getFileName());
        logger.info("This test case is for: {}", integrationTestData.getDescription());
        LocalDateTime startTime = LocalDateTime.now();
        logger.info("Start time: {}", startTime.format(DateTimeFormatter.ISO_DATE_TIME));

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(TestConfig.apiKey, TestConfig.apiSecret);
        integrationTestData.getHeaders().forEach(headers::add);
        if (Arrays.asList("random").equals(headers.get(xAppCorrelationId))) {
            headers.remove(xAppCorrelationId);
            headers.add(xAppCorrelationId, UUID.randomUUID().toString());
        }
        logger.info(xAppCorrelationId+" is: {}", headers.get(xAppCorrelationId));
        if (TestConfig.swimLane!=null) headers.add("x-swimlane", TestConfig.swimLane);
        HttpEntity<String> entity = new HttpEntity<>(integrationTestData.getRequestJson(), headers);

        ResponseEntity<String> responseEntity = IntegrationTestSupport.restTemplate.exchange(TestConfig.hostname + integrationTestData.getUrl(), HttpMethod.valueOf(integrationTestData.getMethod()), entity, String.class);

        logger.info(TestConfig.hostname + integrationTestData.getUrl());
        LocalDateTime endTime = LocalDateTime.now();
        logger.info("End time: {}", endTime.format(DateTimeFormatter.ISO_DATE_TIME));
        logger.info("Time spent: {} mini-second", endTime.toInstant(ZoneOffset.UTC).toEpochMilli()-startTime.toInstant(ZoneOffset.UTC).toEpochMilli());

        logger.info("Response status is : {}", responseEntity.getStatusCodeValue());
        logger.info("Response body is : {}", responseEntity.getBody());

        logger.error("================test case completed=============");
        Assertions.assertEquals(integrationTestData.getHttpResponseStatus().intValue(), responseEntity.getStatusCodeValue());
        JSONAssert.assertEquals(integrationTestData.getExpectedResponseJson(), responseEntity.getBody(), false);
    }
}