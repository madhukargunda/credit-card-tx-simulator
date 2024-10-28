/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:2:33 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.domain.user.UserCreditCardUsageInfo;
import io.madhu.creditCardTx.domain.user.UserCreditLimitOverview;
import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import io.madhu.creditCardTx.service.UserTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RequestMapping("/api/v1/users")
@Slf4j
@RestController
@Tag(name = "User Transactions Summary", description = "User Transactions Summary")
public class UserTransactionController {

    @Autowired
    private UserTransactionService userTransactionService;


    @GetMapping("/financial-summary")
    @Operation(
            summary = "Credit Limit Monitoring and Alerts all users",
            description = "Monitor a user’s transactions to check if their total spending exceeds a predefined credit limit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreditLimitOverview.class))})
    })
    public Flux<UserFinancialSummary> userCreditLimitSummary() {
        return userTransactionService.getAllUserFinancialSummary();
    }

    @GetMapping("/{username}/financial-summary")
    @Operation(
            summary = "Running Total of Credit Card Spending per User",
            description = "Track the running total of a user’s spending over time. This total is " +
                    "continuously updated with every new transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreditCardUsageInfo.class))})
    })
    public Flux<UserFinancialSummary> getUserFinancialSummary(@Valid @NotEmpty @NonNull
                                                              @Parameter(name = "username")
                                                              @PathVariable("username") String username) {
        log.info("Initiating retrieval of financial summary for user {}", username);
        return Flux
                .interval(Duration.ofSeconds(20))
                .flatMap(tick -> userTransactionService.getUserFinancialSummary(username));
    }


}
