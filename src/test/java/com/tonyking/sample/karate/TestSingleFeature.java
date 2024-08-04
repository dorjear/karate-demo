package com.tonyking.sample.karate;

import com.intuit.karate.junit5.Karate;

class TestSingleFeature {

    String featurePath = "post/e2e/CreateAndUpdatePostUsingCommonComponent.feature";
    @Karate.Test
    Karate testOne() {
        return Karate.run(featurePath).relativeTo(getClass());
    }

}