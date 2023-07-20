/*
 * Licensed to the Ted Dunning under one or more contributor license
 * agreements.  See the NOTICE file that may be
 * distributed with this work for additional information
 * regarding copyright ownership.  Ted Dunning licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.mapr.synth.samplers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.dataguardians.datasynth.GeneratorConfiguration;
import com.dataguardians.datasynth.ShortTextGenerator;
import com.dataguardians.exceptions.HttpException;
import com.dataguardians.openai.GenerativeAPI;
import com.dataguardians.security.ApiKey;
import com.dataguardians.security.TokenProvider;

/**
 * Samples a phone number
 * <p>
 * Thread safe
 */

class ChatGPTShortTextSampler extends FieldSampler {

    final ShortTextGenerator shortTextGenerator;

    int preCache = 2;
    private TokenProvider provider = ApiKey.builder().fromEnv("OPENAI_API_KEY").build();

    @SuppressWarnings("WeakerAccess")
    public ChatGPTShortTextSampler() {
        // gross assumption of average of 4 characters per token.
        GeneratorConfiguration config = GeneratorConfiguration.builder().maxTokens(40).build();
        shortTextGenerator = new ShortTextGenerator(provider, new GenerativeAPI(provider), config);

    }

    @Override
    public JsonNode sample() {
        synchronized (this) {
            try {
                return new TextNode(shortTextGenerator.generate());
            } catch (HttpException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
