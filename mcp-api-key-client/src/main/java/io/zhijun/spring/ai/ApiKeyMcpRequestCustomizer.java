package io.zhijun.spring.ai;

import io.modelcontextprotocol.client.transport.customizer.McpSyncHttpClientRequestCustomizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 为 MCP HTTP 请求添加 X-API-key 请求头，用于连接使用 API Key 认证的 16-mcp-server-api-key。
 * 默认请求头名由 mcp-server-security 约定为 X-API-key，值为 id.secret（如 api01.mycustomapikey）。
 */
@Configuration
public class ApiKeyMcpRequestCustomizer {

    public static final String X_API_KEY_HEADER = "X-API-key";

    @Bean
    McpSyncHttpClientRequestCustomizer apiKeyRequestCustomizer(
            @Value("${mcp.security.api-key:api01.mycustomapikey}") String apiKey) {
        return (builder, method, uri, body, context) ->
                builder.header(X_API_KEY_HEADER, apiKey);
    }
}
