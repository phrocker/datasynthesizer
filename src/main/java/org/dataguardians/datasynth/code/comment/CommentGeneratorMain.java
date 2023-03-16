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
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;

public class CommentGeneratorMain {

    private static TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();



    public static void main(String[] args) {

        String filename = args[0];
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        CommentGenerator gen = new CommentGenerator(provider,chatGPT, new GeneratorConfiguration());
        JavaCommentGenerator generator = new JavaCommentGenerator(gen, filename, true);
        generator.generate();
        // This saves all the files we just read to an output directory.
    }
}
