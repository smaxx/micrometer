/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.core.samples;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.samples.utils.SampleRegistries;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class FunctionCounterSample {
    public static void main(String[] args) {
        MeterRegistry registry = SampleRegistries.datadog();

        AtomicInteger n = new AtomicInteger(0);

        FunctionCounter.builder("my.fcounter", n, AtomicInteger::get)
            .baseUnit("happiness")
            .description("A counter derived from a monotonically increasing value")
            .register(registry);

        Counter counter = Counter.builder("my.counter")
            .baseUnit("happiness")
            .description("A normal counter")
            .register(registry);

        Flux.interval(Duration.ofMillis(10))
            .doOnEach(i -> {
                n.incrementAndGet();
                counter.increment();
            })
            .blockLast();
    }
}
