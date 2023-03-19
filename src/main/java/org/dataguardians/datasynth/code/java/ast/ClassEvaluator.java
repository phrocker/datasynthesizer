package org.dataguardians.datasynth.code.java.ast;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassEvaluator<T,J>{
    @Builder.Default
    List<Function<MethodDeclaration,T>> methodFunctions = new ArrayList<>();

    @Builder.Default
    List<T> methodReturns = new ArrayList<>();

    @Builder.Default
    List<Function<ClassOrInterfaceDeclaration,J>> classFunctions = new ArrayList<>();

    @Builder.Default
    List<J> classReturns = new ArrayList<>();

    @Builder.Default
    List<ClassType> types = new ArrayList<>();

    public void evaluate(MethodDeclaration declr){
        methodFunctions.forEach( methodFunction -> methodReturns.add( methodFunction.apply(declr) ));
    }

    public void evaluate(ClassOrInterfaceDeclaration declr){
        classFunctions.forEach( classFunction -> classReturns.add( classFunction.apply(declr) ));
    }

    public void evaluate(FieldDeclaration declr){
        declr.getVariables().forEach( x ->{
            types.add(ClassType.builder().variableName(x.getName().toString()).variableType(x.getType().toString()).build());
        });
    }
}
