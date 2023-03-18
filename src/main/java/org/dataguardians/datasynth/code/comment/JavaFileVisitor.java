package org.dataguardians.datasynth.code.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dataguardians.exceptions.HttpException;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * This class implements the java.nio.file.FileVisitor interface and provides methods to traverse the directory tree
 * and perform operations on the files and directories. The class contains the following methods:
 *
 * - visitFile(Path path, BasicFileAttributes attrs): Invoked for a file in a directory.
 * - postVisitDirectory(Path dir, IOException exc): Invoked for a directory after all entries have been visited.
 * - visitFileFailed(Path path, IOException exc): Invoked when a file cannot be visited.
 * - main(String[] args): The entry point for the application.
 *
 * Usage:
 * 1. Create an instance of JavaFileVisitor.
 * 2. Pass the root directory path to the instance.
 * 3. Call the walkFileTree() method on the root directory.
 *
 * The methods in this class allow for customization of the behavior while traversing the directory tree.
 *
 * @author Your Name
 * @version 1.0
 * @since date
 */
@Data
@Builder
@AllArgsConstructor
@Slf4j
public class JavaFileVisitor extends SimpleFileVisitor<Path> {

    private final Double minCommentThreshold;

    List<String> filesToEvaluate = new ArrayList<>();

    public JavaFileVisitor(Double minCommentThreshold) {
        this.minCommentThreshold = minCommentThreshold;
    }

    private boolean isClass(String file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Stream<String> linesStream = bufferedReader.lines();
        return !linesStream.filter(a -> a.contains("public class") || a.contains("final class")).collect(Collectors.toList()).isEmpty();
    }
    /**
     * Invoked for a file visitation when a file is visited.
     *
     * @param file the path representing the file being visited
     * @param attr the basic attributes of the file
     * @return the file visit result indicating the status of the file visitation
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (attr.isRegularFile()) {
            if (file.toString().endsWith(".java")) {
                try {
                    boolean ifMeetsCriteria = JavaDocParser.javaDocsMeetCriteria(file.toAbsolutePath().toString(), true, true, minCommentThreshold);
                    if (!ifMeetsCriteria && isClass(file.toString())) {
                        log.info("Generating comments for file: " + file.toString());
                        filesToEvaluate.add(file.toString());
                    } else {
                        log.info("Skipping comments for file: " + file.toString());
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (HttpException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return CONTINUE;
    }

    /**
     * This method is the entry point of the JavaFileVisitor class. It takes a String array containing the command
     * line arguments and throws an IOException. It will start the execution of the program and as it is a static
     * method, it can be called without creating an object of the JavaFileVisitor class.
     *
     * @param args an array of command-line arguments for the program
     * @throws IOException if any input/output exception occurs during the execution of the program
     */
    public static void main(String[] args) throws IOException {
        String filename = args[0];
        JavaFileVisitor visitor = new JavaFileVisitor(0.5);
        Files.walkFileTree(Path.of(filename), visitor);
        System.out.println(visitor.filesToEvaluate);
    }
}
