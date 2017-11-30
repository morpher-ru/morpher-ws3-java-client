package exceptions;

public class IpBlockedException extends AccessDeniedException {
    public IpBlockedException(String message) {
        super(message);
    }
}
