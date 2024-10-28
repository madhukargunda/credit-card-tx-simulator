/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:30 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.merchant.MerchantTransactionDetails;
import io.madhu.creditCardTx.domain.merchant.MerchantTransactionsData;
import io.madhu.creditCardTx.domain.store.StoreMerchantTransactionData;
import io.madhu.creditCardTx.repository.base.KeyValueStateStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class MerchantTransactionsRepository {

    @Autowired
    KeyValueStateStoreRepository keyValueStateStoreRepository;


    public MerchantTransactionsData getAllMerchantSummaries() {
        MerchantTransactionsData merchantTransactionsData = new MerchantTransactionsData<>();
        ReadOnlyKeyValueStore<String, StoreMerchantTransactionData> store = keyValueStateStoreRepository.getStore(StateStoreTypes.STORE_TRANSACTION_STORE.name(),
                String.class, StoreMerchantTransactionData.class);
        KeyValueIterator<String, StoreMerchantTransactionData> all = store.all();
        store.all().forEachRemaining(storeEntry -> {
            KeyValue<String, StoreMerchantTransactionData> next = all.next();
            Map<String, MerchantTransactionDetails> merchantTransactionSummaryMap = next.value.getMerchantTransactionDataMap();
            merchantTransactionSummaryMap.entrySet()
                    .stream()
                    .forEach(entry -> {
                        merchantTransactionsData.add(entry.getKey(), entry.getValue().getTotalTransactionAmount());
                    });
        });
        return merchantTransactionsData;
    }

    public MerchantTransactionsData getMerchantSummary(String merchantType) {
        MerchantTransactionsData merchantTransactionsData = new MerchantTransactionsData();
        ReadOnlyKeyValueStore<String, StoreMerchantTransactionData> store = keyValueStateStoreRepository.getStore(StateStoreTypes.STORE_TRANSACTION_STORE.name(),
                String.class, StoreMerchantTransactionData.class);
        store.all().forEachRemaining(storeEntry -> {
            Map<String, MerchantTransactionDetails> merchantTransactionSummaryMap = storeEntry.value.getMerchantTransactionDataMap();
            merchantTransactionSummaryMap.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equalsIgnoreCase(merchantType))
                    .forEach(entry -> {
                        MerchantTransactionDetails merchantSummary = entry.getValue();
                        merchantTransactionsData.add(merchantType, merchantSummary.getTotalTransactionAmount());
                    });
        });
        return merchantTransactionsData;
    }


}
