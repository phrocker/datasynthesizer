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

    /**
     * Generates a String input value for CommentGenerator.
     * The implementation of the actual input string is left to the subclasses.
     *
     * @return a String value representing the input for CommentGenerator.
     * @see CommentGenerator
     * @since 1.0
     */
    protected String generateInput() {
        return "";
    }

    /**
     * Generates a string representation of comment data in JSON format.
     * This method may throw an HttpException or JsonProcessingException if there are errors
     * while generating the comment data or converting it to JSON, respectively.
     *
     * @return a JSON string representing comment data
     * @throws HttpException if there are errors while generating the comment data
     * @throws JsonProcessingException if there are errors while converting the data to JSON
     */
    public String generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }

    /**
     * Generate a method from the given class name and method signature.
     *
     * @param className the name of the class containing the method
     * @param methodSig the method signature, in the format "methodName(paramType1, paramType2, ...)"
     * @return a string representing the method declaration, in the format
     *         "public returnType methodName(paramType1 paramName1, paramType2 paramName2, ...)"
     *
     * @throws IllegalArgumentException if the className or methodSig is invalid
     */
    private String generateMethodFromSignature(String className, String methodSig) {
        return "Please generate a javadoc for a method " + methodSig + " that is in a class named " + className + ". Please only generate the method javadoc";
    }

    /**
     * Generates a comment for a given method with the specified class name and method signature.
     * Returns the generated comment as a string.
     *
     * @param className the name of the class containing the method
     * @param methodSignature the signature of the method in the format "methodName(paramType1 arg1, paramType2 arg2, ...)"
     * @return the generated comment as a string
     * @throws HttpException if an error occurs during HTTP communication
     * @throws JsonProcessingException if an error occurs during JSON processing
     */
    public String generate(String className, String methodSignature) throws HttpException, JsonProcessingException {
        String req = generateMethodFromSignature(className, methodSignature);
        System.out.println(req);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(req).maxTokens(1024).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }
}
