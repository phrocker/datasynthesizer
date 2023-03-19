package org.dataguardians.datasynth.code.java.ast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodType {
    private String methodName;
    private String methodReturnType;

    @Builder.Default
    private List<ClassType> methodArguments = new ArrayList<>();
}
