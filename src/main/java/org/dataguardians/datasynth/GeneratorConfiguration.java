package org.dataguardians.datasynth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Generator configuration object.
 */
public class GeneratorConfiguration {

    @Builder.Default
    protected int concurrentGenerations = 1;

    @Builder.Default
    protected int maxTokens = 4096;
}
