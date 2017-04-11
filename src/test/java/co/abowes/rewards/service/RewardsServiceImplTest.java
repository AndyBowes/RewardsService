package co.abowes.rewards.service;

import co.abowes.rewards.exception.InvalidAccountNumberException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RewardsServiceImplTest {

    private static final String ELIGIBLE_ACCOUNT_NO = "A12345";
    private static final String INELIGIBLE_ACCOUNT_NO = "Z99999";

    private RewardsService rewardsService;
    private EligibilityService eligibilityService;

    @Before
    public void setup() {
        eligibilityService = Mockito.mock(EligibilityService.class);
        Mockito.when(eligibilityService.checkAccountEligibility(ELIGIBLE_ACCOUNT_NO)).thenReturn(EligibilityService.CUSTOMER_ELIGIBLE);
        Mockito.when(eligibilityService.checkAccountEligibility(INELIGIBLE_ACCOUNT_NO)).thenReturn(EligibilityService.CUSTOMER_INELIGIBLE);
        rewardsService = new RewardsServiceImpl(eligibilityService);
    }

    @Test
    public void testEligibleCustomerNoProducts() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, new ArrayList<>());
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testIneligibleCustomerNoProducts() {
        List<String> rewards = rewardsService.determineRewards(INELIGIBLE_ACCOUNT_NO, new ArrayList<>());
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(INELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithSportsSubscription() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("SPORTS"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.size(), equalTo(1));
        assertThat(rewards.contains("CHAMPIONS_LEAGUE_FINAL_TICKET"), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testIneligibleCustomerWithSportsSubscription() {
        List<String> rewards = rewardsService.determineRewards(INELIGIBLE_ACCOUNT_NO, Arrays.asList("SPORTS"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(INELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithMoviesSubscription() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("MOVIES"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.size(), equalTo(1));
        assertThat(rewards.contains("PIRATES_OF_THE_CARIBBEAN_COLLECTION"), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithMusicSubscription() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("MUSIC"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.size(), equalTo(1));
        assertThat(rewards.contains("KARAOKE_PRO_MICROPHONE"), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerOneNonQualifyingSubscription() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("KIDS"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testIneligibleCustomerOneNonQualifyingSubscription() {
        List<String> rewards = rewardsService.determineRewards(INELIGIBLE_ACCOUNT_NO, Arrays.asList("KIDS"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(INELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithMultipleSubscriptions() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("MOVIES", "KIDS", "MUSIC"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.size(), equalTo(2));
        assertThat(rewards.contains("KARAOKE_PRO_MICROPHONE"), equalTo(true));
        assertThat(rewards.contains("PIRATES_OF_THE_CARIBBEAN_COLLECTION"), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testIneligibleCustomerWithMultipleSubscriptions() {
        List<String> rewards = rewardsService.determineRewards(INELIGIBLE_ACCOUNT_NO, Arrays.asList("MOVIES", "KIDS", "MUSIC"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(INELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithAllSubscriptions() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("SPORTS", "KIDS", "MUSIC", "NEWS", "MOVIES"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.size(), equalTo(3));
        assertThat(rewards.contains("CHAMPIONS_LEAGUE_FINAL_TICKET"), equalTo(true));
        assertThat(rewards.contains("KARAOKE_PRO_MICROPHONE"), equalTo(true));
        assertThat(rewards.contains("PIRATES_OF_THE_CARIBBEAN_COLLECTION"), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testEligibleCustomerWithUnexpectedSubscription() {
        List<String> rewards = rewardsService.determineRewards(ELIGIBLE_ACCOUNT_NO, Arrays.asList("ARTS"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility(ELIGIBLE_ACCOUNT_NO);
    }

    @Test
    public void testHandleUnexpectedExceptions() {
        Mockito.when(eligibilityService.checkAccountEligibility("XXX999")).thenThrow(new RuntimeException("Unexpected Technical Exception"));
        List<String> rewards = rewardsService.determineRewards("XXX999", Arrays.asList("MOVIES"));
        assertThat(rewards, notNullValue());
        assertThat(rewards.isEmpty(), equalTo(true));
        Mockito.verify(eligibilityService).checkAccountEligibility("XXX999");
    }

    @Test(expected = InvalidAccountNumberException.class)
    public void testInvalidAccountNumber() {
        Mockito.when(eligibilityService.checkAccountEligibility("XXX999")).thenThrow(new InvalidAccountNumberException("XXX999"));
        rewardsService.determineRewards("XXX999", Arrays.asList("MOVIES"));
    }

}
