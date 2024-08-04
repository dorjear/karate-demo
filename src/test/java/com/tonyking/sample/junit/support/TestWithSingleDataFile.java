package com.tonyking.sample.junit.support;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWithSingleDataFile {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String filePath = filePath();

    @BeforeAll
    public static void setup() {
//        BasicConfigurator.resetConfiguration();
//        BasicConfigurator.configure();
    }

    @Test
    public void testWithSingleFile() throws Exception {
        logger.info(filePath);
        if(filePath == null) return;
        IntegrationTestData integrationTestData = IntegrationTestSupport.dataFromFile(filePath);
        IntegrationTestSupport.testAndVerify(integrationTestData, logger);
    }

    protected String filePath(){
        return null;
    }

}