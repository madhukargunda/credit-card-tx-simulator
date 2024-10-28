/**
 * Author: Madhu
 * User:madhu
 * Date:2/10/24
 * Time:11:19 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;

import io.madhu.creditCardTx.domain.StoreWiseTransactionSummary;
import io.madhu.creditCardTx.service.StoreWiseTransactionService;
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

import java.util.List;

@RestController
@RequestMapping("/v1")
@Slf4j
@Tag(name = "Store Wise Transaction Summary",
        description = "Store Wise Transaction Summary")
public class StoreWiseTransactionsController {

    @Autowired
    StoreWiseTransactionService storeWiseTransactionService;

    @GetMapping("/stores/{storeName}/summary")
    @Operation(
            summary = "Fetch store wise transactions summary specific to storeName",
            description = "fetches all plant entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreWiseTransactionSummary.class))})
    })
    public StoreWiseTransactionSummary storeWiseTransactionSummary(@Parameter(name = "storeName")
                                                                       @PathVariable("storeName") String storeName) {
        return storeWiseTransactionService.storeWiseTransactionSummary(storeName);
    }

    @GetMapping("/stores/summary")
    @Operation(
            summary = "Fetch all store wise transactions summary",
            description = "fetches all plant entities and their data from data source")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    public List<StoreWiseTransactionSummary> storeWiseTransactionSummary() {
        return storeWiseTransactionService.storeWiseTransactionSummaryList();
    }
}