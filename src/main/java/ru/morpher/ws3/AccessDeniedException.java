package ru.morpher.ws3;

public class AccessDeniedException extends Exception{
    public AccessDeniedException(String message) {
        super(message);
    }
}
