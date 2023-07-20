package com.dataguardians.datasynth.compliance;

import com.dataguardians.datasynth.query.compliance.QueryComplianceConfiguration;
import com.dataguardians.datasynth.query.compliance.QueryComplianceScorer;
import com.dataguardians.datasynth.rules.ComplianceRule;
import com.dataguardians.exceptions.HttpException;
import com.dataguardians.security.ApiKey;
import com.dataguardians.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.dataguardians.openai.GenerativeAPI;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QueryComplianceTest {

    private TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    // @Test
    public void test() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);

        List<ComplianceRule> rules = new ArrayList<>();
        rules.add(ComplianceRule.builder().rule("queries must contain at least one concrete term or bounded range")
                .build());
        var complianceConfigBuilder = QueryComplianceConfiguration.builder().rules(rules);
        complianceConfigBuilder = complianceConfigBuilder.query("select * from users where age > 18");
        QueryComplianceScorer generator = new QueryComplianceScorer(provider, chatGPT, null,
                complianceConfigBuilder.build());
        System.out.println(generator.generate());
    }

    @Test
    public void testPass() {
    }
}
