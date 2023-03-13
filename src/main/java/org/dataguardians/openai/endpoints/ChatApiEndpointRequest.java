package org.dataguardians.openai.endpoints;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.dataguardians.openai.api.chat.ChatRequest;
import org.dataguardians.openai.api.chat.Message;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
public class ChatApiEndpointRequest extends ApiEndPointRequest {

    public static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    @Override
    public String getEndpoint() {
        return API_ENDPOINT;
    }

    @Override
    public Object create() {
        List<Message> messages = new ArrayList<>();
        String role = StringUtils.isBlank(user) ? "user" : user;
        messages.add(Message.builder().role(role).content(input).build());
        var requestBody = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .user(role)
//                .maxTokens(maxTokens)
                .messages(messages);
        if (maxTokens != 4096){
            requestBody.maxTokens(maxTokens);
        }
        return requestBody.build();
    }

}
