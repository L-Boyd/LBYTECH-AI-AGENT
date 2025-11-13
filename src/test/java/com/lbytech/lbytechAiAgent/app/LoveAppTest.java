package com.lbytech.lbytechAiAgent.app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
}