package io.zhijun.spring.ai;

import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntity;
import org.springaicommunity.mcp.security.server.apikey.ApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.apikey.memory.ApiKeyEntityImpl;
import org.springaicommunity.mcp.security.server.apikey.memory.InMemoryApiKeyEntityRepository;
import org.springaicommunity.mcp.security.server.config.McpApiKeyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class McpServerSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated())
                .with(
                        McpApiKeyConfigurer.mcpServerApiKey(),
                        apiKey -> apiKey.apiKeyRepository(apiKeyRepository())
                )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    /**
     * CORS for MCP Inspector / browser clients. Aligned with
     * <a href="https://github.com/spring-ai-community/mcp-security/tree/main/samples/sample-mcp-server-api-key">sample-mcp-server-api-key</a>.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * In-memory API key repository for demo. Default header: X-API-key with value "api01.mycustomapikey".
     * Optional: apiKey.headerName("CUSTOM-API-KEY") or apiKey.authenticationConverter(...) per
     * <a href="https://docs.spring.io/spring-ai/reference/api/mcp/mcp-security.html">MCP Security</a>.
     * For production, implement your own ApiKeyEntityRepository (e.g. database); InMemoryApiKeyEntityRepository
     * uses bcrypt and is not suited for high-traffic use.
     */
    private ApiKeyEntityRepository<ApiKeyEntity> apiKeyRepository() {
        ApiKeyEntity apiKey = ApiKeyEntityImpl.builder()
                .name("demo api key")
                .id("api01")
                .secret("mycustomapikey")
                .build();
        return new InMemoryApiKeyEntityRepository<>(List.of(apiKey));
    }
}
