package org.dataguardians.datasynth.rules;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

public abstract class ComplianceScorer extends DataGenerator<Double> {

    protected ComplianceConfiguration complianceConfig;

    public ComplianceScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config,
            ComplianceConfiguration complianceConfig) {
        super(token, generator, config);
        this.complianceConfig = complianceConfig;
    }

    /**
     * Parses queries from the response.
     *
     * @return List of queries.
     */
    @Override
    public Double generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).build();
        request.setTemperature(0.5f);
        Response hello = api.sample(request, Response.class);
        return Double.valueOf(hello.concatenateResponses());
    }
}
