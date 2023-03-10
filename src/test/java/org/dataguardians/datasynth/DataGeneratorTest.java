package org.dataguardians.datasynth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
import org.junit.Test;


public class DataGeneratorTest {

    private TokenProvider provider = new ApiKey(System.getenv("OPENAI_API_KEY"));

    @Test
    public void test() throws HttpException, JsonProcessingException {
            GenerativeAPI chatGPT = new GenerativeAPI(provider);
        ShortTextGenerator generator = new ShortTextGenerator(provider, chatGPT, null);
            System.out.println(generator.generate());
        }
}
