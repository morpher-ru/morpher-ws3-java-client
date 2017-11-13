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
    private String url = "https://ws3.morpher.ru";
    private Communicator communicator = new HttpURLConnectionCommunicator();

    private RussianClient russianClient;

    public MorpherClient(String token) {
        this.token = token;
    }

    public MorpherClient(String token, String url) {
        this.token = token;
        this.url = url;
    }

    public MorpherClient(String token, String url, Communicator communicator) {
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

    public static ClientBuilder createNewClient(){
        return new ClientBuilder();
    }

    public static class ClientBuilder {
        private String token = null;
        private String url = "https://ws3.morpher.ru"; //Значение по умолчанию
        private Communicator communicator = new HttpURLConnectionCommunicator();

        private ClientBuilder() {
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

            //Сбрасываем значения для следующей сборки
            token = null;
            url = null;
            communicator = null;

            //Возвращаем собранный экземпляр
            return client;
        }
    }
}
