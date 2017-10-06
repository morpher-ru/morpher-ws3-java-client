import Domain.Russian.RussianClient;
import com.squareup.okhttp.OkHttpClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import retrofit2.Retrofit;

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
public class MorpherClient
{
    private static String token;
    private String url;
    private OkHttpClient httpClient;

    private RussianClient russianClient;

    public MorpherClient(String token, String url, OkHttpClient httpClient) {
        this.token = token;
        this.url = url;
        this.httpClient = httpClient;

        russianClient = new RussianClient(token);
    }

    public RussianClient getRussian() {
        return russianClient;
    }

    public static String getToken() { return token; }

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
