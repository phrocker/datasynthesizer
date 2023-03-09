package org.dataguardians.security;

public class ApiKey implements TokenProvider {

    private final String apiKey;
    private String principal = "user";


    public ApiKey(String apiKey){
        this.apiKey=apiKey;
    }
    public ApiKey(final String principal, final String apiKey){
        this.principal=principal;
        this.apiKey=apiKey;
    }
    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getToken() {
        return apiKey;
    }
}
