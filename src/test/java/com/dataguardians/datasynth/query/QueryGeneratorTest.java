package com.dataguardians.datasynth.query;

import com.dataguardians.datasynth.query.entity.FieldType;
import com.dataguardians.datasynth.query.entity.QueryConfiguration;
import com.dataguardians.datasynth.query.entity.QueryType;
import com.dataguardians.exceptions.HttpException;
import com.dataguardians.security.ApiKey;
import com.dataguardians.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import com.dataguardians.openai.GenerativeAPI;
import com.dataguardians.openai.api.chat.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QueryGeneratorTest {

    private TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    // @Test
    public void test() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        List<QueryConfiguration.DataDictionaryDefinition> dataDictionary = new ArrayList<>();
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carType")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carColor")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carModel")
                .type(FieldType.FUZZY).build());
        final QueryConfiguration queryConfig = QueryConfiguration.builder().count(2).queryType(QueryType.SQL)
                .dataDictionary(dataDictionary).build();
        QueryGenerator generator = new QueryGenerator(provider, chatGPT, null, queryConfig);
        System.out.println(generator.generate());
    }

    // @Test
    public void testJexl() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        List<QueryConfiguration.DataDictionaryDefinition> dataDictionary = new ArrayList<>();
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carType")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carColor")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carModel")
                .type(FieldType.FUZZY).build());
        final QueryConfiguration queryConfig = QueryConfiguration.builder().count(2).queryType(QueryType.JEXL)
                .dataDictionary(dataDictionary).build();
        QueryGenerator generator = new QueryGenerator(provider, chatGPT, null, queryConfig);
        System.out.println(generator.generate());
    }

    // @Test
    public void testSQLWithInvalid() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        List<QueryConfiguration.DataDictionaryDefinition> dataDictionary = new ArrayList<>();
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carType")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carColor")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carModel")
                .type(FieldType.FUZZY).build());
        final QueryConfiguration queryConfig = QueryConfiguration.builder().count(2).queryType(QueryType.SQL)
                .dataDictionary(dataDictionary).invalidOnly(true).build();
        QueryGenerator generator = new QueryGenerator(provider, chatGPT, null, queryConfig);
        System.out.println(generator.generate());
    }

    // @Test
    public void testLuceneInvalid() throws HttpException, JsonProcessingException {
        GenerativeAPI chatGPT = new GenerativeAPI(provider);
        List<QueryConfiguration.DataDictionaryDefinition> dataDictionary = new ArrayList<>();
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carType")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carColor")
                .type(FieldType.EXACT).build());
        dataDictionary.add(QueryConfiguration.DataDictionaryDefinition.builder().fieldName("carModel")
                .type(FieldType.FUZZY).build());
        final QueryConfiguration queryConfig = QueryConfiguration.builder().count(2).queryType(QueryType.LUCENE)
                .dataDictionary(dataDictionary).invalidOnly(true).build();
        QueryGenerator generator = new QueryGenerator(provider, chatGPT, null, queryConfig);
        System.out.println(generator.generate());
    }

    @Test
    public void testParseResponse() throws IOException, HttpException {

        final String jsonText = IOUtils.toString(this.getClass().getResourceAsStream("/ChatGPTResponse2.json"),
                "UTF-8");
        List<String> responses = new ArrayList<>();
        responses.add("(carType == \"Sedan\") && (carColor == \"Blue\") && (carModel ~= \"Camry\")");
        responses.add("(carType == \"Truck\") && (carColor == \"Black\") && (carModel ~= \"F-150\")");
        Response resp = new ObjectMapper().readValue(jsonText, Response.class);

        QueryGenerator.parseQueries(resp.concatenateResponses()).forEach(responses::remove);
        assert responses.isEmpty();
    }

    @Test
    public void testParseEmptyResponse() throws IOException, HttpException {

        final String jsonText = IOUtils.toString(this.getClass().getResourceAsStream("/ChatGPTResponseEmpty.json"),
                "UTF-8");

        Response resp = new ObjectMapper().readValue(jsonText, Response.class);

        assert resp.concatenateResponses().isEmpty();
        assert QueryGenerator.parseQueries(resp.concatenateResponses()).isEmpty();
    }
}
