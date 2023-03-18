package org.dataguardians.datasynth.code.comment;

import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;

import java.io.IOException;

public class CommentGeneratorMain {

    private static TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();



    public static void main(String[] args) throws HttpException, IOException {

        String filename = args[0];
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        CommentGenerator gen = new CommentGenerator(provider,chatGPT, new GeneratorConfiguration());
        JavaCommentGenerator generator = new JavaCommentGenerator(gen, filename, true);
        generator.generate();
        // This saves all the files we just read to an output directory.
    }
}
