/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:3:49 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain.user;

import lombok.Data;

@Data
@Deprecated
public class UserCreditLimitOverview {

    private String userId;
    private double creditLimit;
    private double currentSpending;
    private boolean limitApproaching;
    private boolean limitExceeded;
    private String alertMessage;
    private String alertMessageType;

}
