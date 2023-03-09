package org.dataguardians.datasynth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
import org.junit.Test;


public class DataGeneratorTest {

    private TokenProvider provider = new ApiKey("YOUR-APK-KEY");
    @Test
    public void test() throws HttpException, JsonProcessingException {
            GenerativeAPI chatGPT = new GenerativeAPI(provider);
            DataGenerator generator = new DataGenerator(provider, chatGPT, null);
            System.out.println(generator.generate());
        }
}
