package com.tonyking.sample.junit.support;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestWithFolder {

    public final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String folderPath = folderPath();

    @BeforeAll
    public static void setup() {
//        BasicConfigurator.resetConfiguration();
//        BasicConfigurator.configure();
    }

    @Test
    public void testWithFolder() throws Exception {
        logger.info(folderPath);
        if (folderPath==null) return;
        List<IntegrationTestData> testDatas = IntegrationTestSupport.dataFromFolder(folderPath);
        for (IntegrationTestData each : testDatas){
            IntegrationTestSupport.testAndVerify(each, logger);
        }
    }

    protected String folderPath(){
        return null;
    }

}
