package com.lbytech.lbytechAiAgent.rag;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于AI的文档元信息增强器（为文档补充元信息）
 */
@Component
public class MyKeywordEnricher {

    @Autowired
    private ChatModel dashscopeChatModel;

    /**
     * 增强文档列表中的关键词
     * @param documents 原始文档列表
     * @return 增强后的文档列表
     */
    public List<Document> enrichDocuments(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(dashscopeChatModel, 5);
        return keywordMetadataEnricher.apply(documents);
    }

}
