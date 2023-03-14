package org.dataguardians.datasynth.rules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
/**
 * Generic compliance rule for a process to follow ( code quality, query compliance, security, etc ).
 */
public class ComplianceRule {
    String rule;
}
