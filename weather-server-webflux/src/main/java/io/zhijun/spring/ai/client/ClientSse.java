
package io.zhijun.spring.ai.client;

import io.modelcontextprotocol.client.transport.WebFluxSseClientTransport;
import io.modelcontextprotocol.json.McpJsonMapper;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * @author Christian Tzolov
 */
public class ClientSse {

    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(WebClient.builder().baseUrl("http://localhost:8080"), McpJsonMapper.createDefault());
        new SampleClient(transport).run();
    }

}