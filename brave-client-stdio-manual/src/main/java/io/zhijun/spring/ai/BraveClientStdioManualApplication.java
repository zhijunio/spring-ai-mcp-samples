package io.zhijun.spring.ai;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.List;

@SpringBootApplication
public class BraveClientStdioManualApplication {
    private static final Logger log = LoggerFactory.getLogger(BraveClientStdioManualApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BraveClientStdioManualApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                                 @Autowired(required = false) List<McpSyncClient> mcpSyncClients,
                                                 @Autowired(required = false) McpSyncClient mcpClient,
                                                 ConfigurableApplicationContext context) {
        return args -> {
            // Determine which client(s) to use: auto-configured list or programmatic single
            List<McpSyncClient> clients = (mcpSyncClients != null && !mcpSyncClients.isEmpty())
                    ? mcpSyncClients
                    : (mcpClient != null ? List.of(mcpClient) : List.of());

            if (clients.isEmpty()) {
                log.error("No MCP clients available. Check your configuration.");
                context.close();
                return;
            }

            var chatClient = chatClientBuilder
                    .defaultToolCallbacks(new SyncMcpToolCallbackProvider(clients))
                    .build();

            String question = "Does Spring AI supports the Model Context Protocol? Please provide some references.";
            log.info("QUESTION: {}\n", question);
            log.info("ASSISTANT: {}\n", chatClient.prompt(question).call().content());

            context.close();
        };
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnMissingBean(McpSyncClient.class)
    public McpSyncClient mcpClient() {
        // Linux/Mac: npx can be executed directly
        ServerParameters stdioParams = ServerParameters.builder("npx")
                .args("-y", "@modelcontextprotocol/server-brave-search")
                .build();

        var mcpClient = McpClient.sync(new StdioClientTransport(stdioParams, McpJsonMapper.createDefault()))
                .requestTimeout(Duration.ofSeconds(10))
                .build();

        var init = mcpClient.initialize();
        System.out.println("MCP Initialized: " + init);

        return mcpClient;
    }

}
