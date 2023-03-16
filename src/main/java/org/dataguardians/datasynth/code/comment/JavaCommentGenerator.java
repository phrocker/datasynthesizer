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

import javax.xml.stream.events.Comment;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaCommentGenerator {


    private CommentGenerator generator;
    private String fileOrDirectory;

    private boolean saveToFile;

    public JavaCommentGenerator(CommentGenerator generator, String  fileOrDirectory, boolean saveToFile){
        this.generator= generator;
        this.fileOrDirectory = fileOrDirectory;
        this.saveToFile = saveToFile;
    }


    public List<String> generate(){
        // JavaParser has a minimal logging class that normally logs nothing.
        // Let's ask it to write to standard out:
        Log.setAdapter(new Log.StandardOutStandardErrorAdapter());

        // SourceRoot is a tool that read and writes Java files from packages on a certain root directory.
        // In this case the root directory is found by taking the root from the current Maven module,
        // with src/main/resources appended.
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(JavaCommentGenerator.class).resolve("src/main/java"));

        // Our sample is in the root of this directory, so no package name.
        CompilationUnit cu = sourceRoot.parse("", fileOrDirectory);

        String className = cu.getPrimaryTypeName().get();
        final List<String> commentsAdded = new ArrayList<>();
        cu.accept(new ModifierVisitor<Void>() {
            @Override
            public Visitable visit(final MethodDeclaration n, final Void arg) {
                var ret = super.visit(n, arg);
                System.out.println("I'm here " + n.getDeclarationAsString(true, true, true));
                if (n.getComment().isPresent()){
                    System.out.println("I'm here " + n.getComment().get().getContent());
                }
                else{
                    String comment = null;
                    try {
                        comment = generator.generate(className,n.getDeclarationAsString(true, true, true));
                        n.setComment(new JavadocComment(comment));
                        commentsAdded.add(comment);
                    } catch (HttpException e) {
                        throw new RuntimeException(e);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }

                }

                return ret;

            }
        }, null);

        sourceRoot.saveAll();
        return commentsAdded;
    }


}
