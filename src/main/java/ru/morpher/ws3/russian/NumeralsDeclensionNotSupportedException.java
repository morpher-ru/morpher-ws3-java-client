package ru.morpher.ws3.russian;

public class NumeralsDeclensionNotSupportedException extends Exception {
    public NumeralsDeclensionNotSupportedException() {
        super("Для склонения числительных используйте метод spell.");
    }
}
