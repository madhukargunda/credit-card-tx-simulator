/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:7:57 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public class KStreamSpringBootTemplate {

   // @Autowired
    KafkaProperties kafkaProperties;

   // @Bean
    public StreamsBuilder streamsBuilder() {
        return new StreamsBuilder();
    }

   // @Bean
    public StreamsConfig streamsConfig() {
        return new StreamsConfig(this.toMap());
    }

   // @Bean
    public KafkaStreams kafkaStreams() {
        KafkaStreams kafkaStreams = new KafkaStreams(topology(), streamsConfig());
        kafkaStreams.start();
        return kafkaStreams;
    }

   // @Bean
    public Topology topology() {
        StreamsBuilder streamsBuilder = streamsBuilder();
        return streamsBuilder.build();
    }

    private Map<String, Object> toMap() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, kafkaProperties.getStreams().getReplicationFactor());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getStreams().getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getStreams().getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG));
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG));
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.NUM_STREAM_THREADS_CONFIG));
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG));
        return props;
    }
}
