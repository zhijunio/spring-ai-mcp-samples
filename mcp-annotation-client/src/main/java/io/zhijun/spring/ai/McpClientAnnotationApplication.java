package io.zhijun.spring.ai;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Map;

@SpringBootApplication
public class McpClientAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientAnnotationApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(List<McpSyncClient> mcpClients, ConfigurableApplicationContext context) {

        return args -> {

            for (McpSyncClient mcpClient : mcpClients) {
                System.out.println(">>> MCP Client: " + mcpClient.getClientInfo());

                // Call a tool that sends progress notifications
                McpSchema.CallToolRequest toolRequest = McpSchema.CallToolRequest.builder()
                        .name("tool1")
                        .arguments(Map.of("input", "test input"))
                        .progressToken(666)
                        .build();
                McpSchema.CallToolResult response = mcpClient.callTool(toolRequest);
                System.out.println("Tool response: " + response);

                McpSchema.CompleteResult nameCompletion = mcpClient.completeCompletion(
                        new McpSchema.CompleteRequest(
                                new McpSchema.PromptReference("personalized-message"),
                                new McpSchema.CompleteRequest.CompleteArgument("name", "J")));

                System.out.println("Name completions: " + nameCompletion.completion());

                String nameValue = nameCompletion.completion().values().get(3);

                try {
                    McpSchema.GetPromptResult promptResponse = mcpClient
                            .getPrompt(new McpSchema.GetPromptRequest("personalized-message", Map.of("name", nameValue)));

                    System.out.println("Prompt response: " + promptResponse);
                } catch (Exception e) {
                    System.err.println("Error getting prompt: " + e.getMessage());
                }

                nameCompletion = mcpClient.completeCompletion(
                        new McpSchema.CompleteRequest(
                                new McpSchema.ResourceReference("user-status://{username}"),
                                new McpSchema.CompleteRequest.CompleteArgument("username", "J")));

                System.out.println("Name completions: " + nameCompletion.completion());

                var resourceResponse = mcpClient.readResource(new McpSchema.ReadResourceRequest("user-status://" + nameCompletion.completion().values().get(0)));

                System.out.println("Resource response: " + resourceResponse);

            }

            context.close();
        };
    }
}
