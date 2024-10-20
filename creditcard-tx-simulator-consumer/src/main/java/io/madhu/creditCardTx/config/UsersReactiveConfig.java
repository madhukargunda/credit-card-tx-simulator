/**
 * Author: Madhu
 * User:madhu
 * Date:27/10/24
 * Time:5:13 PM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.config;

import io.madhu.creditCardTx.domain.user.UserFinancialSummary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class UsersReactiveConfig {

    @Bean
    public Sinks.Many<UserFinancialSummary> userFinancialSummaryMany() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
