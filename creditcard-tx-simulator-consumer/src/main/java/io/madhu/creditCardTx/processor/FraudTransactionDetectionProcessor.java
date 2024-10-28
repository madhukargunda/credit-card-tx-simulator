/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:12:23 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.processor;


import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.fraud.FraudTransaction;
import io.madhu.creditCardTx.domain.fraud.FraudulentTransactions;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class FraudTransactionDetectionProcessor {


    public KTable<Windowed<String>, FraudulentTransactions> process(KStream<String, CreditCardTransaction> transactionKStream) {
        TimeWindows timeWindows = TimeWindows.of(Duration.ofMinutes(5)).advanceBy(Duration.ofSeconds(30));
        //KTable<Windowed<String>, FraudTransactionList> windowedFraudTransactionsDataKTable = null;

        TimeWindowedKStream<String, CreditCardTransaction> windowedKStream = transactionKStream
                .groupBy((k, v) -> v.getCreditCardNumber(), Grouped.with(Serdes.String(), new JsonSerde<>(CreditCardTransaction.class)))
                .windowedBy(timeWindows);

        KTable<Windowed<String>, FraudulentTransactions> windowedFraudTransactionListKTable = windowedKStream.aggregate(
                        FraudulentTransactions::new,
                        (key, value, initializer) -> {
                            FraudTransaction fraudTransaction = new FraudTransaction();
                            fraudTransaction.setTransactionId(value.getTransactionId());
                            fraudTransaction.setCreditCardNumber(value.getCreditCardNumber());
                            fraudTransaction.setCreditCardHolderName(value.getCardHolderName());
                            fraudTransaction.setTotalCreditAmount(value.getTransactionAmount());
                            initializer.addFraudTransaction(fraudTransaction);
                            return initializer;
                        },
                        Materialized.<String, FraudulentTransactions, WindowStore<Bytes, byte[]>>as(StateStoreTypes.FRAUD_DETECTION.name())
                                .withKeySerde(Serdes.String())  // Windowed<String> Serde for the key
                                .withValueSerde(new JsonSerde<>(FraudulentTransactions.class))
                )
                .filter((windowedKey, fraudulentTransactions) -> fraudulentTransactions.size() >= 3);

        windowedFraudTransactionListKTable
                .toStream()
                .print(Printed.<Windowed<String>, FraudulentTransactions>toSysOut().withLabel("[FRAUD-DETECTION]"));
        return windowedFraudTransactionListKTable;
    }
}
