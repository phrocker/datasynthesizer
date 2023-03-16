package org.dataguardians.datasynth.rules;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Compliance configuration object that contains a set of compliance rules.
 * This object is used to configure the compliance scorer(s).
 */
public class ComplianceConfiguration {

    List<ComplianceRule> rules;

}
