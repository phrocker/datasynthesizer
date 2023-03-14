package org.dataguardians.datasynth.code.quality;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.dataguardians.datasynth.query.compliance.QueryComplianceConfiguration;
import org.dataguardians.datasynth.query.compliance.QueryComplianceScorer;
import org.dataguardians.datasynth.rules.ComplianceRule;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.security.ApiKey;
import org.dataguardians.security.TokenProvider;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CodeQualityTest {

    private TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();
    //@Test
    public void test() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);

        List<ComplianceRule> rules = new ArrayList<>();
        rules.add(ComplianceRule.builder().rule("Descriptive variable names").build());
        rules.add(ComplianceRule.builder().rule("comments must exist").build());
        rules.add(ComplianceRule.builder().rule("Don't repeat code within a module").build());
        var codeQuality = CodeQualityConfiguration.builder().rules(rules);
        codeQuality = codeQuality.codeUrl("https://github.com/phrocker/datasynthesizer/blob/main/src/main/java/org/dataguardians/datasynth/SchemaSynthesizer.java");
        CodeQualityScorer generator = new CodeQualityScorer(provider, chatGPT, null, codeQuality.build());
        System.out.println(generator.generate());// response will be 0.8
    }

    @Test
    public void testPass(){
    }
}
