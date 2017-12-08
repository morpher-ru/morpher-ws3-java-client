package ru.morpher.ws3;

public class TokenNotFoundException extends AccessDeniedException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}
