package org.dataguardians.datasynth.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.dataguardians.datasynth.DataGenerator;
import org.dataguardians.datasynth.GeneratorConfiguration;
import org.dataguardians.datasynth.query.entity.QueryConfiguration;
import org.dataguardians.datasynth.query.entity.QueryType;
import org.dataguardians.exceptions.HttpException;
import org.dataguardians.openai.GenerativeAPI;
import org.dataguardians.openai.api.chat.Response;
import org.dataguardians.openai.endpoints.ChatApiEndpointRequest;
import org.dataguardians.security.TokenProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The QueryGenerator class is responsible for generating inputs and queries for a database. It provides three methods:
 * generateInput(), parseQueries(String), and generate().
 *
 * The generateInput() method generates a string input for a database. It randomly generates a list of values and
 * returns them as a string.
 *
 * The parseQueries(String) method takes a string input and parses it into a list of queries. The input should be in a
 * specific format: each query should be on a new line, and have its parameters separated by commas. The method returns
 * a list of strings, where each string is a query with its parameters separated by spaces.
 *
 * The generate() method generates a list of random queries with random parameters. It returns a list of queries as
 * strings.
 *
 * @author Marc Parisi
 *
 * @version 1.0
 */
@Slf4j
public class QueryGenerator extends DataGenerator<List<String>> {

    private final QueryConfiguration queryConfig;

    public QueryGenerator(TokenProvider token, GenerativeAPI generator, GeneratorConfiguration config,
            QueryConfiguration queryConfig) {
        super(token, generator, config);
        this.queryConfig = queryConfig;
    }

    /**
     * Generates input for the generative AI endpoint.
     *
     * @return Question to be asked to the generative AI endpoint.
     */
    @Override
    protected String generateInput() {
        String queryStr = queryConfig.getCount() > 1 ? "queries" : "query";
        String query = "Generate " + queryConfig.getCount() + " random ";
        if (queryConfig.getQueryType() == QueryType.SQL)
            query += "select ";
        query += queryConfig.getQueryType().name() + " " + queryStr + " for the following data dictionary: ";
        final StringBuilder queries = new StringBuilder();
        queryConfig.getDataDictionary().forEach(dd -> {
            if (queries.length() > 0)
                queries.append(", ");
            queries.append(dd.getFieldName() + " of type " + dd.getType().name());
        });
        query += " " + queries + ". Please don't include explanations or comments in the " + queryStr;
        if (queryConfig.isInvalidOnly())
            query += " and make them with invalid syntax structure";
        query += ".";
        return query;
    }

    /**
     * Parses queries from the response.
     *
     * @param response
     *            query response from the AI endpoint.
     *
     * @return
     */
    static List<String> parseQueries(final String response) {
        final String[] lines = response.split("\\r?\\n");
        final List<String> queries = new ArrayList<>();
        for (String line : lines) {
            // should be #. query. So we need to extract the query
            // no need for a regex.
            int idx = line.indexOf(". ");
            if (idx >= 1) {
                queries.add(line.substring(idx + 2));
            } else if (idx == 0) {
                // this would be an odd case. We should not have a line starting with a dot.
                log.error("Parsing failed: index 0 on {} ", line);
            }
        }
        return queries;
    }

    /**
     * Generates a list of Strings by HTTP request and JSON processing.
     *
     * @return the list of Strings generated
     *
     * @throws HttpException
     *             if there is a problem with the HTTP request
     * @throws JsonProcessingException
     *             if there is a problem processing the JSON
     */
    @Override
    public List<String> generate() throws HttpException, JsonProcessingException {
        ChatApiEndpointRequest request = ChatApiEndpointRequest.builder().input(generateInput()).maxTokens(1024)
                .build();
        Response hello = api.sample(request, Response.class);
        return parseQueries(hello.concatenateResponses());
    }
}
