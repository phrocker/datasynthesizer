package org.dataguardians.datasynth.code.comment;

import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The CommentGeneratorMain class provides methods for generating comments.
 * This class contains a main method that takes an array of strings as an argument.
 * The main method is responsible for calling the other methods in the class to generate comments.
 */
public class CommentGeneratorMain {

    private static TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    /**
     * The main method of the CommentGeneratorMain class.
     *
     * @param args
     *            an array of command-line arguments for the main method.
     *
     * @throws HttpException
     *             if an HTTP exception occurs during execution
     *
     * @throws IOException
     *             if an I/O exception occurs during execution
     */
    public static void main(String[] args) throws HttpException, IOException {
        String filename = args[0];
        JavaFileVisitor jfr = new JavaFileVisitor(0.5);
        Files.walkFileTree(Path.of(filename), jfr);
        final GenerativeAPI chatGPT = new GenerativeAPI(provider);
        final CommentGenerator gen = new CommentGenerator(provider, chatGPT, new GeneratorConfiguration());
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
