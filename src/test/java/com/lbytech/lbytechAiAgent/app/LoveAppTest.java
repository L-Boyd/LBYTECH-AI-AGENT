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

    @Test
    void doChatWithRagPgVector() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有个朋友已经结婚了，但是婚后关系不太亲密，怎么办？";
        String answer = loveApp.doChatWithRagPgVector(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithCustomAdvisor() {
        String chatId = UUID.randomUUID().toString();
        String message = "我有个朋友已经结婚了，但是婚后关系不太亲密，怎么办？";
        // 把过滤标签设置为“单身”，应该检索不到文章
        String answer = loveApp.doChatWithCustomAdvisor(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatCanCallTools() {
        String chatId = UUID.randomUUID().toString();
        // 测试网页搜索问题
        String message1 = "周末想带女朋友去上海约会，推荐几个合适的小众情侣打卡地";
        String answer = loveApp.doChatCanCallTools(message1, chatId);
        Assertions.assertNotNull(answer);

        // 测试网页抓取
        String message2 = "最近吵架了，看看http://www.lbytech.cn的其他情侣是怎么解决矛盾的";
        answer = loveApp.doChatCanCallTools(message2, chatId);
        Assertions.assertNotNull(answer);

        // 测试图片下载
        String message3 = "下载一张适合做手机壁纸的星空情侣图片为文件";
        answer = loveApp.doChatCanCallTools(message3, chatId);
        Assertions.assertNotNull(answer);

        // 测试终端操作
        String message4 = "执行python3脚本来生成数据分析报告";
        answer = loveApp.doChatCanCallTools(message4, chatId);
        Assertions.assertNotNull(answer);

        // 测试文件操作
        String message5 = "保存我的恋爱档案为文件";
        answer = loveApp.doChatCanCallTools(message5, chatId);
        Assertions.assertNotNull(answer);

        // 测试PDF生成
        String message6 = "生成一份’七夕约会计划‘PDF，包含餐厅预定、活动流程和礼物清单";
        answer = loveApp.doChatCanCallTools(message6, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message1 = "周末想带女朋友去上海约会，推荐几个合适的小众情侣打卡地。如有调用工具，请告诉我。";
        String answer = loveApp.doChatWithMcp(message1, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMyMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "帮我找一些浪漫的图片";
        String answer = loveApp.doChatWithMcp(message, chatId);
        Assertions.assertNotNull(answer);
    }
}