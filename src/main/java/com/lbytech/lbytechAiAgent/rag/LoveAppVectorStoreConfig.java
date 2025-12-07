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

    @Autowired
    private MyTokenTextSplitter myTokenTextSplitter;

    @Autowired
    private MyKeywordEnricher myKeywordEnricher;

    @Bean
    public VectorStore loveAppVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        // 加载文档
        List<Document> documentList = loveAppDocumentLoader.loadLoveAppDocuments();

        // 自主切分文档
        //List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documentList);

        // 自动补充关键词元信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documentList);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }

}
