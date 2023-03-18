package org.dataguardians.datasynth.code.comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;
import org.dataguardians.exceptions.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a comment generator for Java code. It includes methods for visiting method declarations
 * and class/interface declarations, and a method for generating the comments as a list of strings.
 *
 * @author [Marc Parisi]
 * @version 1.0
 * @since [3/18/2023]
 */
public class JavaCommentGenerator {

    private final boolean replaceComments;

    private CommentGenerator generator;

    private String fileOrDirectory;

    private boolean saveToFile;

    public JavaCommentGenerator(CommentGenerator generator, String fileOrDirectory, boolean saveToFile) {
        this(generator, fileOrDirectory, saveToFile, false);
    }

    public JavaCommentGenerator(CommentGenerator generator, String fileOrDirectory, boolean saveToFile, boolean replaceComments) {
        this.generator = generator;
        this.fileOrDirectory = fileOrDirectory;
        this.saveToFile = saveToFile;
        this.replaceComments = replaceComments;
    }

    /**
     * Generates a list of strings using the specified configuration.
     * This method may throw HttpException or IOException when attempting to retrieve the configuration.
     *
     * @return A List of Strings generated using the specified configuration.
     * @throws HttpException If there is a problem with the HTTP connection during configuration retrieval.
     * @throws IOException If an IO error occurs while retrieving the configuration.
     */
    public List<String> generate() throws HttpException, IOException {
        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaCommentGenerator.class).resolve("src/main/java"));
        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", fileOrDirectory);
        String className = cu.getPrimaryTypeName().get();
        final List<String> commentsAdded = new ArrayList<>();
        final List<String> methods = new ArrayList<>();
        cu.accept(new ModifierVisitor<Void>() {

            /**
             * Visits a MethodDeclaration node in the Abstract Syntax Tree (AST)
             * and generates JavaDoc comments for the method.
             *
             * @param n the MethodDeclaration node in the AST to be visited
             * @param arg unused argument of type Void
             * @return a Visitable object representing the AST node
             */
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                methods.add(n.getDeclarationAsString(false, false, false));
                if (!n.getComment().isPresent() || replaceComments) {
                    String comment = null;
                    try {
                        final String methodDeclr = n.getDeclarationAsString(true, true, true);
                        comment = generator.generateMethodJavaDoc(className, methodDeclr);
                        n.setComment(new JavadocComment(JavaDocParser.parseJavaDocFrom(className, comment)));
                        commentsAdded.add(comment);
                    } catch (HttpException e) {
                        throw new RuntimeException(e);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return ret;
            }
        }, null);
        var classJavaDoc = new JavadocComment(JavaDocParser.parseClassJavaDoc(className, generator.generateClassJavaDoc(className, methods)));
        cu.accept(new ModifierVisitor<Void>() {

            /**
             */
            @Override
            public Visitable visit(final ClassOrInterfaceDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                n.setComment(classJavaDoc);
                return ret;
            }
        }, null);
        sourceRoot.saveAll();
        return commentsAdded;
    }
}
