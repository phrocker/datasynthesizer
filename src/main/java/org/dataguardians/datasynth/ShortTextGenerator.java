package org.dataguardians.datasynth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

public class ShortTextGenerator extends DataGenerator<String>{
    public ShortTextGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config) {
        super(token, generator, config);
    }

    protected String generateInput(){
        return "Write me a random short paragraph.";
    }


    public String generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024).build();
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }




}
