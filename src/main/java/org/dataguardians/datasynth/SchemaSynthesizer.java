/* Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dataguardians.datasynth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mapr.synth.samplers.SchemaSampler;
import org.dataguardians.datasynth.schema.Schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SchemaSynthesizer {

    protected Schema definedSchema = null;

    protected ThreadLocal<SamplerBase> sampler = new ThreadLocal<>();

    public SchemaSynthesizer(Schema schema) throws IOException {
        this.definedSchema = schema;
        loadSchema();
    }

    protected void loadSchema() throws IOException {
        if (sampler.get() == null) {
            try {
                sampler.set(new SchemaSampler(definedSchema.getSchemaDefinition()));
            } catch (MismatchedInputException mie) {
                sampler.set(new SchemaSampler("[" + definedSchema.getSchemaDefinition() + "]"));
            }
        }
    }

    public List<JsonNode> generateRecords(int maxRecordsToGenerate) throws IOException {

        final List<Optional<JsonNode>> records = new ArrayList<>();

        if (maxRecordsToGenerate > 0) {
            final SamplerBase mySampler = sampler.get();
            IntStream.range(0, maxRecordsToGenerate).forEach(i -> records.add(mySampler.nextSample()));
        }
        return records.stream().filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

}