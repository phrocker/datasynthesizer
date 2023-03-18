package org.dataguardians.openai.security;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.dataguardians.security.ApiKey;

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
