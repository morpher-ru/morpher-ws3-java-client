package ru.morpher.ws3.russian;

public class ArgumentNotRussianException extends Exception {
    public ArgumentNotRussianException() {
        super("Не найдено русских слов.");
    }
}
