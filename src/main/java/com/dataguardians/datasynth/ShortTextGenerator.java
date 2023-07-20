package com.dataguardians.datasynth;

import com.dataguardians.exceptions.HttpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dataguardians.openai.GenerativeAPI;
import com.dataguardians.openai.api.chat.Response;
import com.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import com.dataguardians.security.TokenProvider;

public class ShortTextGenerator extends DataGenerator<String> {
    public ShortTextGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config) {
        super(token, generator, config);
    }

    protected String generateInput() {
        return "Write me a random short paragraph.";
    }

    public String generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024)
                .build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }

}
