package org.dataguardians.datasynth.code.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.dataguardians.exceptions.HttpException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;

@Data
@Builder
@AllArgsConstructor
public class JavaFileVisitor extends SimpleFileVisitor<Path> {


    private final Double minCommentThreshold;

    List<String> filesToEvaluate = new ArrayList<>();
    public JavaFileVisitor(Double minCommentThreshold){
        this.minCommentThreshold=minCommentThreshold;
    }
    @Override
    public FileVisitResult visitFile(Path file,
                                     BasicFileAttributes attr) {
        if (attr.isRegularFile()) {
            if (file.toString().endsWith(".java")) {
                try {
                    boolean ifMeetsCriteria = JavaDocParser.javaDocsMeetCriteria(file.toAbsolutePath().toString(), true, true, minCommentThreshold);
                    if (!ifMeetsCriteria) {
                        filesToEvaluate.add(file.toString());
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
    public FileVisitResult postVisitDirectory(Path dir,
                                              IOException exc) {
        return CONTINUE;
    }

    // If there is some error accessing
    // the file, let the user know.
    // If you don't override this method
    // and an error occurs, an IOException
    // is thrown.
    @Override
    public FileVisitResult visitFileFailed(Path file,
                                           IOException exc) {
        return CONTINUE;
    }

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        JavaFileVisitor visitor = new JavaFileVisitor(0.5);
        Files.walkFileTree(Path.of(filename), visitor);
        System.out.println(visitor.filesToEvaluate);
    }
}