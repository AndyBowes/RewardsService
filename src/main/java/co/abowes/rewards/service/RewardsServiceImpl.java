package co.abowes.rewards.service;

import co.abowes.rewards.exception.InvalidAccountNumberException;

import java.util.*;
import java.util.stream.Collectors;

public class RewardsServiceImpl implements RewardsService {

    final EligibilityService eligibilityService;

    final Map<String, String> availableRewards = new HashMap<>();

    public RewardsServiceImpl(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;

        // Details of available rewards could be loaded from database or configuration
        availableRewards.put("SPORTS", "CHAMPIONS_LEAGUE_FINAL_TICKET");
        availableRewards.put("MUSIC", "KARAOKE_PRO_MICROPHONE");
        availableRewards.put("MOVIES", "PIRATES_OF_THE_CARIBBEAN_COLLECTION");
    }

    @Override
    public List<String> determineRewards(String accountNo, List<String> portfolio) throws InvalidAccountNumberException {

        try {
            final List<String> rewards;
            switch (eligibilityService.checkAccountEligibility(accountNo)) {
                case EligibilityService.CUSTOMER_ELIGIBLE:
                    rewards = portfolio.stream()
                            .map(subscription -> availableRewards.getOrDefault(subscription, null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    break;
                case EligibilityService.CUSTOMER_INELIGIBLE:
                default:
                    rewards = new ArrayList<>();
            }
            return rewards;
        } catch (InvalidAccountNumberException e) {
            throw e;
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

}
