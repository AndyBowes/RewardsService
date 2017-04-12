package co.abowes.rewards.service;

import co.abowes.rewards.exception.InvalidAccountNumberException;

import java.util.List;

public interface RewardsService {

    List<String> determineRewards(String accountNo, List<String> portfolio) throws InvalidAccountNumberException;
}
