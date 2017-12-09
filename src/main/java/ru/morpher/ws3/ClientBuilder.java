package ru.morpher.ws3;

import ru.morpher.ws3.communicator.*;

public class ClientBuilder {

    private String url = "https://ws3.morpher.ru";
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
