package ru.morpher.ws3.communicator.connection;

import ru.morpher.ws3.AccessDeniedException;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

public class BasicAuthConnectionHandler extends ConnectionHandler {
    static final String HEADER_AUTHORIZATION = "Authorization";

    private final String token;

    public BasicAuthConnectionHandler(String token) {
        this.token = token;
    }

    @Override
    public HttpURLConnection openConnection(String urlString) throws IOException, AccessDeniedException {
        HttpURLConnection con = super.openConnection(urlString);
        populateAuthHeader(con);

        return con;
    }

    void populateAuthHeader(HttpURLConnection con) {
        if (token == null || token.trim().length() == 0) {
            return;
        }

        try {
            byte[] encodeBase64 = Base64.encodeBase64(token.getBytes());
            con.setRequestProperty(HEADER_AUTHORIZATION, "Basic " + new String(encodeBase64, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
