package com.tonyking.sample.karate;
import com.intuit.karate.junit5.Karate;

class TestAllFeatures {

    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:com/tonyking/sample/karate/post").relativeTo(getClass());
    }

}