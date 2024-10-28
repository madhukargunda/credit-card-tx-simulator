/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:49 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.UserCreditLimitSummary;
import io.madhu.creditCardTx.domain.UserCreditCardUsageSummary;
import io.madhu.creditCardTx.repository.base.StateStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserTransactionRepository {

    @Autowired
    private StateStoreRepository stateStoreRepository;

    public UserCreditCardUsageSummary userCreditCardUsageSummary(String userId) {
        return stateStoreRepository.getValue(StateStoreTypes.USER_USAGE_SUMMARY_STORE.name(),
                userId, String.class, UserCreditCardUsageSummary.class);
    }

    public UserCreditLimitSummary creditLimitSummary(String userId){
        return stateStoreRepository.getValue(StateStoreTypes.USER_CREDIT_STATUS_STORE.name(),
                userId, String.class, UserCreditLimitSummary.class);
    }
}
