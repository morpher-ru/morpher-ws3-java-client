package exceptions;

public class InvalidServerResponseException extends RuntimeException {
    private int responseCode;

    public InvalidServerResponseException(int responseCode, String message) {
        super(message);
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
}
