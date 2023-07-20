package com.dataguardians.datasynth.query.compliance;

import com.dataguardians.datasynth.rules.ComplianceConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * This class represents a QueryComplianceConfiguration feature that provides methods for retrieving the current
 * compliance configuration settings.
 *
 * The methods available in this class include:
 *
 * - getComplianceConfiguration(): Get the current compliance configuration settings.
 *
 * - setComplianceConfiguration(ComplianceConfig config): Set the compliance configuration settings for the current
 * query. This method takes a single argument of type ComplianceConfig, which represents the desired settings to be
 * applied to the query.
 *
 * By default, the compliance configuration settings are set to the system defaults. Use the
 * setComplianceConfiguration() method to configure specific settings for your query. The getComplianceConfiguration()
 * method can be called to return the currently configured settings.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class /**
              * Query compliance configuration object that extends the compliance configuration.
              */
QueryComplianceConfiguration extends ComplianceConfiguration {

    String query;
}
