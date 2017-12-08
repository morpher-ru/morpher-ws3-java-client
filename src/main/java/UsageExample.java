import ru.morpher.ws3.*;

import java.io.IOException;

public class UsageExample extends Log {

    public static void main(String[] argv) {
        // Вы можете передать токен в качестве аргумента конструктора.
        // String token = "17ce56c3-934f-453a-9ef7-cc1feec4e344";
        // !!! Не используйте этот токен в production !!!
        //
        // Если вы используете "Морфер.Сервер" (http://morpher.ru/webservice/local/),
        // то вы можете указать в качестве url адрес вашего локального сервера:
        // String url = "http://ws3.morpher.ru"
        //
        // MorpherClient morpherClient = new MorpherClient(token, url);
        // Для удобства можно использовать встроенный билдер:

        MorpherClient morpherClient = new MorpherClient.ClientBuilder()
                .useToken("a8dab5fe-7a47-4c17-84ea-46facb7d19fe")
                .useUrl("http://ws3.morpher.ru")
                .build();

        try {
            RussianDemo.demo(morpherClient.russian());
            UkrainianDemo.demo(morpherClient.ukrainian());

            log("Остаток запросов на сегодня: " + morpherClient.queriesLeftForToday());
            log("");
        } catch (IOException e) {
            log("Ошибка коммуникации: " + e.getMessage());
        } catch (AccessDeniedException e) {
            log("Ошибка доступа: " + e.getMessage());
        }
    }
}
