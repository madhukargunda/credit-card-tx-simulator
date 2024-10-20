/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:12:27 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.processor;


import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.domain.store.StoreMerchantTransactionData;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

@Component
public class StoreWiseTransactionSummaryProcessor {

    public KTable<String, StoreMerchantTransactionData> process(KStream<String, CreditCardTransaction> transactionKStream) {
        return transactionKStream
                .groupBy((key, creditCardTransaction) -> creditCardTransaction.getStoreType().name(), Grouped.<String, CreditCardTransaction>with(Serdes.String(), new JsonSerde<>(CreditCardTransaction.class
                )))
                .aggregate(StoreMerchantTransactionData::new,
                        (storeName, creditCardTransaction, storeMerchantTransactionData) -> {
                            storeMerchantTransactionData.setStoreTypes(StoreTypes.valueOf(storeName));
                            storeMerchantTransactionData.addStoreTransactionAmount(creditCardTransaction.getTransactionAmount());
                            storeMerchantTransactionData.updateMerchantTransaction(creditCardTransaction.getMerchantType(), creditCardTransaction.getTransactionAmount());
                            return storeMerchantTransactionData;
                        },
                        Materialized.<String, StoreMerchantTransactionData, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.STORE_TRANSACTION_STORE.name())
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(StoreMerchantTransactionData.class)));
    }
}
