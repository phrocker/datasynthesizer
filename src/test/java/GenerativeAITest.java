import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
import org.junit.jupiter.api.Test;

public class GenerativeAITest {


    TokenProvider provider = new ApiKey("API-KEY");
    @Test
    void test() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input("Hello, how are you today?").build();
        Response hello = chatGPT.sample(request, Response.class);
        System.out.println(hello.concatenateResponses());
    }


    // TODO: powermock tests
}
