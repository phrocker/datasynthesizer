package org.dataguardians.datasynth.code.quality;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dataguardians.datasynth.rules.ComplianceConfiguration;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class CodeQualityConfiguration extends ComplianceConfiguration {

    String codeUrl;
    @Builder.Default
    int maxMethodLength=-1;
}
