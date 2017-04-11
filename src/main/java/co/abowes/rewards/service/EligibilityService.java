package co.abowes.rewards.service;

public interface EligibilityService {

    String CUSTOMER_ELIGIBLE = "CUSTOMER_ELIGIBLE";
    String CUSTOMER_INELIGIBLE = "CUSTOMER_INELIGIBLE";

    String checkAccountEligibility(String accountNo);
}