/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:5:29 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.StoreWiseTransactionSummary;
import io.madhu.creditCardTx.repository.base.StateStoreRepository;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreWiseTransactionRepository {

    @Autowired
    private StateStoreRepository stateStoreRepository;

    public StoreWiseTransactionSummary storeWiseTransactionSummary(String storeName) {
        return stateStoreRepository.getValue(StateStoreTypes.REGION_TRANSACTION_SUMMARY_STORE.name(), storeName,
                String.class, StoreWiseTransactionSummary.class);
    }

    public List<StoreWiseTransactionSummary> storeWiseTransactionSummary() {
         ArrayList storeWiseSummaryList = new ArrayList();
        ReadOnlyKeyValueStore<String, StoreWiseTransactionSummary> repositoryStore = stateStoreRepository
                .getStore(StateStoreTypes.REGION_TRANSACTION_SUMMARY_STORE.name(), String.class, StoreWiseTransactionSummary.class);
        KeyValueIterator<String, StoreWiseTransactionSummary> all = repositoryStore.all();
        while (all.hasNext()) {
            KeyValue<String, StoreWiseTransactionSummary> next = all.next();
            storeWiseSummaryList.add(next.value);
        }
        return storeWiseSummaryList;
    }
}
