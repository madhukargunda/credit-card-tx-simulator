/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:33 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.domain.UserCreditCardUsageInfo;
import io.madhu.creditCardTx.domain.UserCreditLimitSummary;
import io.madhu.creditCardTx.service.UserTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/users")
@Slf4j
@RestController
@Tag(name = "User Transaction Summary and Credit Limit Monitoring", description = "Credit Limit Monitoring and Alerts")
public class UserTransactionController {

    @Autowired
    private UserTransactionService userTransactionService;

    @GetMapping("/{userId}/total-spending")
    @Operation(
            summary = "Running Total of Credit Card Spending per User",
            description = "Track the running total of a user’s spending over time. This total is " +
                    "continuously updated with every new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserCreditCardUsageInfo.class))})
    })
    public UserCreditCardUsageInfo totalUserSpending(@Parameter(name = "userId") @PathVariable("userId") String userId) {
        return userTransactionService.userTotalSpending(userId);
    }

    @GetMapping("/{userId}/credit-limit")
    @Operation(
            summary = "Credit Limit Monitoring and Alerts",
            description = "Monitor a user’s transactions to check if their total spending exceeds a predefined credit limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreditLimitSummary.class))})
    })
    public UserCreditLimitSummary userCreditLimitSummary(@PathVariable("userId") String userId) {
        return userTransactionService.creditLimitSummary(userId);
    }
}
