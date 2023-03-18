package org.dataguardians.datasynth.schema;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
@Builder
public class Schema {
    private String schemaDefinition;

    public static class SchemaBuilder {
        public SchemaBuilder from(File schemaFile) throws IOException {
            this.schemaDefinition = IOUtils.toString(schemaFile.toURI(), StandardCharsets.UTF_8);
            return this;
        }

        public SchemaBuilder from(String schema) throws IOException {
            this.schemaDefinition = schema;
            return this;
        }
    }
}
