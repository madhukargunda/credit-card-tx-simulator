/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:10:47 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.MerchantTransactionsData;
import io.madhu.creditCardTx.dto.MerchantTransactionsResponse;
import io.madhu.creditCardTx.dto.mapper.TransactionMapper;
import io.madhu.creditCardTx.repository.TransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TransactionsService {

    @Autowired
    TransactionsRepository transactionsRepository;

    @Autowired
    TransactionMapper transactionMapper;

    public MerchantTransactionsResponse getByMerchantCount() {
        MerchantTransactionsData<String, Long> countByMerchant = transactionsRepository.getCountByMerchant();
        return transactionMapper.from(countByMerchant, String.class, Long.class);
    }

    public MerchantTransactionsResponse getTotalSpendingByUser() {
        MerchantTransactionsData<String, Double> totalSpendByUser = transactionsRepository.getTotalSpendByUser();
        return transactionMapper.from(totalSpendByUser, String.class, Double.class);
    }
}
