package com.dataguardians.openai.security;

import com.dataguardians.security.ApiKey;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class ApiKeyTest {

    @Test
    public void testApiKeyCreation() {
        ApiKey key = ApiKey.builder().build();
        Assert.assertEquals("user", key.getPrincipal());
        Assert.assertNull(key.getToken());
        key = ApiKey.builder().principal("notnull").build();
        Assert.assertEquals("notnull", key.getPrincipal());
        Assert.assertNull(key.getToken());
    }
}
