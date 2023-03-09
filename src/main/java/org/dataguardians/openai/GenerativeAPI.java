package org.dataguardians.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.endpoints.ApiEndPointRequest;
import org.dataguardians.security.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * <p>
 * Generative API calls for OpenAI. Intended to be extensible to other endpoints.
 * </p>
 *
 * original @author <a href="https://github.com/LiLittleCat">LiLittleCat</a>
 * @since 2023/3/2
 */
@Slf4j
public  class GenerativeAPI {
    protected final TokenProvider authToken;
    protected OkHttpClient client;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public GenerativeAPI(TokenProvider authToken) {
        this.authToken = authToken;
        this.client = new OkHttpClient();
    }

    public GenerativeAPI(TokenProvider authToken, OkHttpClient client) {
        this.authToken = authToken;
        this.client = client;
    }

    public GenerativeAPI(TokenProvider authToken, Proxy proxy) {
        this.authToken = authToken;
        client = new OkHttpClient.Builder().proxy(proxy).build();
    }

    public GenerativeAPI(TokenProvider authToken, String proxyHost, int proxyPort) {
        this.authToken = authToken;
        client = new OkHttpClient.Builder().
                proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)))
                .build();
    }



    private String buildRequestBody(final ApiEndPointRequest request) {
        try {
            return objectMapper.writeValueAsString(request.create());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ask for response message
     *
     * @param apiRequest Api Request object
     * @return ChatCompletionResponseBody
     */
    public String sample(final ApiEndPointRequest apiRequest) throws HttpException{
        RequestBody body = RequestBody.create(buildRequestBody(apiRequest), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(apiRequest.getEndpoint())
                .header("Authorization", "Bearer " + authToken.getToken())
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    log.error("Request failed: {}, please try again", response.message());
                    throw new HttpException(response.code(), "Request failed");
                } else {
                    log.error("Request failed: {}, please try again", response.body().string());
                    throw new HttpException(response.code(), response.body().string());
                }
            } else {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("Request failed: {}", e.getMessage());
            throw new HttpException(500, e.getMessage());
        }
    }

    public <T> T sample(final ApiEndPointRequest apiRequest, Class<T> clazz) throws HttpException, JsonProcessingException {
        return (T) objectMapper.readValue(sample(apiRequest), clazz);
    }


}
