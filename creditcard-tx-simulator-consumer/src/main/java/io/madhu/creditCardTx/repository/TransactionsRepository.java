/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:30 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.MerchantTransactionsData;
import io.madhu.creditCardTx.repository.base.StateStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class TransactionsRepository {

    @Autowired
    StateStoreRepository stateStoreRepository;

    @Autowired
    private Topology topology;

    public MerchantTransactionsData<String, Long> merchantTransactionSummary() {
        MerchantTransactionsData<String, Long> merchantTransactionsData = new MerchantTransactionsData<String, Long>();
        ReadOnlyKeyValueStore<String, Long> store = stateStoreRepository.getStore(StateStoreTypes.MERCHANT_TRANSACTION_STORE.name(),
                String.class, Long.class);
        KeyValueIterator<String, Long> all = store.all();
        while (all.hasNext()) {
            KeyValue<String, Long> next = all.next();
            merchantTransactionsData.add(next.key, next.value);
        }
        return merchantTransactionsData;
    }


    public MerchantTransactionsData<String, Double> getTotalSpendByUser() {
        MerchantTransactionsData<String, Double> merchantTransactionsData = new MerchantTransactionsData<String, Double>();
        ReadOnlyKeyValueStore<String, Double> store = stateStoreRepository.getStore(StateStoreTypes.USER_USAGE_SUMMARY_STORE.name(),
                String.class, Double.class);
        KeyValueIterator<String, Double> all = store.all();
        while (all.hasNext()) {
            KeyValue<String, Double> next = all.next();
            merchantTransactionsData.add(next.key, next.value);
        }
        return merchantTransactionsData;
    }

}
