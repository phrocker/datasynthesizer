package org.dataguardians.datasynth.query.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dataguardians.openai.api.chat.Message;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Query configuration requestor for simple queries
 */
public class QueryConfiguration {

    private List<DataDictionaryDefinition> dataDictionary;

    private QueryType queryType;

    private int count;

    private boolean invalidOnly;

    @Data
    @Builder
    /**
     * Data dictionary for the query configuration object.
     */
    public static class DataDictionaryDefinition {

        public String fieldName;

        public FieldType type;
    }
}
