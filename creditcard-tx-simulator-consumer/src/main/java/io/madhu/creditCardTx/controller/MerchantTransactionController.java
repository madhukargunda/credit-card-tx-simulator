/**
 * Author: Madhu
 * User:madhu
 * Date:13/10/24
 * Time:1:52 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.domain.StoreWiseTransactionSummary;
import io.madhu.creditCardTx.dto.MerchantTransactionsResponse;
import io.madhu.creditCardTx.service.MerchantTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/v1/merchants")
@Slf4j
@Tag(name = "Track the total revenue generated by a merchant ",
        description = "Aggregate and analyze transaction volumes based on merchant categories. For example, analyze " +
        "how much spending is occurring on travel, retail, or dining for each user.")
public class MerchantTransactionController {

    @Autowired
    MerchantTransactionService merchantTransactionService;

    @GetMapping("/transactions/aggregates")
    @Operation(
            summary = "Track the total revenue generated by a merchant ",
            description = "Aggregate all transactions for each merchant to display total transaction amounts and the number of transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MerchantTransactionsResponse.class))})
    })
    public ResponseEntity<?> merchantTransactionSummary() {
        return ResponseEntity.ok(merchantTransactionService.merchantTransactionSummary());
    }

    @GetMapping("/{merchantType}/transactions/aggregates")
    @Operation(
            summary = "Transaction Aggregation by Merchant Name",
            description = "Aggregate all transactions for each merchant to display total transaction amounts and the number of transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreWiseTransactionSummary.class))})
    })
    public ResponseEntity<?> merchantTransactionSummaryByName(@PathVariable(name = "merchantType") String merchantType) {
        log.info("Received request for total count by merchant:");
        return ResponseEntity.ok(merchantTransactionService.merchantTransactionSummary());
    }
}
