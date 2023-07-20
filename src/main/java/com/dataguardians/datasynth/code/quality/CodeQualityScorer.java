package com.dataguardians.datasynth.code.quality;

import com.dataguardians.datasynth.rules.ComplianceScorer;
import com.dataguardians.datasynth.GeneratorConfiguration;
import com.dataguardians.openai.GenerativeAPI;
import com.dataguardians.security.TokenProvider;

/**
 * Attempts to generate a code quality confidence score.
 *
 * The confidence reflects adherence to the rules generated in <code>CodeQualityConfiguration complianceConfig</code>.
 */
public class CodeQualityScorer extends ComplianceScorer {

    public CodeQualityScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config,
            CodeQualityConfiguration complianceConfig) {
        super(token, generator, config, complianceConfig);
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
        for (var rule : codeConfig.getRules())
            queryStr += rule.getRule() + ",";

        queryStr += ". Provide a confidence score from 0 to 1 on whether the following code url is compliant with said rules: "
                + codeConfig.getCodeUrl()
                + ";  Please only provide the score. Ignore the license at the beginning of the file. ";

        System.out.println(queryStr);
        return queryStr;
    }

}
