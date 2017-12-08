package ru.morpher.ws3;

public class ArgumentEmptyException extends Exception {
    public ArgumentEmptyException() {
        super("Передана пустая строка");
    }
}
