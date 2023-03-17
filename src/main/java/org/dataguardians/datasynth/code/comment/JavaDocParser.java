package org.dataguardians.datasynth.code.comment;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import org.dataguardians.exceptions.HttpException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JavaDocParser {




    public static String parseJavaDocFrom(String className, String code) throws IOException {
        // JavaParser has a minimal logging class that normally logs nothing.
        // Let's ask it to write to standard out:
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());

        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));

        File tempFile = File.createTempFile("created-", "-java");
        tempFile.deleteOnExit();

        StringBuilder sb = new StringBuilder();
        sb.append("public class " + className + " {");
        sb.append(code);
        sb.append("}");

        Files.write(tempFile.toPath(), sb.toString().getBytes());


        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", tempFile.getAbsolutePath());

        final List<String> commentsAdded = new ArrayList<>();
        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                System.out.println("I'm here " + n.getDeclarationAsString(true, true, true));

                if (n.getComment().isPresent()){
                    commentsAdded.add(n.getComment().get().getContent());
                    System.out.println("I'm here " + n.getComment().get().getContent());
                }
                else{
                    System.out.println("nothing for " + n.getDeclarationAsString(true, true, true));
                }

                return ret;

            }
        }, null);

        tempFile.delete();
        return commentsAdded.get(0);
    }


    public static void main(String [] args) throws IOException {
        Path filePath = Path.of("e:/sample-java.txt");

        String content = Files.readString(filePath);

        System.out.println(parseJavaDocFrom("myClassName", content));
    }
}