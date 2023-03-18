package org.dataguardians.datasynth.code.comment;

import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CommentGeneratorMain {

    private static TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();


    /**
     * replace  * @author [Your Name]
     *  * @version 1.0
     *  * @since [date of creation]
     * @param args
     * @throws HttpException
     * @throws IOException
     */
    public static void main(String[] args) throws HttpException, IOException {

        String filename = args[0];
        JavaFileVisitor jfr = new JavaFileVisitor(0.5);

        Files.walkFileTree(Path.of(filename), jfr);
        final GenerativeAPI chatGPT = new GenerativeAPI(provider);
        final CommentGenerator gen = new CommentGenerator(provider,chatGPT, new GeneratorConfiguration());
        jfr.getFilesToEvaluate().forEach(requestedFilePath -> {
            JavaCommentGenerator generator = new JavaCommentGenerator(gen, requestedFilePath, true);
            try {
                generator.generate();
            } catch (HttpException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // This saves all the files we just read to an output directory.
    }
}
