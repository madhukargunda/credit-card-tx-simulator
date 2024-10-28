/**
 * Author: Madhu
 * User:madhu
 * Date:15/10/24
 * Time:9:40 AM
 * Project: creditcard-tx-simulator-consumer
 */

package io.madhu.creditCardTx.controller;


import io.madhu.creditCardTx.constants.DashboardConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class DashboardController {

    @GetMapping("/dashboard")
    public String displayDashboard(Model model) {
        return DashboardConstants.STORE_CHART_DASHBOARD;//"storedashboard";
    }

    @GetMapping("/dashboard/summary/all-users/row-view")
    public String showRowAllUserFinancialSummaryDashboard(Model model) {
        log.info("Displaying financial summaries for all users in row view");
        return DashboardConstants.ALL_USERS_ROW_VIEW_DASHBOARD;
    }

    @GetMapping("/dashboard/summary/all-users")
    public String showAllUserFinancialSummaryDashboard(Model model) {
        log.info("Displaying financial summaries for all users");
        return DashboardConstants.ALL_USERS_SUMMARY_DASHBOARD;
    }

    @GetMapping("/dashboard/summary/user/{username}")
    public String showUserFinancialSummaryDashboard(Model model,
                                                    @PathVariable("username") String username) {
        log.info("Displaying financial summary for user: {}", username);
        model.addAttribute("userId", username);
        return DashboardConstants.USER_SPECIFIC_DASHBOARD;
    }

}
