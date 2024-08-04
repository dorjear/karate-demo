package com.tonyking.sample.junit.biz.posts;

import com.tonyking.sample.junit.support.TestWithSingleDataFile;

public class UpdatePostTest extends TestWithSingleDataFile {
    @Override
    protected String filePath(){
        return "/com/tonyking/sample/junit/posts/UpdatePost.json";
    }
}
