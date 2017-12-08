package ru.morpher.ws3.communicator;

import java.net.HttpURLConnection;

import ru.morpher.ws3.communicator.BasicAuthConnectionHandler;
import ru.morpher.ws3.communicator.HttpURLConnectionStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static ru.morpher.ws3.communicator.BasicAuthConnectionHandler.HEADER_AUTHORIZATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class BasicAuthConnectionHandlerTest {

    private HttpURLConnection connection;

    @Before
    public void setUp() throws Exception {
        connection = new HttpURLConnectionStub("http://test.com");
    }

    @Test
    public void populateAuthHeader_nullToken(){
        String token = null;
        BasicAuthConnectionHandler authenticator = new BasicAuthConnectionHandler(token);

        authenticator.populateAuthHeader(connection);

        Map<String, List<String>> allRequestProperties = connection.getRequestProperties();
        assertFalse(allRequestProperties.containsKey(HEADER_AUTHORIZATION));

        String authHeader = connection.getRequestProperty(HEADER_AUTHORIZATION);
        assertNull(authHeader);
    }

    @Test
    public void populateAuthHeader_emptyToken() throws Exception {
        String token = "";
        BasicAuthConnectionHandler authenticator = new BasicAuthConnectionHandler(token);

        authenticator.populateAuthHeader(connection);

        Map<String, List<String>> allRequestProperties = connection.getRequestProperties();
        assertFalse(allRequestProperties.containsKey(HEADER_AUTHORIZATION));

        String authHeader = connection.getRequestProperty(HEADER_AUTHORIZATION);
        assertNull(authHeader);
    }

    @Test
    public void populateAuthHeader_populatedToken() throws Exception {
        String token = "a3cfb5fe-7a47-4c27-81ea-46facb5d19fa";
        BasicAuthConnectionHandler authenticator = new BasicAuthConnectionHandler(token);

        authenticator.populateAuthHeader(connection);

        Map<String, List<String>> allRequestProperties = connection.getRequestProperties();
        assertTrue(allRequestProperties.containsKey(HEADER_AUTHORIZATION));

        String authHeader = connection.getRequestProperty(HEADER_AUTHORIZATION);
        assertNotNull(authHeader);
        assertEquals("Basic YTNjZmI1ZmUtN2E0Ny00YzI3LTgxZWEtNDZmYWNiNWQxOWZh", authHeader);
    }

}