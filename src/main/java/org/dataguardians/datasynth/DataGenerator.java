package org.dataguardians.datasynth;

import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.TokenProvider;

public class DataGenerator {

    private final TokenProvider token;
    private final GenerativeAPI api;

    private final GeneratorConfiguration config;

    public DataGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config){
        this.token=token;
        this.api=generator;
        this.config =config;
    }





}
