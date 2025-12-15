package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSearchToolTest {

    @Autowired
    private WebSearchTool tool;

    @Test
    void searchWeb() {
        String query = "腾讯总部在哪";
        String result = tool.searchWeb(query);
        System.out.println(result);
    }
}