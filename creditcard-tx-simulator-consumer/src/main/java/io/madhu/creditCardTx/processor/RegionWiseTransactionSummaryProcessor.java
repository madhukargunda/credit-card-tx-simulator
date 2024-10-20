/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:12:18 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.processor;


import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.constants.StoreTypes;
import io.madhu.creditCardTx.domain.store.StoreTransactionData;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;

@Deprecated
public class RegionWiseTransactionSummaryProcessor {

    public KTable<String, StoreTransactionData> process(KStream<String, CreditCardTransaction> transactionKStream) {
        KTable<String, StoreTransactionData> regionWiseTxTotalAmount = transactionKStream
                .groupBy((key, creditCardTransaction) -> creditCardTransaction.getStoreType().getName(),
                        Grouped.<String, CreditCardTransaction>keySerde(Serdes.String()).withValueSerde(new JsonSerde<CreditCardTransaction>()))
                .aggregate(StoreTransactionData::new, (storeName, creditCardTransaction, storeWiseTransactionSummary) -> {
                            storeWiseTransactionSummary.setStore(StoreTypes.getStoreTypeByStoreName(storeName));
                            storeWiseTransactionSummary.setTotalAmount(creditCardTransaction.getTransactionAmount());
                            return storeWiseTransactionSummary;
                        },
                        Materialized.<String, StoreTransactionData, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.STORE_TRANSACTION_STORE.name()));
        regionWiseTxTotalAmount.toStream().print(Printed.<String, StoreTransactionData>toSysOut().withLabel("[Region Wise Total Amount]"));
        return regionWiseTxTotalAmount;
    }
}
