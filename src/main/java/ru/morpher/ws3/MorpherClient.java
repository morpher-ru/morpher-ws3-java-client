package ru.morpher.ws3;

import clients.russian.RussianClient;
import clients.ukrainian.UkrainianClient;
import com.fasterxml.jackson.core.type.TypeReference;
import communicator.Communicator;
import communicator.HttpURLConnectionCommunicator;
import communicator.LanguagePathCommunicator;
import communicator.PrefixAppender;
import communicator.UrlAuthCommunicator;
import communicator.connection.ConnectionHandler;
import exceptions.AccessDeniedException;

import java.io.IOException;
import java.util.Collections;

import static communicator.Communicator.METHOD_GET;
import static java.util.Collections.emptyMap;

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

    private RussianClient russianClient;
    private UkrainianClient ukrainianClient;
    private LanguagePathCommunicator communicator;

    MorpherClient(LanguagePathCommunicator communicator) {
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

        public MorpherClient build() {
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

            return new MorpherClient(languagePathCommunicator);
        }
    }
}
