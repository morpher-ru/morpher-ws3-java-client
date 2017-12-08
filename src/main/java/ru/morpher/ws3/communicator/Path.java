package ru.morpher.ws3.communicator;

public class Path
{
    private static String stripSlashes(String s) {
        if (s.startsWith("/")) s = s.substring(1);
        if (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    public static String combine(String path1, String path2) {
        return stripSlashes(path1) + "/" + stripSlashes(path2);
    }
}
