import static java.lang.String.format;

class Log
{
    protected static final String PREMIUM = "*****";

    protected static void log(String message) {
        System.out.println(message);
    }

    protected static void log(String messageWithPlaceholders, Object... params) {
        System.out.println(format(messageWithPlaceholders, params));
    }
}
