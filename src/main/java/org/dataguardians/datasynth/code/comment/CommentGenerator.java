package org.dataguardians.datasynth.code.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;
import java.util.List;

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
public class CommentGenerator extends DataGenerator<String> {

    public CommentGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config) {
        super(token, generator, config);
    }

    /**
     * Generates a String input value for CommentGenerator. The implementation of the actual input string is left to the
     * subclasses.
     *
     * @return a String value representing the input for CommentGenerator.
     *
     * @see CommentGenerator
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
     * @param methodSig
     *            the method signature, in the format "methodName(paramType1, paramType2, ...)"
     *
     * @return a string representing the method declaration, in the format "public returnType methodName(paramType1
     *         paramName1, paramType2 paramName2, ...)"
     *
     * @throws IllegalArgumentException
     *             if the className or methodSig is invalid
     */
    private String generateMethodFromSignature(String className, String methodSig) {
        return "Generate a javadoc for a method " + methodSig + " that is in a class named " + className + ". Please only generate the method javadoc";
    }

    /**
     * Generates a class signature using the provided class name and list of methods. The generated signature includes
     * the class name, implemented interfaces, and public method signatures.
     *
     * @param className
     *            The name of the class to generate the signature for.
     * @param methods
     *            A list of method signatures to include in the signature.
     *
     * @return A string representing the generated class signature.
     */
    private String generateClassSignature(String className, List<String> methods) {
        return "Generate a class comment for " + className + " that has the following methods:" + StringUtils.join(methods, ", ") + ". Please only generate the class javadoc";
    }

    /**
     * Generates a comment for a given method with the specified class name and method signature. Returns the generated
     * comment as a string.
     *
     * @param className
     *            the name of the class containing the method
     * @param methodSignature
     *            the signature of the method in the format "methodName(paramType1 arg1, paramType2 arg2, ...)"
     *
     * @return the generated comment as a string
     *
     * @throws HttpException
     *             if an error occurs during HTTP communication
     * @throws JsonProcessingException
     *             if an error occurs during JSON processing
     */
    public String generateMethodJavaDoc(String className, String methodSignature) throws HttpException, JsonProcessingException {
        final String req = generateMethodFromSignature(className, methodSignature);
        log.debug("Making request for {} ", req);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(req).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }

    /**
     * Generates the JavaDoc for a given class and list of method signatures.
     *
     * @param className
     *            The name of the class for which JavaDoc is to be generated.
     * @param methodSignatures
     *            The list of method signatures for which JavaDoc is to be generated.
     *
     * @return The generated JavaDoc.
     *
     * @throws HttpException
     *             If there is an HTTP exception during the generation process.
     * @throws JsonProcessingException
     *             If there is a JSON processing exception during the generation process.
     */
    public String generateClassJavaDoc(String className, List<String> methodSignatures) throws HttpException, JsonProcessingException {
        final String req = generateClassSignature(className, methodSignatures);
        log.debug("Making request for {} ", req);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(req).build();
        request.setMaxTokens(config.getMaxTokens());
        Response hello = api.sample(request, Response.class);
        return hello.concatenateResponses();
    }
}
