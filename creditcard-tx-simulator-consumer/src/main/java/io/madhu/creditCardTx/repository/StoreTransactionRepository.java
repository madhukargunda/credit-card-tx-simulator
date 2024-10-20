/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:5:29 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.merchant.MerchantTransactionDetails;
import io.madhu.creditCardTx.domain.store.StoreMerchantTransactionData;
import io.madhu.creditCardTx.exception.ResourceNotFoundException;
import io.madhu.creditCardTx.repository.base.KeyValueStateStoreRepository;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class StoreTransactionRepository {

    @Autowired
    private KeyValueStateStoreRepository keyValueStateStoreRepository;

    public StoreMerchantTransactionData getStoreSummary(String storeName) {
        StoreMerchantTransactionData storeMerchantTransactionData = keyValueStateStoreRepository
                .getValue(StateStoreTypes.STORE_TRANSACTION_STORE.name(), storeName,
                        String.class, StoreMerchantTransactionData.class);
        if(Objects.nonNull(storeMerchantTransactionData))
            return storeMerchantTransactionData;
        else
            return new StoreMerchantTransactionData();
    }

    public List<StoreMerchantTransactionData> getAllStoreSummary() {
        ArrayList storeWiseSummaryList = new ArrayList();
        ReadOnlyKeyValueStore<String, StoreMerchantTransactionData> repositoryStore = keyValueStateStoreRepository
                .getStore(StateStoreTypes.STORE_TRANSACTION_STORE.name(), String.class, StoreMerchantTransactionData.class);
        KeyValueIterator<String, StoreMerchantTransactionData> all = repositoryStore.all();
        while (all.hasNext()) {
            KeyValue<String, StoreMerchantTransactionData> next = all.next();
            storeWiseSummaryList.add(next.value);
        }
        return storeWiseSummaryList;
    }

    public List<MerchantTransactionDetails> getAllMerchantSummariesByStore(String storeName) {
        List<MerchantTransactionDetails> merchantTransactionDetailsList = new ArrayList<>();
        StoreMerchantTransactionData storeTransactionData = keyValueStateStoreRepository
                .getValue(StateStoreTypes.STORE_TRANSACTION_STORE.name(), storeName,
                        String.class, StoreMerchantTransactionData.class);
        storeTransactionData.getMerchantTransactionDataMap()
                .entrySet().stream().forEach(entrySet -> {
                    merchantTransactionDetailsList.add(entrySet.getValue());
                });
        return merchantTransactionDetailsList;
    }

    public MerchantTransactionDetails getMerchantSummaryByStore(String storeName, String merchantName) {
        StoreMerchantTransactionData storeTransactionData = keyValueStateStoreRepository
                .getValue(StateStoreTypes.STORE_TRANSACTION_STORE.name(), storeName,
                        String.class, StoreMerchantTransactionData.class);
        return storeTransactionData.getMerchantTransactionDataMap()
                .entrySet()
                .stream()
                .filter(merchantDataSummary -> merchantDataSummary.getKey().equalsIgnoreCase(merchantName))
                .map(entry -> entry.getValue())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No merchantName in database repository"));
    }
}
