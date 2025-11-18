package com.lbytech.lbytechAiAgent.rag;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppDocumentLoaderTest {

    @Autowired
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Test
    void loadLoveAppDocument() {
        loveAppDocumentLoader.loadLoveAppDocuments();
    }
}