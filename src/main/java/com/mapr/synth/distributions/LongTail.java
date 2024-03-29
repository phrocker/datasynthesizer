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

package com.mapr.synth.distributions;

import com.google.common.collect.Lists;
import org.apache.mahout.math.random.Sampler;

import java.util.List;

/**
 * Samples from a set of things based on a long-tailed distribution. This converts the Pittman-Yor distribution from a
 * distribution over integers into a distribution over more plausible looking things like words.
 */
public abstract class LongTail<T> implements Sampler<T> {
    private final PittmanYorProcess base;
    private final List<T> things = Lists.newArrayList();

    protected LongTail(double alpha, double discount) {
        base = new PittmanYorProcess(alpha, discount);
    }

    public T sample() {
        int n = base.sample();
        while (n >= things.size()) {
            things.add(createThing());
        }
        return things.get(n);
    }

    public PittmanYorProcess getBaseDistribution() {
        return base;
    }

    protected abstract T createThing();

    public void setThing(int i, T thing) {
        // extend thing list to desired length
        // should almost always extend by exactly 1 object
        while (things.size() <= i) {
            // insert null place holders
            things.add(null);
        }
        things.set(i, thing);
    }

    public void add(T thing) {
        things.add(thing);
    }

    public void setSeed(long seed) {
        base.setSeed(seed);
    }
}
