package org.dataguardians.datasynth.code.quality;

import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.datasynth.query.compliance.QueryComplianceConfiguration;
import org.dataguardians.datasynth.rules.ComplianceScorer;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.TokenProvider;

public class CodeQualityScorer  extends ComplianceScorer {


    public CodeQualityScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config, CodeQualityConfiguration complianceConfig) {
        super(token, generator, config,complianceConfig);
    }


    /**
     * Generates input for the generative AI endpoint.
     *
     * @return Question to be asked to the generative AI endpoint.
     */
    @Override
    protected String generateInput() {
        String queryStr = "Assuming the following rules for code quality:";

        CodeQualityConfiguration codeConfig = (CodeQualityConfiguration) complianceConfig;
        for(var rule : codeConfig.getRules())
            queryStr += rule.getRule() +",";

        queryStr +=      ". Provide a confidence score from 0 to 1 on whether the following code url is compliant with said rules: " + codeConfig.getCodeUrl() + ";  Please only provide the score. ";

        System.out.println(queryStr);
        return queryStr;
    }

}
