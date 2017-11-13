import clients.russian.RussianClient;
import communicator.Communicator;
import communicator.HttpURLConnectionCommunicator;

/**
 * <p>
 * Базовый класс, предоставляющий доступ к API веб-сервиса
 * <p>
 * token - access-token при использовании платной версии веб-сервиса
 * url -  url сервиса
 * httpClient - для проведения unit-тестов
 */
public class MorpherClient {
    private String token;
    private String url;
    private Communicator communicator;

    private RussianClient russianClient;

    MorpherClient(String token, String url, Communicator communicator) {
        this.token = token;
        this.url = url;
        this.communicator = communicator;
    }

    public RussianClient russian() {
        if (russianClient == null) {
            russianClient = new RussianClient(token, url, communicator);
        }

        return russianClient;
    }

    public class ClientBuilder {
        private String token = null;
        private String url = "https://ws3.morpher.ru"; //Значение по умолчанию
        private Communicator communicator = new HttpURLConnectionCommunicator();

        public ClientBuilder() {
        }

        public ClientBuilder useUrl(String clientUrl) {
            this.url = clientUrl;

            return this;
        }

        public ClientBuilder useToken(String clientToken) {
            this.token = clientToken;

            return this;
        }

        public ClientBuilder useCommunicator(Communicator clientCommunicator) {
            this.communicator = clientCommunicator;

            return this;
        }

        public MorpherClient build() {
            //Собираем экземпляр класса
            MorpherClient client = new MorpherClient(token, url, communicator);

            //Возвращаем собранный экземпляр
            return client;
        }
    }
}
