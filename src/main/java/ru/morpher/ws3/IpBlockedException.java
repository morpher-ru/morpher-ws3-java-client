package ru.morpher.ws3;

public class IpBlockedException extends AccessDeniedException {
    public IpBlockedException(String message) {
        super(message);
    }
}
