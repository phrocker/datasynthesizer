package org.dataguardians.datasynth.query.compliance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

public class QueryComplianceScorer extends DataGenerator<Double> {

    private final QueryComplianceConfiguration complianceConfig;

    public QueryComplianceScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config, QueryComplianceConfiguration complianceConfig) {
        super(token, generator, config);
        this.complianceConfig = complianceConfig;
    }


    /**
     * Generates input for the generative AI endpoint.
     *
     * @return Question to be asked to the generative AI endpoint.
     */
    @Override
    protected String generateInput() {
        String queryStr = "Assuming the following rules for queries:";

        for(var rule : complianceConfig.getRules())
            queryStr += rule +",";

        queryStr +=      ". Can you give me a confidence score from 0 to 1 on whether the following query is non-compliant: " + complianceConfig.getQuery() + ";  Please only provide the score. ";


        return queryStr;
    }

/**
     * Parses queries from the response.
     *
     * @return List of queries.
     */
    @Override
    public Double generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024).build();
        Response hello = api.sample(request, Response.class);
        return Double.valueOf(hello.concatenateResponses());
    }
}