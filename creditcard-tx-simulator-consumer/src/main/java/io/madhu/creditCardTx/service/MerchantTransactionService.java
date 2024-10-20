/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:1:57 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.dto.store.Stall;
import io.madhu.creditCardTx.dto.store.Store;
import io.madhu.creditCardTx.exception.ResourceNotFoundException;
import io.madhu.creditCardTx.mapper.TransactionMapper;
import io.madhu.creditCardTx.repository.MerchantTransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MerchantTransactionService {

    @Autowired
    MerchantTransactionsRepository merchantTransactionsRepository;

    @Autowired
    StoreTransactionService storeTransactionService;

    @Autowired
    TransactionMapper transactionMapper;

    public List<Stall> getAllMerchantSummaries() {
        Map<String, Stall> aggregateAllStallsTransactions = new HashMap<>();
        List<Store> allStoreSummaries = storeTransactionService.getAllStoreSummaries();
        Map<String, List<Stall>> stallGroups = allStoreSummaries
                .stream()
                .flatMap(store -> store.getStalls().stream())
                .collect(Collectors.groupingBy(stall -> stall.getStallName()));

        stallGroups.entrySet().forEach(entry -> {
            double totalAmount = 0;
            int transcationCount = 0;
            for (Stall stall : entry.getValue()) {
                totalAmount += stall.getTotalTransactionAmount();
                transcationCount += stall.getTransactionCount();
            }
            aggregateAllStallsTransactions.put(entry.getKey(), new Stall(entry.getKey(), transcationCount, totalAmount));
        });
        return aggregateAllStallsTransactions.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    public Stall getMerchantSummary(String merchantName) {
        return this.getAllMerchantSummaries().stream()
                .filter(stall -> stall.getStallName().equalsIgnoreCase(merchantName))
                .findAny()
                .orElseThrow(() -> new ResourceNotFoundException(merchantName + "Not Exist"));
        // return merchantTransactionsRepository.getMerchantSummary(merchantName);
    }
}
