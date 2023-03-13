package org.dataguardians.openai.endpoints;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.dataguardians.openai.api.chat.ChatRequest;
import org.dataguardians.openai.api.chat.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a request to the OpenAI Chat API endpoint.
 *
 * This class provides a convenient way to build a request to the OpenAI Chat API. It includes methods to set the input
 * text, the model to use, and the parameters for the request, among others. Once the request is built, it can be sent
 * using the {@link ChatApiEndpoint#send(ChatApiEndpointRequest)} method.
 *
 * Example usage:
 *
 * <pre>{@code
 * ChatApiEndpointRequest request = new ChatApiEndpointRequest()
 *         .setModel("davinci")
 *         .setInput("Hello, world!")
 *         .addParameter("temperature", 0.5)
 *         .addParameter("max_tokens", 10);
 *
 * ChatApiEndpoint endpoint = new ChatApiEndpoint(apiKey);
 * ChatApiResponse response = endpoint.send(request);
 * }</pre>
 *
 * @see ChatApiEndpoint
 * @see ChatApiResponse
 */
@Data
@SuperBuilder
public class ChatApiEndpointRequest extends ApiEndPointRequest {

    public static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    @Override
    public String getEndpoint() {
        return API_ENDPOINT;
    }

    /**
     * Creates a new instance of the ChatApiEndpoint with the specified API key.
     *
     * This method is used to create a new instance of the ChatApiEndpoint with the specified API key. The API key is
     * required to send requests to the OpenAI Chat API endpoint. If the API key is invalid or not provided, an
     * IllegalArgumentException will be thrown.
     *
     * Example usage:
     *
     * <pre>{@code
     * ChatApiEndpoint endpoint = ChatApiEndpoint.create("my-api-key");
     * }</pre>
     *
     * @param apiKey The API key to use for requests to the OpenAI Chat API endpoint.
     * @return A new instance of the ChatApiEndpoint.
     * @throws IllegalArgumentException If the API key is null or empty.
     */
    @Override
    public Object create() {
        List<Message> messages = new ArrayList<>();
        String role = StringUtils.isBlank(user) ? "user" : user;
        messages.add(Message.builder().role(role).content(input).build());
        ChatRequest requestBody = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .user(role)
                .maxTokens(maxTokens)
                .messages(messages)
                .build();
        return requestBody;
    }

}
