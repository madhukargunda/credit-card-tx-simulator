/**
 * Author: Madhu
 * User:madhu
 * Date:1/10/24
 * Time:10:16 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import io.madhu.creditCardTx.processor.*;
import io.madhu.creditCardTx.util.StreamLoggerUtility;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

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

    @Autowired
    private UserCreditCardUsageSummaryProcessor userCreditCardUsageSummaryProcessor;

    @Autowired
    private FraudTransactionDetectionProcessor fraudTransactionDetectionProcessor;

    @Autowired
    private StoreWiseTransactionSummaryProcessor storeWiseTransactionSummaryProcessor;

    @Autowired
    private StreamLoggerUtility streamLoggerUtility;

    @Bean
    public KStream<String, CreditCardTransaction> creditCardTxStream(StreamsBuilder streamsBuilder) {
        KStream<String, CreditCardTransaction> creditCardTxStream = streamsBuilder
                .stream(topicName, Consumed.with(Serdes.String(), new JsonSerde<CreditCardTransaction>(CreditCardTransaction.class)));
        return creditCardTxStream;
    }


    /**
     * Topology is step by steps computational logic of stream processing application
     *
     * @param streamsBuilder
     * @param creditCardTxStream
     * @return
     */
    @Bean
    public Topology topology(StreamsBuilder streamsBuilder, KStream<String, CreditCardTransaction> creditCardTxStream) {
        log.info("Read from the topic into a KStream");
        // Print incoming transactions for debugging purpose :-)
        streamLoggerUtility.logCreditCardTxKStreamDetails(creditCardTxStream);

        storeWiseTransactionSummaryProcessor.process(creditCardTxStream);
        userCreditCardUsageSummaryProcessor.process(creditCardTxStream);
         fraudTransactionDetectionProcessor.process(creditCardTxStream);
      //  regionWiseTransactionSummaryProcessor.process(creditCardTxStream);
        Topology topology = streamsBuilder.build();
        streamLoggerUtility.logTopologyDetails(topology);
        return topology;
    }


   /* private Map<String, Object> toMap() {
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
    } */
}
