package org.dataguardians.datasynth.query.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dataguardians.openai.api.chat.Message;

import java.util.List;

/**
 * Query configuration requestor for simple queries
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryConfiguration {

    private List<DataDictionaryDefinition> dataDictionary;

    private QueryType queryType;

    private int count;

    @Data
    @Builder
    public static class DataDictionaryDefinition {

        public String fieldName;

        public FieldType type;
    }
}
