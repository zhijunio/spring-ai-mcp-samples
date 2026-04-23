package io.zhijun.spring.ai;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpServerAnnotationApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerAnnotationApplication.class, args);
    }

    // Note: this is not MCP Annotations related, but demonstrates how to register a SpringAI tool
    // callback provider as MCP tools along with the @McpTool such
    @Bean
    ToolCallbackProvider weatherTools(WeatherService tools) {
        return MethodToolCallbackProvider.builder().toolObjects(tools).build();
    }

    public record TextInput(String input) {
    }

    @Bean
    public ToolCallback toUpperCase() {
        return FunctionToolCallback.builder("toUpperCase", (TextInput input) -> input.input().toUpperCase())
                .inputType(TextInput.class)
                .description("Put the text to upper case")
                .build();
    }
}
