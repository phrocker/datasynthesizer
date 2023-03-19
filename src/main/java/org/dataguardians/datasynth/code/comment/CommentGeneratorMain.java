package org.dataguardians.datasynth.code.comment;

import lombok.extern.slf4j.Slf4j;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.datasynth.code.java.JavaDocParser;
import org.dataguardians.datasynth.code.java.JavaFileVisitor;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The CommentGeneratorMain class provides methods for generating comments. This class contains a main method that takes
 * an array of strings as an argument. The main method is responsible for calling the other methods in the class to
 * generate comments.
 */
@Slf4j
public class CommentGeneratorMain {

    private static TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    private static boolean isClass(String file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> linesStream = bufferedReader.lines();
        return !linesStream.filter(a -> a.contains("public class") || a.contains("final class") || a.contains("abstract class"))
                .collect(Collectors.toList()).isEmpty();
    }

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
        String author = args[1];
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

        JavaFileVisitor jfr = new JavaFileVisitor((String file)-> {
            try {
                // the java doc meeting criteria function evaluates if we have a class header comment block
                // and at least 50% of the methods have a comment block. If we have both, we skip the file
                boolean ifMeetsCriteria = JavaDocParser.javaDocsMeetCriteria(file, true,
                        true, 0.5);
                if (!ifMeetsCriteria && isClass(file.toString())) {
                    log.info("Generating comments for file: " + file.toString());
                    return true;
                } else {
                    log.info("Skipping comments for file: " + file.toString());
                    return false;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (HttpException e) {
                throw new RuntimeException(e);
            }

        });
        Files.walkFileTree(Path.of(filename), jfr);
        log.info("Walking file tree {}", filename);
        final GenerativeAPI chatGPT = new GenerativeAPI(provider);
        final CommentGenerator gen = new CommentGenerator(provider, chatGPT, new GeneratorConfiguration());
        jfr.getFilesToEvaluate().forEach(requestedFilePath -> {
            log.info("Generating comments for file: {}", requestedFilePath);
            JavaCommentGenerator generator = new JavaCommentGenerator(gen, requestedFilePath,true, author,date);
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
