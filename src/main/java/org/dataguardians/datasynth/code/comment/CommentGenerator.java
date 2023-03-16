package org.dataguardians.datasynth.code.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

public class CommentGenerator extends DataGenerator<String> {
    public CommentGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config) {
        super(token, generator, config);
    }

    protected String generateInput(){


        return "";
    }


    public String generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }

    private String generateMethodFromSignature(String className, String methodSig){
        return "Please generate a javadoc for a method " + methodSig + " that is a class named " + className;

    }

    public String generate(String className, String methodSignature) throws HttpException, JsonProcessingException {
        String req = generateMethodFromSignature(className,methodSignature);
        System.out.println(req);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(req).maxTokens(1024).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }




}
