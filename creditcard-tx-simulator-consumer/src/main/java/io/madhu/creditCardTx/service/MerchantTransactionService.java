/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:1:57 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.MerchantTransactionsData;
import io.madhu.creditCardTx.dto.MerchantTransactionsResponse;
import io.madhu.creditCardTx.dto.mapper.TransactionMapper;
import io.madhu.creditCardTx.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantTransactionService {

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    TransactionMapper transactionMapper;

    public MerchantTransactionsResponse merchantTransactionSummary() {
        MerchantTransactionsData<String, Long> countByMerchant = transactionsRepository.merchantTransactionSummary();
        return transactionMapper.from(countByMerchant, String.class, Long.class);
    }

    public MerchantTransactionsResponse getTotalSpendingByUser() {
        MerchantTransactionsData<String, Double> totalSpendByUser = transactionsRepository.getTotalSpendByUser();
        return transactionMapper.from(totalSpendByUser, String.class, Double.class);
    }
}
