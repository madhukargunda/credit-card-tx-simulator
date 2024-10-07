/**
 * Author: Madhu
 * User:madhu
 * Date:1/10/24
 * Time:10:16 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import io.madhu.creditCardTx.constants.LogConstants;
import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.DslStoreSuppliers;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
@Slf4j
public class TransactionKStreamConfig {

    @Autowired
    KafkaProperties kafkaProperties;

    @Value("${spring.kafka.topic}")
    String topicName;

    @Autowired
    private StreamsBuilder streamsBuilder;

    @Bean
    public Topology topology(StreamsBuilder streamsBuilder) {
        log.info("Topology started consuming the records from the topic");
        KStream<String, CreditCardTransaction> creditCardTransactionKStream = streamsBuilder
                .stream(topicName, Consumed.with(Serdes.String(), new JsonSerde<CreditCardTransaction>(CreditCardTransaction.class)));
        printTransactionLog(creditCardTransactionKStream);
        aggregateTxByMerchantCount(creditCardTransactionKStream);
        totalSpendingByUser(creditCardTransactionKStream);
        Topology topology = streamsBuilder.build();
        TopologyDescription description = topology.describe();
        log.info("Topology Description: {}", description);

        // Extract and print state stores from the description
        description.subtopologies().forEach(subtopology -> {
            subtopology.nodes().forEach(node -> {
                if (node.name() != null) {
                    log.info("State stores for node {}: {}", node.name());
                }
            });
        });
        log.info("Topology Description: {}", topology.describe());
        return topology;
    }

    @Bean
    public KTable<String, Double> totalSpendingByUser(KStream<String, CreditCardTransaction> transactionKStream) {
        KTable<String,Double> totalSpendingUser =  transactionKStream
                .groupBy((key, value) -> value.getUserEmail(),Grouped.with(Serdes.String(),new JsonSerde<CreditCardTransaction>(CreditCardTransaction.class)))
                .aggregate(() -> 0.0d,
                        (key, value, currentTotal) -> currentTotal+value.getTransactionAmount().doubleValue(),
                        Materialized.<String,Double,KeyValueStore<Bytes,byte[]>>as(StateStoreTypes.GET_TOTAL_BY_USER.name())
                                .withCachingDisabled()
                                .withKeySerde(Serdes.String())
                                .withValueSerde(Serdes.Double()));
        totalSpendingUser.toStream().print(Printed.<String,Double>toSysOut().withLabel("Total-Spending"));
        return totalSpendingUser;
    }

    private static void printTransactionLog(KStream<String, CreditCardTransaction> creditCardTransactionKStream) {
        creditCardTransactionKStream.mapValues((k, v) ->
                        String.format(LogConstants.PRINT_TX_LOG,
                                        v.getStoreType(),
                                        v.getCreditCardNumber(),
                                        v.getUserName(),
                                        v.getTransactionId(),
                                        v.getTransactionAmount()))
                                .print(Printed.<String, String>toSysOut()
                                .withLabel("[Walmart:]"));
    }

    @Bean
    public KTable<String, Long> aggregateTxByMerchantCount(KStream<String, CreditCardTransaction> transactionKStream) {
        log.info("CreditCard Transactions AggregateByMerchant");
        return transactionKStream.groupBy(
                        (key, value) -> value.getMerchantType().name())
                .count(Materialized.as(StateStoreTypes.GET_COUNT_BY_MERCHANT.name()));
    }

    private Map<String, Object> toMap() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, kafkaProperties.getStreams().getReplicationFactor());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaProperties.getStreams().getApplicationId());
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getStreams().getBootstrapServers());
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG));
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG));
       // props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.NUM_STREAM_THREADS_CONFIG));
        props.put(StreamsConfig.STATE_DIR_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.STATE_DIR_CONFIG));
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, kafkaProperties.getStreams().getProperties().get(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG));
        return props;
    }
}
