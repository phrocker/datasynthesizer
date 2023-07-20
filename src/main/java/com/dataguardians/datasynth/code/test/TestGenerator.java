package com.dataguardians.datasynth.code.test;

import com.dataguardians.exceptions.HttpException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import com.dataguardians.datasynth.DataGenerator;
import com.dataguardians.datasynth.GeneratorConfiguration;
import com.dataguardians.datasynth.code.java.ast.MethodType;
import com.dataguardians.openai.GenerativeAPI;
import com.dataguardians.openai.api.chat.Response;
import com.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import com.dataguardians.security.TokenProvider;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that generates comments for code snippets.
 *
 * This class provides methods for generating input strings, method signatures,
 * and java docs for classes and methods. It also generates source code
 * snippets with comments.
 *
 * @author Marc Parisi
 * @version 0.1
 * @since 3/17/2021
 */
@Slf4j
public class TestGenerator extends DataGenerator<String> {

    public TestGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config) {
        super(token, generator, config);
    }

    /**
     * Generates a String input value for CommentGenerator. The implementation of the actual input string is left to the
     * subclasses.
     *
     * @return a String value representing the input for CommentGenerator.
     *
     * @see TestGenerator
     *
     * @since 1.0
     */
    protected String generateInput() {
        return "";
    }

    /**
     * Generates a string representation of comment data in JSON format. This method may throw an HttpException or
     * JsonProcessingException if there are errors while generating the comment data or converting it to JSON,
     * respectively.
     *
     * @return a JSON string representing comment data
     *
     * @throws HttpException
     *             if there are errors while generating the comment data
     * @throws JsonProcessingException
     *             if there are errors while converting the data to JSON
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
     * @param className
     *            the name of the class containing the method
     * @param method
     *            the method signature, in the format "methodName(paramType1, paramType2, ...)"
     *
     * @return a string representing the method declaration, in the format "public returnType methodName(paramType1
     *         paramName1, paramType2 paramName2, ...)"
     *
     * @throws IllegalArgumentException
     *             if the className or methodSig is invalid
     */
    public static String generateUnitTestRequest(String className, MethodType method) {
        String request = "Generate unit tests for a method " + method.getMethodName() + " that is in a class named " + className + ". ";
        request += "The method ha the following return type: " + method.getMethodReturnType() + ". ";
        if (!method.getMethodArguments().isEmpty())
            request += "The method has the following arguments: " + method.getMethodArguments().stream().map(arg -> arg.getVariableName() + " of type " + arg.getVariableType() + ", ").collect(Collectors.joining()) + ". ";

        return request + "Please only generate the method unit test";
    }

    /**
     * Generate a method from the given class name and method signature.
     *
     * @param className
     *            the name of the class containing the method
     * @param method
     *            the method signature, in the format "methodName(paramType1, paramType2, ...)"
     *
     * @return a string representing the method declaration, in the format "public returnType methodName(paramType1
     *         paramName1, paramType2 paramName2, ...)"
     *
     * @throws IllegalArgumentException
     *             if the className or methodSig is invalid
     */
    public static String generateUnitTestRequest2(String className, List<MethodType> methods) {
        String request = "Generate unit tests for a class named " + className + " that contains the following public methods: ";

        StringBuilder methodSigs = new StringBuilder();
        methods.stream().filter(mthd -> mthd.getAccessModifier().equals("PUBLIC")).forEach(method -> {
            methodSigs.append("A method " + method.getMethodName() + " with return type: " + method.getMethodReturnType() + ". ");
            if (!method.getMethodArguments().isEmpty())
                methodSigs.append(method.getMethodName() + " has the following arguments: " + method.getMethodArguments().stream().map(arg -> arg.getVariableName() + " of type " + arg.getVariableType() + ", ").collect(Collectors.joining()) + ". ");
            else{
                methodSigs.append(method.getMethodName() + " has no arguments. ");
            }

        });

        return request + methodSigs + " Please only generate the method unit test and make no additional assumptions. ";
    }

    public static String generateUnitTestRequest3(String className, List<MethodType> methods) {
        String request = "Generate unit tests for a class named " + className + " that contains the following public methods: ";

        StringBuilder methodSigs = new StringBuilder();
        methods.stream().filter(mthd -> mthd.getAccessModifier().equals("PUBLIC")).forEach(method -> {
            methodSigs.append("A method " + method.getMethodName() + " with return type: " + method.getMethodReturnType() + ". ");
            if (!method.getMethodArguments().isEmpty())
                methodSigs.append(method.getMethodName() + " has the following arguments: " + method.getMethodArguments().stream().map(arg -> arg.getVariableName() + " of type " + arg.getVariableType() + ", ").collect(Collectors.joining()) + ". ");
            else{
                methodSigs.append(method.getMethodName() + " has no arguments. ");
            }
            methodSigs.append("here is the code implementation of " + method.getMethodName() + ": " + method.getImpl() );

        });

        return request + methodSigs + " Please only generate the method unit test and make no additional assumptions";
    }



    /**
     * Generates a comment for a given method with the specified class name and method signature. Returns the generated
     * comment as a string.
     *
     * @param className
     *            the name of the class containing the method
     * @param method
     *            the signature of the method in the format "methodName(paramType1 arg1, paramType2 arg2, ...)"
     *
     * @return the generated comment as a string
     *
     * @throws HttpException
     *             if an error occurs during HTTP communication
     * @throws JsonProcessingException
     *             if an error occurs during JSON processing
     */
    public String generateUnitTest(String className, MethodType method) throws HttpException, JsonProcessingException {
        final String req = generateUnitTestRequest(className, method);
        log.debug("Making request for {} ", req);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(req).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }

}
