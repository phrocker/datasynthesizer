package com.dataguardians.datasynth.code.java;

import com.dataguardians.datasynth.code.java.ast.ClassEvaluator;
import com.dataguardians.exceptions.HttpException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * The JavaDocParser class is responsible for parsing and analyzing Java Doc comments for both
 * classes/interfaces and methods as well as checking if they meet certain criteria.
 * It has the ability to visit ClassOrInterfaceDeclaration and MethodDeclaration nodes in
 * the Abstract Syntax Tree (AST) and parse the associated Java Doc comments. The class also
 * provides methods to parse a JavaDoc comment from a given String and retrieve a class's
 * JavaDoc comment. Additionally, the class contains a main method to demonstrate the functionality
 * of the class. The class includes a boolean method that determines if a given JavaDoc comment meets
 * the criteria of having a minimum length, containing certain keywords and meeting a minimum "readability"
 * score specified by a Double value.
 */
@Slf4j
public class JavaDocParser {

    /**
     * Parses the javadoc comments from a given code snippet for a specified class name.
     *
     * @param className
     *            the name of the class to search for in the code snippet
     * @param code
     *            the code snippet to search for javadoc comments
     *
     * @return a string of the parsed javadoc comments, or an empty string if no comments found
     *
     * @throws IOException
     *             if there is an error reading the code snippet
     */
    public static String parseJavaDocFrom(String className, String code) throws IOException {
        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));
        File tempFile = File.createTempFile("created-", "-java");
        tempFile.deleteOnExit();
        StringBuilder sb = new StringBuilder();
        sb.append("public class " + className + " {");
        code = code.replaceAll("```", "");
        sb.append(code);
        sb.append("}");
        Files.write(tempFile.toPath(), sb.toString().getBytes());
        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", tempFile.getAbsolutePath());
        final List<String> commentsAdded = new ArrayList<>();
        cu.accept(new ModifierVisitor<Void>() {

            /**
             * Visits the given MethodDeclaration node and returns a Visitable object.
             *
             * @param n
             *            the MethodDeclaration node to be visited
             * @param arg
             *            an optional argument to be passed to the visitor
             *
             * @return a Visitable object representing the visited node
             */
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                if (n.getComment().isPresent()) {
                    commentsAdded.add(n.getComment().get().getContent());
                }
                return ret;
            }
        }, null);
        tempFile.delete();
        return commentsAdded.isEmpty() ? "" : commentsAdded.get(0);
    }

    /**
     * The main method of the JavaDocParser class.
     *
     * This method is the entry point for the JavaDocParser program. It takes an array of arguments as input, which are
     * passed in from the command line. This method is responsible for parsing the specified JavaDoc file and extracting
     * information about methods, classes, and other entities documented in the file.
     *
     * @param args
     *            An array of command line arguments.
     *
     * @throws IOException
     *             If an I/O error occurs while reading the JavaDoc file.
     */
    public static void main(String[] args) throws IOException {
        Path filePath = Path.of("e:/sample-java.txt");
        String content = Files.readString(filePath);
        System.out.println(parseJavaDocFrom("myClassName", content));
    }

    /**
     * Parses the Javadoc of a given class and returns it as a String.
     *
     * @param className
     *            the name of the class to parse
     * @param generateClassJavaDoc
     *            the Javadoc of the class to generate
     *
     * @return a String representing the Javadoc of the class
     *
     * @throws IOException
     *             if an error occurs while reading the Javadoc file
     */
    public static String parseClassJavaDoc(String className, String generateClassJavaDoc) throws IOException {
        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));
        File tempFile = File.createTempFile("created-", "-java");
        tempFile.deleteOnExit();
        StringBuilder sb = new StringBuilder();
        generateClassJavaDoc = generateClassJavaDoc.replaceAll("```", "");
        sb.append(generateClassJavaDoc);
        sb.append("public class " + className + " {");
        sb.append("}");
        Files.write(tempFile.toPath(), sb.toString().getBytes());
        // Our sample is in the root of this directory, so no package name.
        try {
            CompilationUnit cu = sourceRoot.parse("", tempFile.getAbsolutePath());
            final StringBuilder comment = new StringBuilder();
            cu.accept(new ModifierVisitor<Void>() {

                /**
                 * Visits a ClassOrInterfaceDeclaration node in the syntax tree of a Java source file.
                 *
                 * @param n
                 *            the node to visit
                 * @param arg
                 *            an optional argument for the visitor (unused in this implementation)
                 *
                 * @return a Visitable object representing the result of the visit
                 *
                 * @throws IllegalStateException
                 *             if the node is null
                 */
                @Override
                public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                    var ret = super.visit(n, arg);
                    if (n.getComment().isPresent()) {
                        comment.append(n.getComment().get().getContent());
                    }
                    return ret;
                }
            }, null);
            tempFile.delete();
            return comment.toString();
        } catch (ParseProblemException e) {
            throw e;
        }
    }

    /**
     * Analyzes the Javadoc comments in the specified file path and returns whether or not they meet certain criteria.
     *
     * @param filePath
     *            The path of the file to analyze.
     * @param mustHaveClassDoc
     *            Determines whether or not the file must contain Javadoc for its class.
     * @param meetThreshold
     *            Determines whether or not the average quality score of the Javadoc comments must meet a certain
     *            threshold.
     * @param threshold
     *            The minimum threshold score for the Javadoc comments. Must be a value between 0 and 1.
     *
     * @return Whether or not the Javadoc comments meet the specified criteria.
     *
     * @throws IOException
     *             If an error occurs while reading the file.
     * @throws HttpException
     *             If an error occurs while trying to retrieve additional information.
     */
    public static boolean javaDocsMeetCriteria(String filePath, boolean mustHaveClassDoc, boolean meetThreshold, Double threshold) throws IOException, HttpException {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));
        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", filePath);
        final StringBuilder comment = new StringBuilder();
        List<String> methods = new ArrayList<>();
        List<String> methodsWithDoc = new ArrayList<>();
        MutableBoolean hasClassDoc = new MutableBoolean(false);
        cu.accept(new ModifierVisitor<Void>() {

            /**
             * Visits a ClassOrInterfaceDeclaration node in the AST.
             *
             * @param n
             *            the ClassOrInterfaceDeclaration node to visit
             * @param arg
             *            additional argument to pass to the visitor
             *
             * @return the result of visiting the node, or null if none
             *
             * @throws NullPointerException
             *             if the ClassOrInterfaceDeclaration node is null
             */
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                methods.add(n.getNameAsString());
                if (n.getComment().isPresent()) {
                    methodsWithDoc.add(n.getNameAsString());
                }
                return ret;
            }

            /**
             * Visits a MethodDeclaration node and returns a Visitable object.
             *
             * @param n
             *            The MethodDeclaration node to visit.
             * @param arg
             *            A Void argument. This argument is ignored.
             *
             * @return A Visitable object representing the visited MethodDeclaration node.
             */
            @Override
            public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                if (n.getComment().isPresent()) {
                    hasClassDoc.setTrue();
                }
                return ret;
            }
        }, null);
        if (mustHaveClassDoc && !hasClassDoc.booleanValue()) {
            log.info("{} does not have class documentation ", filePath);
            return false;
        } else {
            log.info("{} has class documentation ", filePath);
        }
        if (meetThreshold && !methods.isEmpty()) {
            double percentage = (double) methodsWithDoc.size() / methods.size();
            log.debug("{}} has a percentage of {}, with a desired threshold of {} ", filePath, percentage, threshold);
            return percentage >= threshold;
        }
        return true;
    }

    protected static CompilationUnit visit(
            String filePath) throws IOException, HttpException {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaDocParser.class).resolve("src/main/java"));
        CompilationUnit cu = sourceRoot.parse("", filePath);
        return cu;
    }

    public static <T,J> CompilationUnit visit(
            String filePath,
            ClassEvaluator<T,J> evaluator) throws IOException, HttpException {
        var cu = visit(filePath);
        cu.accept(new ModifierVisitor<Void>() {

            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);

                evaluator.evaluate(n);

                return ret;
            }

            @Override
            public Visitable visit(final FieldDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);

                evaluator.evaluate(n);

                return ret;
            }

            @Override
            public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);

                evaluator.evaluate(n);

                return ret;
            }
        }, null);
        return cu;
    }
}
