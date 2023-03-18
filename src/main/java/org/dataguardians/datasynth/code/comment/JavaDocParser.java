package org.dataguardians.datasynth.code.comment;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.Log;
import com.github.javaparser.utils.SourceRoot;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.dataguardians.exceptions.HttpException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class JavaDocParser {




    public static String parseJavaDocFrom(String className, String code) throws IOException {

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

                if (n.getComment().isPresent()){
                    commentsAdded.add(n.getComment().get().getContent());
                }

                return ret;

            }
        }, null);

        tempFile.delete();
        return commentsAdded.isEmpty() ? "" : commentsAdded.get(0);
    }


    public static void main(String [] args) throws IOException {
        Path filePath = Path.of("e:/sample-java.txt");

        String content = Files.readString(filePath);

        System.out.println(parseJavaDocFrom("myClassName", content));
    }

    public static String parseClassJavaDoc(String className, String generateClassJavaDoc) throws IOException {

        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));

        File tempFile = File.createTempFile("created-", "-java");
        tempFile.deleteOnExit();

        StringBuilder sb = new StringBuilder();
        sb.append(generateClassJavaDoc);
        sb.append("public class " + className + " {");
        sb.append("}");

        Files.write(tempFile.toPath(), sb.toString().getBytes());


        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", tempFile.getAbsolutePath());

        final StringBuilder comment = new StringBuilder();
        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                if (n.getComment().isPresent()){
                    comment.append(n.getComment().get().getContent());
                }
                return ret;

            }
        }, null);



        tempFile.delete();
        return comment.toString();
    }

    public static boolean javaDocsMeetCriteria (String filePath, boolean mustHaveClassDoc, boolean meetThreshold, Double threshold) throws IOException, HttpException {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));


        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", filePath);

        final StringBuilder comment = new StringBuilder();
        List<String> methods = new ArrayList<>();
        List<String> methodsWithDoc = new ArrayList<>();
        MutableBoolean hasClassDoc = new MutableBoolean(false);
        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                methods.add(n.getNameAsString());
                if (n.getComment().isPresent()){
                    methodsWithDoc.add(n.getNameAsString());
                }
                return ret;

            }
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);

                if (n.getComment().isPresent()){
                    hasClassDoc.setTrue();
                }

                return ret;

            }
        }, null);

        if (mustHaveClassDoc && !hasClassDoc.booleanValue()){
            return false;
        }
        if (meetThreshold){
            double percentage = (double) methodsWithDoc.size() / methods.size();
            return percentage >= threshold;
        }
        return true;
    }
}
