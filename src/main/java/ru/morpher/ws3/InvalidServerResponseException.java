package ru.morpher.ws3;

public class InvalidServerResponseException extends RuntimeException {
    private final int responseCode;

    public InvalidServerResponseException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
