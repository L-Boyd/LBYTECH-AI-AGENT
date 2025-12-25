package com.lbytech.lbytechAiAgent.agent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LbyManusTest {

    @Autowired
    private LbyManus lbyManus;

    @Test
    void testLbyManus() {
        String userPrompt = """
                帮我在深圳市福田区深圳图书馆附近5km找个合适的约会地点，
                并结合一些网络图片，指定一份详细的约会计划
                以PDF格式输出
                """;
        String answer = lbyManus.run(userPrompt);
        assertNotNull(answer);
        System.out.println(answer);
    }
}