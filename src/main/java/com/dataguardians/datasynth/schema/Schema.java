package com.dataguardians.datasynth.schema;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * The Schema class provides a convenient way to validate and deserialize data
 * against a schema. It provides two methods to create a SchemaBuilder object:
 * one that takes a File object representing the schema file, and one that takes
 * a String object representing the schema in JSON format. The resulting SchemaBuilder
 * object can then be used to customize the validation and deserialization process for
 * data objects.
 *
 * Example:
 *
 * Schema schema = Schema.from(new File("schema.json"));
 * // or
 * Schema schema = Schema.from("{\\\"type\\\":\\\"object\\\",\\\"properties\\\":{}}");
 *
 * SchemaBuilder builder = schema.builder();
 * builder.addOptionalProperty("name", Schema.STRING_SCHEMA);
 * builder.addRequiredProperty("age", Schema.INT32_SCHEMA);
 * builder.setDefaultValue("name", "John Doe");
 *
 * // Use the builder to create a schema for validating and deserializing data
 * Schema userSchema = builder.build();
 */
@Data
@Builder
public class Schema {

    private String schemaDefinition;

    /**
     * The Schema class provides a convenient way to validate and deserialize data
     * against a schema. It provides two methods to create a SchemaBuilder object:
     * one that takes a File object representing the schema file, and one that takes
     * a String object representing the schema in JSON format. The resulting SchemaBuilder
     * object can then be used to customize the validation and deserialization process for
     * data objects.
     *
     * Example:
     *
     * Schema schema = Schema.from(new File("schema.json"));
     * // or
     * Schema schema = Schema.from("{\\\"type\\\":\\\"object\\\",\\\"properties\\\":{}}");
     *
     * SchemaBuilder builder = schema.builder();
     * builder.addOptionalProperty("name", Schema.STRING_SCHEMA);
     * builder.addRequiredProperty("age", Schema.INT32_SCHEMA);
     * builder.setDefaultValue("name", "John Doe");
     *
     * // Use the builder to create a schema for validating and deserializing data
     * Schema userSchema = builder.build();
     */
    public static class SchemaBuilder {

        /**
         * Creates a new SchemaBuilder instance using the specified schema file.
         *
         * @param schemaFile the schema file to use for building the schema
         * @return a new instance of SchemaBuilder initialized with the specified schema file
         * @throws IOException if there was an error reading or accessing the schema file
         */
        public SchemaBuilder from(File schemaFile) throws IOException {
            this.schemaDefinition = IOUtils.toString(schemaFile.toURI(), StandardCharsets.UTF_8);
            return this;
        }

        /**
         * Creates a new SchemaBuilder instance from the specified schema string.
         *
         * @param schema the schema string to build the Schema instance from
         * @return a new SchemaBuilder instance
         * @throws IOException if an error occurs while reading the schema string
         */
        public SchemaBuilder from(String schema) throws IOException {
            this.schemaDefinition = schema;
            return this;
        }
    }
}
