package org.dataguardians.datasynth.query.compliance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryComplianceConfiguration {

    List<ComplianceRule> rules;

    String query;

}
