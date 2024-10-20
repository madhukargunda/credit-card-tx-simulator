/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:34 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.service;

import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import io.madhu.creditCardTx.mapper.TransactionMapper;
import io.madhu.creditCardTx.repository.UserTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Slf4j
public class UserTransactionService {

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @Autowired
    private Sinks.Many<UserFinancialSummary> userFinancialSummaryMany;

    @Autowired
    private TransactionMapper transactionMapper;

    public Flux<UserFinancialSummary> getUserFinancialSummary(String username) {
        log.info("Fetching financial summary for user: {}", username);
        return Flux.just(userTransactionRepository.getUserFinancialSummary(username));
    }

    public Flux<UserFinancialSummary> getAllUserFinancialSummary() {
        log.info("Fetching financial summary for All users");
        return userFinancialSummaryMany.asFlux();
    }
}
