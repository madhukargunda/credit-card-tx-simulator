/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:3:49 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.domain;

import lombok.Data;

@Data
public class UserCreditLimitSummary {

    private String userId;
    private double creditLimit;
    private double currentSpending;
    private boolean limitApproaching;
    private boolean limitExceeded;
    private String alertMessage;
    private String alertMessageType;

}
