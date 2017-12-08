package ru.morpher.ws3.russian;

public class InvalidFlagsException extends Exception {
    public InvalidFlagsException() {
        super("Указаны неправильные флаги.");
    }
}