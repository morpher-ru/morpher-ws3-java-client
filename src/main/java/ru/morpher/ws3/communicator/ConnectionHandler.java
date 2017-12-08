package ru.morpher.ws3.communicator;

import ru.morpher.ws3.AccessDeniedException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHandler {

    public HttpURLConnection openConnection(String urlString) throws IOException, AccessDeniedException {
        URL url = new URL(urlString);

        return (HttpURLConnection) url.openConnection();
    }
}
