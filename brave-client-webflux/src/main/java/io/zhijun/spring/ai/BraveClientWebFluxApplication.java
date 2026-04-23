package io.zhijun.spring.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BraveClientWebFluxApplication {
    private static final Logger log = LoggerFactory.getLogger(BraveClientWebFluxApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BraveClientWebFluxApplication.class, args);
    }

    @Bean
    public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                                 ToolCallbackProvider toolCallbackProvider,
                                                 ConfigurableApplicationContext context) {
        return args -> {
            var chatClient = chatClientBuilder.defaultToolCallbacks(toolCallbackProvider).build();

            String question = "Does Spring AI supports the Model Context Protocol? Please provide some references.";
            log.info("QUESTION: {}\n", question);
            log.info("ASSISTANT: {}\n", chatClient.prompt(question).call().content());

            context.close();
        };
    }

}
