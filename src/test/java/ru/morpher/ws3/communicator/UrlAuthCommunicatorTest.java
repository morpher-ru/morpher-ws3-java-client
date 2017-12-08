package ru.morpher.ws3.communicator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static ru.morpher.ws3.communicator.Communicator.METHOD_GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UrlAuthCommunicatorTest {
    private static final String REQUEST_URL = "https://ws3.morpher.ru/russian/spell";

    private CommunicatorStub communicator;

    @Before
    public void setUp() throws Exception {
        communicator = new CommunicatorStub();
    }

    @Test
    public void sendRequest_passedToken() throws Exception {
        String token = "a3cfb5fe-7a47-4c27-81ea-46facb5d19fa";
        UrlAuthCommunicator urlAuthCommunicator = new UrlAuthCommunicator(token, communicator);

        urlAuthCommunicator.sendRequest(REQUEST_URL, new HashMap<String, String>(), METHOD_GET);

        String urlPassed = communicator.readLastUrlPassed();
        assertNotNull(urlPassed);
        assertEquals(REQUEST_URL + "?token=" + token, urlPassed);
    }

    @Test
    public void sendRequest_passedTokenAndUrlContainsRequestParams() throws Exception {
        String token = "a3cfb5fe-7a47-4c27-81ea-46facb5d19fa";
        UrlAuthCommunicator urlAuthCommunicator = new UrlAuthCommunicator(token, communicator);

        urlAuthCommunicator.sendRequest(REQUEST_URL + "?s=test", new HashMap<String, String>(), METHOD_GET);

        String urlPassed = communicator.readLastUrlPassed();
        assertNotNull(urlPassed);
        assertEquals(REQUEST_URL + "?s=test&token=" + token, urlPassed);
    }

}