package co.abowes.rewards.exception;

public class InvalidAccountNumberException extends RuntimeException {
    public InvalidAccountNumberException(String accountNo) {
        super(String.format("Invalid Account Number : %s", accountNo));
    }
}
