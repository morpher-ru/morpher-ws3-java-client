package ru.morpher.ws3;

import ru.morpher.ws3.russian.RussianClient;
import ru.morpher.ws3.ukrainian.UkrainianClient;
import com.fasterxml.jackson.core.type.TypeReference;
import ru.morpher.ws3.communicator.Communicator;
import ru.morpher.ws3.communicator.HttpURLConnectionCommunicator;
import ru.morpher.ws3.communicator.LanguagePathCommunicator;
import ru.morpher.ws3.communicator.PrefixAppender;
import ru.morpher.ws3.communicator.UrlAuthCommunicator;
import ru.morpher.ws3.communicator.ConnectionHandler;

import java.io.IOException;
import java.util.Collections;

import static ru.morpher.ws3.communicator.Communicator.METHOD_GET;

/**
 * <p>
 * Базовый класс, предоставляющий доступ к API веб-сервиса
 * <p>
 * token - access-token при использовании платной версии веб-сервиса
 * url -  endpoint url сервиса
 * ru.morpher.ws3.communicator - кастомная имплементация коммуникатора, также может использоваться для проведения unit-тестов
 */
public class Client {
    private static final String WS3_MORPHER_DEFAULT_URL = "https://ws3.morpher.ru";

    private RussianClient russianClient;
    private UkrainianClient ukrainianClient;
    private LanguagePathCommunicator communicator;

    Client(LanguagePathCommunicator communicator) {
        this.russianClient = new RussianClient(new PrefixAppender(communicator, "russian"));
        this.ukrainianClient = new UkrainianClient(new PrefixAppender(communicator, "ukrainian"));
        this.communicator = communicator;
    }

    public RussianClient russian() {
        return russianClient;
    }

    public UkrainianClient ukrainian() {
        return ukrainianClient;
    }

    public int queriesLeftForToday() throws IOException, AccessDeniedException {
        TypeReference<Integer> responseType = new TypeReference<Integer>() {
        };

        String operation = "get_queries_left_for_today";
        return communicator.sendRequest(operation, Collections.<String, String>emptyMap(), METHOD_GET, responseType);
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

        /** Enables or disables authentication using the specified token.
         * @param clientToken Pass in null to disable authentication (which is the default).
         */
        public ClientBuilder useToken(String clientToken) {
            if (clientToken != null && clientToken.trim().length() == 0) {
                throw new RuntimeException("Token must not be empty.");
            }
            this.token = clientToken;

            return this;
        }

        public ClientBuilder use(Communicator clientCommunicator) {
            this.communicator = clientCommunicator;

            return this;
        }

        public Client build() {
            if (communicator == null) {
                // Can be used for Basic authentication
                // ConnectionHandler connectionHandler = new BasicAuthConnectionHandler(token);
                ConnectionHandler connectionHandler = new ConnectionHandler();
                communicator = new HttpURLConnectionCommunicator(connectionHandler);
            }

            if (token != null) {
                communicator = new UrlAuthCommunicator(token, communicator);
            }

            LanguagePathCommunicator languagePathCommunicator = new LanguagePathCommunicator(url, this.communicator);

            return new Client(languagePathCommunicator);
        }
    }
}
