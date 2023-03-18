package org.dataguardians.datasynth.query.compliance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.datasynth.rules.ComplianceScorer;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

/**
 * Query compliance scorer with user defined rules to be provided to OpenAI
 */
public class QueryComplianceScorer extends ComplianceScorer {


    public QueryComplianceScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config, QueryComplianceConfiguration complianceConfig) {
        super(token, generator, config,complianceConfig);
    }


    /**
     * Generates input for the generative AI endpoint.
     *
     * @return Question to be asked to the generative AI endpoint.
     */
    @Override
    protected String generateInput() {
        String queryStr = "Assuming the following rules for queries:";

        QueryComplianceConfiguration queryComplianceConfiguration = (QueryComplianceConfiguration) complianceConfig;
        for(var rule : queryComplianceConfiguration.getRules())
            queryStr += rule.getRule() +",";

        queryStr +=      ". Can you give me a confidence score from 0 to 1 on whether the following query is non-compliant: " + queryComplianceConfiguration.getQuery() + ";  Please only provide the score. ";


        return queryStr;
    }

}