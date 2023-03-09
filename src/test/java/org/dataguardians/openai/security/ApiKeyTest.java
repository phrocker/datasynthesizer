package org.dataguardians.openai.security;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.dataguardians.security.ApiKey;

public class ApiKeyTest {

    @Test
    public void testApiKeyCreation(){
        ApiKey key = new ApiKey(null);
        Assert.assertEquals("user",key.getPrincipal());
        Assert.assertNull(key.getToken());
        key = new ApiKey("notnull",null);
        Assert.assertEquals("notnull",key.getPrincipal());
        Assert.assertNull(key.getToken());
    }
}
