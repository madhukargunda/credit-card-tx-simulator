/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:12:20 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.processor;


import io.madhu.creditCardTx.constants.CreditStatus;
import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.user.UserCreditLimitOverview;
import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import io.madhu.creditCardTx.model.tx.CreditCardTransaction;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class UserCreditCardUsageSummaryProcessor {

    @Autowired
    private Sinks.Many<UserFinancialSummary> userFinancialSummaryMany;

    private static final double CREDIT_LIMIT = 50000d;

    public KTable<String, UserFinancialSummary> process(KStream<String, CreditCardTransaction> transactionKStream) {
        KTable<String, UserFinancialSummary> totalSpendingUser = transactionKStream
                .groupBy((key, value) -> value.getUserName(), Grouped.with(Serdes.String(), new JsonSerde<CreditCardTransaction>(CreditCardTransaction.class)))
                .aggregate(UserFinancialSummary::new,
                        (key, creditCardTransaction, userFinancialSummary) -> {
                            userFinancialSummary.setTotalSpending(userFinancialSummary.getTotalSpending().add(creditCardTransaction.getTransactionAmount()));
                            userFinancialSummary.setTransactionCount(userFinancialSummary.getTransactionCount() + 1);
                            userFinancialSummary.setCreditLimit(CREDIT_LIMIT);
                            double usagePercentage = (userFinancialSummary.getTotalSpending().doubleValue() / CREDIT_LIMIT) * 100;
                            userFinancialSummary.setLimitApproaching((usagePercentage >= 90 && usagePercentage < 100));
                            userFinancialSummary.setLimitExceeded(usagePercentage >= 90);

                            if (userFinancialSummary.isLimitExceeded()) {
                                userFinancialSummary.setCreditStatus(CreditStatus.THRESHOLD_EXCEEDED);
                                userFinancialSummary.setMessage("You have exceeded your credit limit.");
                                userFinancialSummary.setMessageType("LIMIT_EXCEEDS");
                            } else if (userFinancialSummary.isLimitApproaching()) {
                                userFinancialSummary.setMessage("You are approaching the credit limit");
                                userFinancialSummary.setMessageType("LIMIT_APPROACHING");
                            } else {
                                userFinancialSummary.setCreditStatus(CreditStatus.THRESHOLD_NORMAL);
                                userFinancialSummary.setMessage("You have sufficient credit limit");
                                userFinancialSummary.setMessageType("SUFFICIENT_FUNDS");
                            }
                            userFinancialSummary.setUserEmail(creditCardTransaction.getUserEmail());
                            userFinancialSummary.setUserId(creditCardTransaction.getUserName());
                            return userFinancialSummary;
                        },
                        Materialized.<String, UserFinancialSummary, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.USER_USAGE_SUMMARY_STORE.name())
                                .withCachingDisabled()
                                .withKeySerde(Serdes.String())
                                .withValueSerde(new JsonSerde<>(UserFinancialSummary.class)))
                .mapValues(v -> {
                    userFinancialSummaryMany.tryEmitNext(v);
                    return v;
                });

        totalSpendingUser
                .toStream()
                .print(Printed.<String, UserFinancialSummary>toSysOut()
                        .withLabel("[User-Total-Card-Spending]"));
        this.processUserCreditLimitVerification(totalSpendingUser);
        return totalSpendingUser;
    }


    private KTable<String, UserCreditLimitOverview> processUserCreditLimitVerification(KTable<String, UserFinancialSummary> creditCardUsageSummary) {
        return creditCardUsageSummary
                .mapValues(userSummary -> {
                    UserCreditLimitOverview userCreditLimitOverview = new UserCreditLimitOverview();
                    userCreditLimitOverview.setUserId(userSummary.getUserId());
                    userCreditLimitOverview.setLimitExceeded(userSummary.isLimitExceeded());
                    userCreditLimitOverview.setLimitApproaching(userSummary.isLimitApproaching());
                    userCreditLimitOverview.setAlertMessage(userSummary.getMessage());
                    userCreditLimitOverview.setAlertMessageType(userSummary.getMessageType());
                    userCreditLimitOverview.setCurrentSpending(userSummary.getTotalSpending().doubleValue());
                    userCreditLimitOverview.setCreditLimit(CREDIT_LIMIT);
                    return userCreditLimitOverview;
                }, Materialized.<String, UserCreditLimitOverview, KeyValueStore<Bytes, byte[]>>as(StateStoreTypes.USER_CREDIT_STATUS_STORE.name())
                        .withKeySerde(Serdes.String()).withValueSerde(new JsonSerde<>(UserCreditLimitOverview.class)));
    }

}
