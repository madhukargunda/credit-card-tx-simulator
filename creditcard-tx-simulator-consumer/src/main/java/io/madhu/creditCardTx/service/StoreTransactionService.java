/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:5:26 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.merchant.MerchantTransactionDetails;
import io.madhu.creditCardTx.domain.store.StoreMerchantTransactionData;
import io.madhu.creditCardTx.dto.store.Stall;
import io.madhu.creditCardTx.dto.store.Store;
import io.madhu.creditCardTx.mapper.StoreTransactionMapper;
import io.madhu.creditCardTx.repository.StoreTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StoreTransactionService {

    @Autowired
    StoreTransactionRepository storeTransactionRepository;

    @Autowired
    StoreTransactionMapper storeTransactionMapper;

    public List<Store> getAllStoreSummaries() {
        log.info("Getting all Store Summaries.........");
        return storeTransactionMapper.from(storeTransactionRepository.getAllStoreSummary());
    }

    public Store getStoreSummary(String storeName) {
        log.info("Getting  Store Summary for storeName...{}", storeName);
        return storeTransactionMapper.from(storeTransactionRepository.getStoreSummary(storeName));
    }

    public List<Stall> getAllMerchantSummariesByStore(String storeName) {
        log.info("The StoreName {} ", storeName);
        StoreMerchantTransactionData storeSummary = storeTransactionRepository.getStoreSummary(storeName);
        return storeTransactionMapper.from(storeSummary).getStalls();
    }

    public Stall getMerchantSummariesByStore(String storeName, String merchantName) {
        log.info("The StoreName {} ,merchantName {} ", storeName, merchantName);
        MerchantTransactionDetails merchantTransactionDetails = storeTransactionRepository.getMerchantSummaryByStore(storeName, merchantName);
        return storeTransactionMapper.from(merchantTransactionDetails);
    }
}
