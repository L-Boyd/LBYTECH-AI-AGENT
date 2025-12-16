package com.lbytech.lbytechAiAgent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceDownloadToolTest {

    private ResourceDownloadTool resourceDonloadTool = new ResourceDownloadTool();

    @Test
    void downloadResource() {
        String url = "https://lbytechcn.oss-cn-shenzhen.aliyuncs.com/ai.md";
        String fileName = "ai.md";
        String result = resourceDonloadTool.downloadResource(url, fileName);
        System.out.println(result);
    }
}