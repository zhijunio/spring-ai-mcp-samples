
package io.zhijun.spring.ai.client;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.transport.WebClientStreamableHttpTransport;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * @author Christian Tzolov
 */
public class StreamableHttpClient {

    public static void main(String[] args) {

        var transport = WebClientStreamableHttpTransport.builder(WebClient.builder().baseUrl("http://localhost:8080"))
                .build();

        var client = McpClient.sync(transport).build();

        client.initialize();

        client.ping();

        // List and demonstrate tools
        McpSchema.ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);

        CallToolResult toUpperCaseResult = client.callTool(new CallToolRequest("toUpperCase",
                Map.of("input", "hellow")));
        System.out.println("toUpperCase: " + toUpperCaseResult.content());

        CallToolResult weatherForcastResult = client.callTool(new CallToolRequest("getWeatherForecastByLocation",
                Map.of("latitude", "47.6062", "longitude", "-122.3321")));
        System.out.println("Weather Forcast: " + weatherForcastResult.content());

        CallToolResult alertResult = client.callTool(new CallToolRequest("getAlerts", Map.of("state", "NY")));
        System.out.println("Alert Response = " + alertResult.content());

        client.closeGracefully();
    }

}