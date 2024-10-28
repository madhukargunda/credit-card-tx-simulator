/**
 * Author: Madhu
 * User:madhu
 * Date:20/10/24
 * Time:5:31 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "CreditCardTransactions Simulator Consumer API", version = "1.0", description = "CreditCardTransactions Simulator Consumer API"),
        tags = {
                @Tag(name = "User Transactions Summary", description = "Endpoints for User Transactions"),
                @Tag(name = "Merchant Transactions Summary", description = "Endpoints for Merchant Transactions"),
                @Tag(name = "Store Transactions Summary", description = "Endpoints for Store Transactions"),

        }
)
public class SwaggerUiConfiguration {
}
