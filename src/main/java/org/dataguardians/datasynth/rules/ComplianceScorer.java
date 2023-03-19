package org.dataguardians.datasynth.rules;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;

/**
 * The ComplianceScorer class contains methods for generating compliance scores.
 * The generate() method returns a double that represents the compliance score.
 *
 * This class can be used to efficiently score compliance in various domains, including but not limited to
 * healthcare, finance, and government regulations.
 *
 * It is recommended to initialize the ComplianceScorer with relevant settings and parameters for a specific
 * compliance scenario. The generate() method can then be called repeatedly on incoming data to obtain
 * compliance scores in real-time.
 *
 * Note: This class does not handle data storage, retrieval or manipulation. It is only intended for
 * calculating compliance scores based on input data.
 */
public abstract class ComplianceScorer extends DataGenerator<Double> {

    protected ComplianceConfiguration complianceConfig;

    public ComplianceScorer(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config, ComplianceConfiguration complianceConfig) {
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
