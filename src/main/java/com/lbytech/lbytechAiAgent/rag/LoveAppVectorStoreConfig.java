package com.lbytech.lbytechAiAgent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 恋爱大师应用向量数据库配置（初始化基于内存的数据库bean）
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Autowired
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    public VectorStore loveAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        List<Document> documentList = loveAppDocumentLoader.loadLoveAppDocuments();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }

}
