package com.dataguardians.openai;

import com.dataguardians.exceptions.HttpException;
import com.dataguardians.openai.api.chat.Response;
import com.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import com.dataguardians.security.ApiKey;
import com.dataguardians.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

public class GenerativeAITest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    private TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    // @Test
    public void test() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();
        Response hello = chatGPT.sample(request, Response.class);
        System.out.println(hello.concatenateResponses());
    }

    @Test
    public void testMockResponseNoBody() throws IOException, HttpException {

        exceptionRule.expect(HttpException.class);
        exceptionRule.expectMessage("Response Code: 500 occurred, with exception");

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(provider, mockClient));
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();

        Request req = mock(Request.class);
        when(chatGPT.buildRequestBody(request)).thenReturn(new ObjectMapper().writeValueAsString(request.create()));
        okhttp3.Response resp = mock(okhttp3.Response.class);
        Call httpCall = mock(Call.class);
        when(httpCall.execute()).thenReturn(resp);
        when(resp.code()).thenReturn(500);
        when(mockClient.newCall(any())).thenReturn(httpCall);
        Response hello = chatGPT.sample(request, Response.class);

    }

    @Test
    public void testMockResponseWithBody() throws IOException, HttpException {

        final String jsonText = IOUtils.toString(this.getClass().getResourceAsStream("/ChatGPTResponse1.json"),
                "UTF-8");

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(provider, mockClient));
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();

        Request req = mock(Request.class);
        when(chatGPT.buildRequestBody(request)).thenReturn(new ObjectMapper().writeValueAsString(request.create()));
        okhttp3.Response resp = mock(okhttp3.Response.class);
        Call httpCall = mock(Call.class);
        when(httpCall.execute()).thenReturn(resp);
        when(resp.code()).thenReturn(200);
        when(resp.isSuccessful()).thenReturn(true);
        when(resp.body()).thenReturn(okhttp3.ResponseBody.create(jsonText, MediaType.get("application/json")));

        when(mockClient.newCall(any())).thenReturn(httpCall);
        Response hello = chatGPT.sample(request, Response.class);
        Assert.assertTrue(hello.concatenateResponses().contains(
                "I'm an AI language model, so I don't have feelings, but I'm here to assist you. How can I help you today"));
    }

    // TODO: add test for other response codes

    // add a test similar to testMockResponseWithBody with other response codes

    @Test
    public void testIncorrectKeyUsed() throws IOException, HttpException {

        final String jsonText = IOUtils.toString(this.getClass().getResourceAsStream("/ChatGPTResponse1.json"),
                "UTF-8");

        exceptionRule.expect(HttpException.class);
        exceptionRule.expectMessage("Response Code: 401 occurred, with exception");

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(provider, mockClient));
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();

        Request req = mock(Request.class);
        when(chatGPT.buildRequestBody(request)).thenReturn(new ObjectMapper().writeValueAsString(request.create()));
        okhttp3.Response resp = mock(okhttp3.Response.class);
        Call httpCall = mock(Call.class);
        when(httpCall.execute()).thenReturn(resp);
        when(resp.code()).thenReturn(401);
        when(resp.body()).thenReturn(okhttp3.ResponseBody.create(jsonText, MediaType.get("application/json")));

        when(mockClient.newCall(any())).thenReturn(httpCall);
        Response hello = chatGPT.sample(request, Response.class);
    }

    @Test
    public void testNullConstructor() throws IOException, HttpException {

        exceptionRule.expect(NullPointerException.class);

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(null));
    }

    @Test
    public void testNullConstructor2() throws IOException, HttpException {

        exceptionRule.expect(NullPointerException.class);

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(null, null));
    }

    @Test
    public void testNullApiRequest() throws IOException, HttpException {

        exceptionRule.expect(NullPointerException.class);

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(null, null));
        chatGPT.sample(null);
    }

    @Test
    public void testInvalidApiRequest() throws IOException, HttpException {

        exceptionRule.expect(NullPointerException.class);

        OkHttpClient mockClient = mock(OkHttpClient.class);
        GenerativeAPI chatGPT = spy(new GenerativeAPI(provider, mockClient));
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().build();
        chatGPT.sample(request);
    }

}
