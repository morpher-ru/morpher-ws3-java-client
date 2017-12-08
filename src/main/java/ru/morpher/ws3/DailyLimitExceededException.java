package ru.morpher.ws3;

public class DailyLimitExceededException extends AccessDeniedException {
    public DailyLimitExceededException(String message) {
        super(message);
    }
}
