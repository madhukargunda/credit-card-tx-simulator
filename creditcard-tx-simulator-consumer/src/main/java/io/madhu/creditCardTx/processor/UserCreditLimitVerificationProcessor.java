/**
 * Author: Madhu
 * User:madhu
 * Date:18/10/24
 * Time:12:25 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.processor;


import io.madhu.creditCardTx.constants.StateStoreTypes;
import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import io.madhu.creditCardTx.domain.user.UserCreditLimitOverview;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.kafka.support.serializer.JsonSerde;

@Deprecated
public class UserCreditLimitVerificationProcessor {

    private static final double CREDIT_LIMIT = 50000d;

    public KTable<String, UserCreditLimitOverview> process(KTable<String, UserFinancialSummary> creditCardUsageSummary) {
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
