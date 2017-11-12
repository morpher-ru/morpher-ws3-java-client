package exceptions;

/**
 * Created by Kraken on 28.09.2017.
 */
public class MorpherException extends Exception {

    private int code;

    public int getCode() {
        return code;
    }

    public MorpherException(String message) {
        super(message);
    }
}
