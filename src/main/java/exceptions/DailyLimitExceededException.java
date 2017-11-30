package exceptions;

public class DailyLimitExceededException extends AccessDeniedException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}
