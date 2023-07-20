package com.dataguardians.datasynth.code.test;

import com.dataguardians.exceptions.HttpException;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.dataguardians.datasynth.code.java.ast.ClassEvaluator;
import com.dataguardians.datasynth.code.java.ast.ClassType;
import com.dataguardians.datasynth.code.java.JavaDocParser;
import com.dataguardians.datasynth.code.java.ast.MethodType;

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
            StringBuilder impl = new StringBuilder();
            declr.getTokenRange().ifPresent(x -> impl.append(x.toString()));
            List<ClassType> arguments = new ArrayList<>();
            declr.getParameters().forEach((parameter) -> {
                arguments.add(ClassType.builder().variableName(parameter.getNameAsString()).variableType(parameter.getTypeAsString()).build());
            });

            return MethodType.builder().methodReturnType(declr.getTypeAsString()).methodName(declr.getNameAsString()).methodArguments( arguments).accessModifier(declr.getAccessSpecifier().toString()).impl(impl.toString()).build();

        });

        ClassEvaluator<MethodType,String> classEval = ClassEvaluator.<MethodType,String>builder().methodFunctions(methodFunctions).classFunctions(classFunctions).build();

        JavaDocParser.visit(filename,classEval);

        System.out.println(classEval.getTypes());

        System.out.println(classEval.getMethodReturns());

        System.out.println(classEval.getClassReturns());

        classEval.getMethodReturns().stream().filter(method -> method.getAccessModifier().equals("PUBLIC") ).forEach((method) -> {
            System.out.println( TestGenerator.generateUnitTestRequest(classEval.getClassReturns().get(0),method) );
        });

        System.out.println( TestGenerator.generateUnitTestRequest2(classEval.getClassReturns().get(0),classEval.getMethodReturns()));

        /**
         * Example that can be sent to OpenAI. Generates the most reasonable test. 
         */

        System.out.println( TestGenerator.generateUnitTestRequest3(classEval.getClassReturns().get(0),classEval.getMethodReturns()) );






    }


}
