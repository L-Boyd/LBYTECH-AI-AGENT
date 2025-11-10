package com.lbytech.lbytechAiAgent.invoke;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

/**
 * spring ai框架调用大模型
 */
@SpringBootTest
@Component
public class SpringAiInvoke {  // 实现单次执行方法，项目启动时扫描@Component，执行run方法

    @Autowired
    private ChatModel chatModel;

    @Test
    public void test() {
        AssistantMessage asssitantMessage = chatModel.call(new Prompt("你是谁"))
                .getResult()
                .getOutput();
        System.out.println(asssitantMessage.getText());
    }
}
