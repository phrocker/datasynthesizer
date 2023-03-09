package org.dataguardians.datasynth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

public class DataGenerator {

    private final TokenProvider token;
    private final GenerativeAPI api;

    private final GeneratorConfiguration config;

    public DataGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config){
        this.token=token;
        this.api=generator;
        this.config =config;
    }




    public String generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Write me a random short paragraph.").maxTokens(1024).build();
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }




}
