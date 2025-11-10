package com.lbytech.lbytechAiAgent.invoke;

import com.lbytech.lbytechAiAgent.config.DashScopeConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LangChainAiInvoke {

    @Autowired
    private DashScopeConfig dashScopeConfig;

    @Test
    public void langChainAiInvoke() {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(dashScopeConfig.getApiKey())
                .modelName("qwen-plus")
                .build();

        String answer = qwenChatModel.chat("我是langchan4j，你是谁？");
        System.out.println(answer);
    }

}
