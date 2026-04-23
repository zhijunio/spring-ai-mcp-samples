package io.zhijun.spring.ai;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class GreeterTools {

    @Tool(description = "A tool that greets you in the selected language (e.g. english, french)")
    public String greet(String language) {
        if (!StringUtils.hasText(language)) {
            language = "";
        }
        return switch (language.toLowerCase()) {
            case "english" -> "Hello you!";
            case "french" -> "Salut toi!";
            case "chinese" -> "你好！";
            default -> "I don't understand language \"%s\". So I'm just going to say Hello!".formatted(language);
        };
    }
}
