/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:34 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.UserCreditCardUsageInfo;
import io.madhu.creditCardTx.domain.UserCreditLimitSummary;
import io.madhu.creditCardTx.domain.UserCreditCardUsageSummary;
import io.madhu.creditCardTx.dto.mapper.TransactionMapper;
import io.madhu.creditCardTx.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTransactionService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    public UserCreditCardUsageInfo userTotalSpending(String userId){
        return transactionMapper.from(userTransactionRepository.userCreditCardUsageSummary(userId));
    }

    public UserCreditLimitSummary creditLimitSummary(String userId){
        return userTransactionRepository.creditLimitSummary(userId);
    }

}
