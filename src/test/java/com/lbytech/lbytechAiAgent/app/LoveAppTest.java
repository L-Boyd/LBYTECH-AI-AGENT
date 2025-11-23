package com.lbytech.lbytechAiAgent.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class LoveAppTest {

    @Autowired
    private LoveApp loveApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是博哥";
        String answer = loveApp.doChat(message, chatId);

        // 第二轮
        message = "你是谁";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(message, chatId);

        // 第三轮
        message = "我是谁，我刚刚告诉你了";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(message, chatId);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是博哥，我想知道非暴力沟通应该如何运用在恋爱关系中";
        LoveApp.LoveReport loveReport = loveApp.doChatWithReport(message, chatId);
         Assertions.assertNotNull(loveReport);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有个朋友已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithRagCloud() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有个朋友已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRagCloud(message, chatId);
        Assertions.assertNotNull(answer);
    }
}