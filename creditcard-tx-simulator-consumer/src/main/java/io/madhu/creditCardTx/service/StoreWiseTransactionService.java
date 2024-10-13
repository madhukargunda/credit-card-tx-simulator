/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:5:26 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.StoreWiseTransactionSummary;
import io.madhu.creditCardTx.repository.StoreWiseTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreWiseTransactionService {

    @Autowired
    StoreWiseTransactionRepository storeWiseTransactionRepository;

    public StoreWiseTransactionSummary storeWiseTransactionSummary(String storeName) {
        return storeWiseTransactionRepository.storeWiseTransactionSummary(storeName);
    }

    public List<StoreWiseTransactionSummary> storeWiseTransactionSummaryList() {
        return storeWiseTransactionRepository.storeWiseTransactionSummary();
    }
}
