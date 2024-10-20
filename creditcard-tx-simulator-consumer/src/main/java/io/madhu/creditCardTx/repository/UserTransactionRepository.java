/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:49 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.repository;

import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import io.madhu.creditCardTx.repository.base.KeyValueStateStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@Slf4j
public class UserTransactionRepository {

    @Autowired
    private KeyValueStateStoreRepository keyValueStateStoreRepository;

    public UserFinancialSummary getUserFinancialSummary(String username) {
        log.info("At {}: Fetching financial summary for user '{}'", LocalDateTime.now(), username);
        return keyValueStateStoreRepository.getValue(StateStoreTypes.USER_USAGE_SUMMARY_STORE.name(),
                username, String.class, UserFinancialSummary.class);
    }
}
