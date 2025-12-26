package com.lbytech.lbytechAiAgent.controller;

import com.lbytech.lbytechAiAgent.agent.LbyManus;
import com.lbytech.lbytechAiAgent.app.LoveApp;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private LoveApp loveApp;

    @Autowired
    private ToolCallback[] allTools;

    @Autowired
    private ToolCallbackProvider toolCallbackProvider;

    @Autowired
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用LoveApp
     *
     * @param message 消息内容
     * @param chatId  聊天ID
     * @return 回复内容
     */
    @GetMapping("/love_app/chat/sync")
    public String doChatWithLoveAppSync(String message, String chatId) {
        return loveApp.doChat(message, chatId);
    }

    /**
     * sse流式调用LoveApp
     *
     * @param message 消息内容
     * @param chatId  聊天ID
     * @return 回复内容流
     */
    @GetMapping(value = "/love_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // 返回方式
    public Flux<String> doChatWithLoveAppAsync(String message, String chatId) {
        return loveApp.doChatByStream(message, chatId);
    }

    /**
     * sse流式调用LoveApp
     *
     * @param message 消息内容
     * @param chatId  聊天ID
     * @return 回复内容流
     */
    @GetMapping(value = "/love_app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppAsyncSseEmitter(String message, String chatId) {
        // 超时时间设置为180秒的SseEmitter
        SseEmitter sseEmitter = new SseEmitter(180000L);

        // 获取Flux响应式数据流并直接通过订阅推送给SseEmitter
        loveApp.doChatByStream(message, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);

        return sseEmitter;
    }

    /**
     * sse流式调用LbyManus
     *
     * @param message 消息内容
     * @return 回复内容流
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithLbyManus(String message) {
        LbyManus lbyManus = new LbyManus(allTools, toolCallbackProvider, dashscopeChatModel);
        return lbyManus.runByStream(message);
    }
}
