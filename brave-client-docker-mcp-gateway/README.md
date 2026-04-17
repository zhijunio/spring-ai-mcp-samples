# Spring AI - Model Context Protocol (MCP) Brave Search Example

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker
- Git
- OpenAI API key
- Brave Search API key (Get one at https://brave.com/search/api/)

## How to Run

```bash
git clone https://github.com/zhijunio/spring-ai-mcp-sample.git
cd spring-ai-mcp-sample/brave-docker-mcp-gateway

docker compose up

export OPENAI_API_KEY='your-openai-api-key-here'
export BRAVE_API_KEY='your-brave-api-key-here'

./mvnw spring-boot:run
```

## How it Works

### MCP Client Configuration

The MCP client is configured using configuration files:

1. `application.properties`:

```properties
spring.ai.mcp.client.see.gateway.url=http://localhost:8811
```

This configuration:

1. Uses the Brave Search MCP server via Docker MCP Gateway
2. The Brave API key is passed from environment variables
3. Initializes a synchronous connection to the server

### Chat Integration

The ChatClient is configured with the MCP tool callbacks in the Application class:

```java
var chatClient = chatClientBuilder.defaultToolCallbacks(toolCallbackProvider).build();
```

This setup allows the AI model to:

- Understand when to use Brave Search
- Format queries appropriately
- Process and incorporate search results into responses
