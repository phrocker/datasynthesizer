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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.mapr.synth.Util;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.math.random.Multinomial;

import java.util.Iterator;
import java.util.Random;

/**
 * Samples a phone number
 * <p>
 * Thread safe
 */

class PhoneNumberSampler extends FieldSampler {

    IntegerSampler areaCodeSampler = new IntegerSampler();
    IntegerSampler prefixSampler = new IntegerSampler();
    IntegerSampler suffixSampler = new IntegerSampler();

    private Multinomial<Long> dist = null;

    @SuppressWarnings("WeakerAccess")
    public PhoneNumberSampler() {
        prefixSampler.setMaxasInt(999);
        prefixSampler.setMinAsInt(100);
        suffixSampler.setMaxasInt(9999);
        suffixSampler.setMinAsInt(1000);
    }

    public void setAreaCodeMax(String max) {
        this.areaCodeSampler.setMax(max);
    }

    public void setAreaCodeMin(String min) {
        this.areaCodeSampler.setMin(min);
    }


    @Override
    public JsonNode sample() {
        synchronized (this) {
            String areaCode = areaCodeSampler.sample() + "-" + prefixSampler.sample() + "-" + suffixSampler.sample();
            return new TextNode(areaCode);
        }
    }

}
