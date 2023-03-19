package org.dataguardians.datasynth.code.test;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.dataguardians.datasynth.code.java.ast.ClassEvaluator;
import org.dataguardians.datasynth.code.java.ast.ClassType;
import org.dataguardians.datasynth.code.java.JavaDocParser;
import org.dataguardians.datasynth.code.java.ast.MethodType;
import org.dataguardians.exceptions.HttpException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenerateUnitTest {

    public GenerateUnitTest(){

    }

    // evaluate the members of the class to produce a context



    // evaluate the methods of the class with the arguments


    // use generative AI to generate inputs and expected outputs from a method


    public static void main(String [] args) throws HttpException, IOException {
        String filename = args[0];

        List<Function<ClassOrInterfaceDeclaration,String>> classFunctions = new ArrayList<>();
        classFunctions.add((ClassOrInterfaceDeclaration declr) -> {
            return declr.getName().toString();

        });

        List<Function<MethodDeclaration, MethodType>> methodFunctions = new ArrayList<>();
        methodFunctions.add((MethodDeclaration declr) -> {
            List<ClassType> arguments = new ArrayList<>();
            declr.getParameters().forEach((parameter) -> {
                arguments.add(ClassType.builder().variableName(parameter.getNameAsString()).variableType(parameter.getTypeAsString()).build());
            });
            return MethodType.builder().methodReturnType(declr.getTypeAsString()).methodName(declr.getNameAsString()).methodArguments( arguments).build();

        });

        ClassEvaluator<MethodType,String> classEval = ClassEvaluator.<MethodType,String>builder().methodFunctions(methodFunctions).classFunctions(classFunctions).build();
        JavaDocParser.visit(filename,classEval);

        System.out.println(classEval.getTypes());

        System.out.println(classEval.getMethodReturns());

        System.out.println(classEval.getClassReturns());
    }


}
