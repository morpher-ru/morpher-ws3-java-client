import com.squareup.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Kraken on 11.09.2017.
 *
 * Базовый класс, предоставляющий доступ к API веб-сервиса
 *
 * token - access-token при использовании платной версии веб-сервиса
 * url -  url сервиса
 * httpClient - для проведения unit-тестов
 */
@Getter
@Setter
@AllArgsConstructor
public class MorpherClient
{
    private String token;
    private String url;
    private OkHttpClient httpClient;


    public static class ClientBuilder
    {
        private String token = null;
        private String url = "https://ws3.morpher.ru"; //Значение по умолчанию
        private OkHttpClient httpClient = null;

        public ClientBuilder useUrl(String clientUrl) {
            url = clientUrl;

            return this;
        }

        public ClientBuilder useToken(String clientToken) {
            token = clientToken;

            return this;
        }

        public ClientBuilder useHttpClient(OkHttpClient clientHttpClient) {
            httpClient = clientHttpClient;

            return this;
        }

        public MorpherClient build() {
            //Собираем экземпляр класса
            MorpherClient client = new MorpherClient(token, url, httpClient);
            //Сбрасываем значения для следующей сборки
            token = null;
            url = null;
            httpClient = null;
            //Возвращаем собранный экземпляр
            return client;
        }
    }
}
