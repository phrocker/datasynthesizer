package org.dataguardians.datasynth.compliance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.rules.ComplianceRule;
import org.dataguardians.datasynth.query.compliance.QueryComplianceConfiguration;
import org.dataguardians.datasynth.query.compliance.QueryComplianceScorer;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
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
