package com.andrewjones;

/* Fails with:

Caused by: org.apache.kafka.common.KafkaException: Failed to construct kafka producer
	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:338)
	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:163)
	at org.apache.beam.sdk.io.kafka.KafkaIO$KafkaWriter.setup(KafkaIO.java:1582)
Caused by: java.lang.NullPointerException
	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:230)
	at org.apache.kafka.clients.producer.KafkaProducer.<init>(KafkaProducer.java:163)
	at org.apache.beam.sdk.io.kafka.KafkaIO$KafkaWriter.setup(KafkaIO.java:1582)

No idea why...
 */

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.kafka.KafkaIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Arrays;
import java.util.List;

public class KafkaProducerValuesExample {
    static final String[] WORDS_ARRAY = new String[] {
            "hi there", "hi", "hi sue bob",
            "hi sue", "", "bob hi"};

    static final List<String> WORDS = Arrays.asList(WORDS_ARRAY);

    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.create();
        Pipeline p = Pipeline.create(options);

        PCollection<String> input = p.apply(Create.of(WORDS).withCoder(StringUtf8Coder.of()));
        input.apply(KafkaIO.<Void, String>write()
                .withBootstrapServers("kafka:29092")
                .withTopic("words")
                .withValueSerializer(StringSerializer.class)
                .values()
        );

        p.run().waitUntilFinish();
    }
}
