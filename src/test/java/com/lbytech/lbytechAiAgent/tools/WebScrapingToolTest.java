package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebScrapingToolTest {

    private WebScrapingTool tool = new WebScrapingTool();

    @Test
    void scrapeWebPage() {
        String url = "http://www.lbytech.cn";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
        System.out.println(result);
    }
}