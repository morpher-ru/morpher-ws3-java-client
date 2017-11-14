import clients.russian.RussianClient;
import clients.ukrainian.UkrainianClient;
import communicator.Authenticator;
import communicator.Communicator;
import communicator.HttpURLConnectionCommunicator;

/**
 * <p>
 * Базовый класс, предоставляющий доступ к API веб-сервиса
 * <p>
 * token - access-token при использовании платной версии веб-сервиса
 * url -  endpoint url сервиса
 * communicator - кастомная имплементация коммуникатора, также может использоваться для проведения unit-тестов
 */
public class MorpherClient {
    private static final String WS3_MORPHER_DEFAULT_URL = "https://ws3.morpher.ru";

    private Communicator communicator;

    private RussianClient russianClient;
    private UkrainianClient ukrainianClient;

    MorpherClient(Communicator communicator) {
        this.communicator = communicator;
    }

    public RussianClient russian() {
        if (russianClient == null) {
            russianClient = new RussianClient(communicator);
        }

        return russianClient;
    }

    public UkrainianClient ukrainian() {
        if (ukrainianClient == null) {
            ukrainianClient = new UkrainianClient(communicator);
        }

        return ukrainianClient;
    }

    public static class ClientBuilder {
        private String url = WS3_MORPHER_DEFAULT_URL;
        private String token;
        private Communicator communicator;

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
            if (communicator == null) {
                Authenticator authenticator = new Authenticator(token);
                communicator = new HttpURLConnectionCommunicator(url, authenticator);
            }

            return new MorpherClient(communicator);
        }
    }
}
