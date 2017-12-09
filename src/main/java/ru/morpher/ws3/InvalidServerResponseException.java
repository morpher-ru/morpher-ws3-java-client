package ru.morpher.ws3;

public class InvalidServerResponseException extends RuntimeException {
    private final int responseCode;
    private final String responseBody;

    public InvalidServerResponseException(int responseCode, String message, String responseBody) {
        super(message);
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }
    public String getResponseBody() {return responseBody; }
}
