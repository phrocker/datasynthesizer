package org.dataguardians.datasynth.query.compliance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dataguardians.datasynth.rules.ComplianceConfiguration;
import org.dataguardians.datasynth.rules.ComplianceRule;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Query compliance configuration object that extends the compliance configuration.
 */
public class QueryComplianceConfiguration extends ComplianceConfiguration {


    String query;

}
