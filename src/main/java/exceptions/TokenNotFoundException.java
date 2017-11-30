package exceptions;

public class TokenNotFoundException extends AccessDeniedException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
