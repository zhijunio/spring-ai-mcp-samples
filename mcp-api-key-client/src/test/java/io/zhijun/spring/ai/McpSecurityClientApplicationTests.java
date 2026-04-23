package io.zhijun.spring.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.List;

@SpringBootTest(properties = {
        "spring.ai.mcp.client.enabled=false",
        "spring.ai.mcp.client.toolcallback.enabled=false"
})
@Import(McpSecurityClientApplicationTests.TestConfig.class)
class McpSecurityClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        ToolCallbackProvider toolCallbackProvider() {
            return ToolCallbackProvider.from(List.of());
        }
    }
}
