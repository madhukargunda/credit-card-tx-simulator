/**
 * Author: Madhu
 * User:madhu
 * Date:1/10/24
 * Time:10:16 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import io.madhu.creditCardTx.constants.CreditStatus;
import io.madhu.creditCardTx.constants.LogConstants;
import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.domain.*;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyDescription;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableKafkaStreams
@Slf4j
public class TransactionKStreamConfig {

    private static final double CREDIT_LIMIT = 5000d;

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
        storeTransactionSummary(creditCardTransactionKStream);
        KTable<String, UserCreditCardUsageSummary> creditCardUsageSummary = creditCardUsageSummary(creditCardTransactionKStream);
        creditLimitStatusKStream(creditCardUsageSummary);
        fraudDetection(creditCardTransactionKStream);
        regionWiseTransactionsSummary(creditCardTransactionKStream);
        Topology topology = streamsBuilder.build();
        printTopology(topology);
        return topology;
    }

    @Bean
    public KTable<String, UserCreditCardUsageSummary> creditCardUsageSummary(KStream<String, CreditCardTransaction> transactionKStream) {
        KTable<String, UserCreditCardUsageSummary> totalSpendingUser = transactionKStream
                .groupBy((key, value) -> value.getUserEmail(), Grouped.with(Serdes.String(), new JsonSerde<CreditCardTransaction>(CreditCardTransaction.class)))
                .aggregate(UserCreditCardUsageSummary::new,
                        (key, creditCardTransaction, userCreditCardUsageSummary) -> {
                            userCreditCardUsageSummary.setTotalSpending(userCreditCardUsageSummary.getTotalSpending().add(creditCardTransaction.getTransactionAmount()));
                            userCreditCardUsageSummary.setTransactionCount(userCreditCardUsageSummary.getTransactionCount() + 1);
                            double usagePercentage = (userCreditCardUsageSummary.getTotalSpending().doubleValue() / CREDIT_LIMIT) * 100;
                            userCreditCardUsageSummary.setLimitApproaching((usagePercentage >= 90 && usagePercentage < 100));
                            userCreditCardUsageSummary.setLimitExceeded(usagePercentage >= 90);

                            if (userCreditCardUsageSummary.isLimitExceeded()) {
                                userCreditCardUsageSummary.setCreditStatus(CreditStatus.THRESHOLD_EXCEEDED);
                                userCreditCardUsageSummary.setMessage("You have exceeded your credit limit.");
                                userCreditCardUsageSummary.setMessageType("LIMIT_EXCEEDS");
                            } else if (userCreditCardUsageSummary.isLimitApproaching()) {
                                userCreditCardUsageSummary.setMessage("You are approaching the credit limit");
                                userCreditCardUsageSummary.setMessageType("LIMIT_APPROACHING");
                            } else {
                                userCreditCardUsageSummary.setCreditStatus(CreditStatus.THRESHOLD_NORMAL);
                                userCreditCardUsageSummary.setMessage("You have sufficient credit limit");
                                userCreditCardUsageSummary.setMessageType("SUFFICIENT_FUNDS");
                            }

                            return userCreditCardUsageSummary;
                        },
                        Materialized.<String, UserCreditCardUsageSummary, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.USER_USAGE_SUMMARY_STORE.name())
                                .withCachingDisabled()
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(UserCreditCardUsageSummary.class)));

        totalSpendingUser.toStream().print(Printed.<String, UserCreditCardUsageSummary>toSysOut().withLabel("[User-Total-Card-Spending]"));
        return totalSpendingUser;
    }

    @Bean
    public KTable<String, UserCreditLimitSummary> creditLimitStatusKStream(KTable<String, UserCreditCardUsageSummary> creditCardUsageSummary) {
        return creditCardUsageSummary
                .mapValues(userSummary -> {
                    UserCreditLimitSummary userCreditLimitSummary = new UserCreditLimitSummary();
                    userCreditLimitSummary.setUserId(userSummary.getUserId());
                    userCreditLimitSummary.setLimitExceeded(userSummary.isLimitExceeded());
                    userCreditLimitSummary.setLimitApproaching(userSummary.isLimitApproaching());
                    userCreditLimitSummary.setAlertMessage(userSummary.getMessage());
                    userCreditLimitSummary.setAlertMessageType(userSummary.getMessageType());
                    userCreditLimitSummary.setCurrentSpending(userSummary.getTotalSpending().doubleValue());
                    userCreditLimitSummary.setCreditLimit(CREDIT_LIMIT);
                    return userCreditLimitSummary;
                }, Materialized.<String, UserCreditLimitSummary, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.USER_CREDIT_STATUS_STORE.name())
                        .withKeySerde(Serdes.String()).withValueSerde(new JsonSerde<>(UserCreditLimitSummary.class)));
    }

    //usecase : Fraudulent Transaction Detection
    @Bean
    public KTable<Windowed<String>, FraudTransactionList> fraudDetection(KStream<String, CreditCardTransaction> transactionKStream) {
        TimeWindows timeWindows = TimeWindows.of(Duration.ofMinutes(5)).advanceBy(Duration.ofSeconds(30));
        //KTable<Windowed<String>, FraudTransactionList> windowedFraudTransactionsDataKTable = null;

        TimeWindowedKStream<String, CreditCardTransaction> windowedKStream = transactionKStream
                .groupBy((k, v) -> v.getCardNumber(), Grouped.with(Serdes.String(), new JsonSerde<>(CreditCardTransaction.class)))
                .windowedBy(timeWindows);

        KTable<Windowed<String>, FraudTransactionList> windowedFraudTransactionListKTable = windowedKStream.aggregate(
                        FraudTransactionList::new,
                        (key, value, initializer) -> {
                            FraudTransaction fraudTransaction = new FraudTransaction();
                            fraudTransaction.setTransactionId(value.getTransactionId());
                            fraudTransaction.setCreditCardNumber(value.getCardNumber());
                            fraudTransaction.setCreditCardHolderName(value.getCardHolderName());
                            fraudTransaction.setTotalCreditAmount(value.getTransactionAmount());
                            initializer.addFraudTransaction(fraudTransaction);
                            return initializer;
                        },
                        Materialized.<String, FraudTransactionList, WindowStore<Bytes, byte[]>>as(StateStoreTypes.FRAUD_DETECTION.name())
                                .withKeySerde(Serdes.String())  // Windowed<String> Serde for the key
                                .withValueSerde(new JsonSerde<>(FraudTransactionList.class))
                )
                .filter((windowedKey, fraudTransactionList) -> fraudTransactionList.size() >= 3);

        windowedFraudTransactionListKTable
                .toStream()
                .print(Printed.<Windowed<String>, FraudTransactionList>toSysOut().withLabel("[FRAUD-DETECTION]"));
        return windowedFraudTransactionListKTable;
    }

    @Bean
    public KTable<String, StoreWiseTransactionSummary> regionWiseTransactionsSummary(KStream<String, CreditCardTransaction> transactionKStream) {
        KTable<String, StoreWiseTransactionSummary> regionWiseTxTotalAmount = transactionKStream
                .groupBy((key, creditCardTransaction) -> creditCardTransaction.getStoreType().getName(),
                        Grouped.<String, CreditCardTransaction>keySerde(Serdes.String()).withValueSerde(new JsonSerde<CreditCardTransaction>()))
                .aggregate(StoreWiseTransactionSummary::new, (storeName, creditCardTransaction, storeWiseTransactionSummary) -> {
                            storeWiseTransactionSummary.setStore(StoreTypes.getStoreTypeByStoreName(storeName));
                            storeWiseTransactionSummary.setTotalAmount(creditCardTransaction.getTransactionAmount());
                            return storeWiseTransactionSummary;
                        },
                        Materialized.<String, StoreWiseTransactionSummary, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.REGION_TRANSACTION_SUMMARY_STORE.name()));
        regionWiseTxTotalAmount.toStream().print(Printed.<String, StoreWiseTransactionSummary>toSysOut().withLabel("[Region Wise Total Amount]"));
        return regionWiseTxTotalAmount;
    }

    @Bean
    public KTable<String, StoreTransactionSummary> storeTransactionSummary(KStream<String, CreditCardTransaction> transactionKStream) {
        return transactionKStream
                .groupBy((key, creditCardTransaction) -> creditCardTransaction.getStoreType().getName())
                .aggregate(StoreTransactionSummary::new,
                        (storeName, creditCardTransaction, storeTransactionSummary) -> {
                            storeTransactionSummary.addMerchantTransactionSummary(creditCardTransaction.getMerchantType(), creditCardTransaction.getTransactionAmount());
                            return storeTransactionSummary;
                        },
                        Materialized.<String, StoreTransactionSummary, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.MERCHANT_TRANSACTION_STORE.name())
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(StoreTransactionSummary.class)));
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

    private static void printTransactionLog(KStream<String, CreditCardTransaction> creditCardTransactionKStream) {
        creditCardTransactionKStream.mapValues((k, v) ->
                        String.format(LogConstants.PRINT_TX_LOG,
                                v.getStoreType(),
                                v.getCardNumber(),
                                v.getUserName(),
                                v.getTransactionId(),
                                v.getTransactionAmount()))
                .print(Printed.<String, String>toSysOut()
                        .withLabel("[Walmart:]"));
    }

    private static Topology printTopology(Topology topology) {
        TopologyDescription description = topology.describe();
        log.info("Topology Description: {}", description);
        // This describes the topology , its helped me in debugging the issues.
        description.subtopologies().forEach(subtopology -> {
            subtopology.nodes().forEach(node -> {
                if (Objects.nonNull(node.name())) {
                    if (node instanceof TopologyDescription.Processor) {
                        TopologyDescription.Processor processor = (TopologyDescription.Processor) node;
                        log.info("Processor {} has the following state stores: {}", processor.name(), processor.stores());
                    }
                    log.info("State stores for node {}: {}", node.name());
                }
            });
        });
        log.info("Topology Description: {}", topology.describe());
        return topology;
    }
}
