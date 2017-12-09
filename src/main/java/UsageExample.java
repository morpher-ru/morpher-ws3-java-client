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
        // Client client = new Client(token, url);
        // Для удобства можно использовать встроенный билдер:

        Client client = new Client.ClientBuilder()
                .useToken("a8dab5fe-7a47-4c17-84ea-46facb7d19fe")
                .useUrl("http://ws3.morpher.ru")
                .build();

        try {
            RussianDemo.demo(client.russian());
            UkrainianDemo.demo(client.ukrainian());

            log("Остаток запросов на сегодня: " + client.queriesLeftForToday());
            log("");
        } catch (IOException e) {
            log("Ошибка коммуникации: " + e.getMessage());
        } catch (AccessDeniedException e) {
            log("Ошибка доступа: " + e.getMessage());
        }
    }
}
