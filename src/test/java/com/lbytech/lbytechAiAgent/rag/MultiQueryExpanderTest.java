package com.lbytech.lbytechAiAgent.rag;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 测试查询扩展器
 */
@SpringBootTest
public class MultiQueryExpanderTest {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    public List<Query> expand(String query) {
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .build();
        List<Query> queries = queryExpander.expand(new Query(query));
        return queries;
    }

     @Test
    void testExpand() {
        List<Query> queries = expand("博哥是谁");
         Assertions.assertNotNull(queries);
        System.out.println(queries);
    }

}
