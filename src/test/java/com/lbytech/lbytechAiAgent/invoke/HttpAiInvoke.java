package com.lbytech.lbytechAiAgent.invoke;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.lbytech.lbytechAiAgent.config.DashScopeConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// 测试http方式调用ai
public class HttpAiInvoke {

    @Autowired
    private DashScopeConfig dashScopeConfig;

    @Test
    void testHtttAi() {
        String apiKey = dashScopeConfig.getApiKey();

        // 构建请求体JSON
        JSONObject requestBody = new JSONObject();
        requestBody.set("model", "qwen-plus");

        // 构建messages数组
        JSONArray messages = new JSONArray();
        // system消息
        JSONObject systemMsg = new JSONObject();
        systemMsg.set("role", "system");
        systemMsg.set("content", "You are a helpful assistant.");
        messages.add(systemMsg);

        // user消息
        JSONObject userMsg = new JSONObject();
        userMsg.set("role", "user");
        userMsg.set("content", "你是谁？");
        messages.add(userMsg);

        requestBody.set("messages", messages);

        // 发送POST请求
        String result = HttpRequest.post("https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions")
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE, "application/json")
                .body(requestBody.toString())
                .execute()
                .body();

        // 输出响应结果
        System.out.println(result);
    }
}
