package com.lbytech.lbytechAiAgent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于文件的聊天记忆实现类
 */
public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;

    private static final Kryo kryo = new Kryo();

    static {
        // 不需要手动注册，用自动注册
        kryo.setRegistrationRequired(false);
        // 实例化策略，指定默认策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    public FileBasedChatMemory(String baseDir) {
        this.BASE_DIR = baseDir;
        File file = new File(baseDir);
        if (!file.exists()) {
            boolean success = file.mkdirs();
            System.out.println(success);
        }
    }

    /**
     * 每个会话文件单独保存
     * @param conversationId
     * @return
     */
    private File getConversationFile(String conversationId) {
        return new File(BASE_DIR, conversationId + ".kryo");
    }

    /**
     * 获取或创建会话消息的列表
     * @param conversationId
     * @return
     */
    private List<Message> getOrCreateConversationMessage(String conversationId) {
        File file = getConversationFile(conversationId);
        List<Message> messages = new ArrayList<>();
        Input input = null;
        if (file.exists()) {
            try {
                input = new Input(new FileInputStream(file));
                messages = kryo.readObject(input, ArrayList.class);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                if (input != null) {
                    input.close();
                }
            }
        }
        return messages;
    }

    private void saveConversation(String conversationId, List<Message> messages) {
        File file = getConversationFile(conversationId);
        Output output = null;
        try {
            output = new Output(new FileOutputStream(file));
            kryo.writeObject(output, messages);
            output.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        List<Message> existingMessages = getOrCreateConversationMessage(conversationId);
        existingMessages.addAll(messages);
        saveConversation(conversationId, existingMessages);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> existingMessages = getOrCreateConversationMessage(conversationId);
        // 子列表形式
        //return existingMessages.subList(Math.max(0, existingMessages.size() - lastN), existingMessages.size());
        // Stream 形式
        return existingMessages.stream()
                .skip(Math.max(0, existingMessages.size() - lastN)) // 跳过多少条消息
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        File file = getConversationFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }
}
