package org.dataguardians.datasynth.code.java;

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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * This class implements the java.nio.file.FileVisitor interface and provides methods to traverse the directory tree and
 * perform operations on the files and directories. The class contains the following methods:
 *
 * - visitFile(Path path, BasicFileAttributes attrs): Invoked for a file in a directory. - postVisitDirectory(Path dir,
 * IOException exc): Invoked for a directory after all entries have been visited. - visitFileFailed(Path path,
 * IOException exc): Invoked when a file cannot be visited. - main(String[] args): The entry point for the application.
 *
 * Usage: 1. Create an instance of JavaFileVisitor. 2. Pass the root directory path to the instance. 3. Call the
 * walkFileTree() method on the root directory.
 *
 * The methods in this class allow for customization of the behavior while traversing the directory tree.
 *
 * @author Marc Parisi
 *
 * @version 1.0
 *
 * @since 3/18/2023
 */
@Data
@Builder
@AllArgsConstructor
@Slf4j
public class JavaFileVisitor extends SimpleFileVisitor<Path> {



    List<String> filesToEvaluate = new ArrayList<>();

    final Function<String,Boolean> suitabilityFunction;

    public JavaFileVisitor(Function<String,Boolean> suitabilityFunction) {
        this.suitabilityFunction=suitabilityFunction;
    }


    /**
     * Invoked for a file visitation when a file is visited.
     *
     * @param file
     *            the path representing the file being visited
     * @param attr
     *            the basic attributes of the file
     *
     * @return the file visit result indicating the status of the file visitation
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (attr.isRegularFile()) {
            if (file.toString().endsWith(".java")) {
                if (suitabilityFunction.apply(file.toAbsolutePath().toString())) {
                    filesToEvaluate.add(file.toAbsolutePath().toString());
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
     * This method is the entry point of the JavaFileVisitor class. It takes a String array containing the command line
     * arguments and throws an IOException. It will start the execution of the program and as it is a static method, it
     * can be called without creating an object of the JavaFileVisitor class.
     *
     * @param args
     *            an array of command-line arguments for the program
     *
     * @throws IOException
     *             if any input/output exception occurs during the execution of the program
     */
    public static void main(String[] args) throws IOException {
        String filename = args[0];
        JavaFileVisitor visitor = new JavaFileVisitor( x -> {return true;} );
        Files.walkFileTree(Path.of(filename), visitor);
        System.out.println(visitor.filesToEvaluate);
    }
}
