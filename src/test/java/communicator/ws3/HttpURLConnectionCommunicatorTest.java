package communicator.ws3;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpURLConnectionCommunicatorTest {

    private static final String VALID_METHOD_PATH = "russian/spell";
    private static final String VALID_URL = "https://ws3.morpher.ru/" + VALID_METHOD_PATH;
    private static final String VALID_BASE_URL = "https://ws3.morpher.ru";

    @Test
    public void buildUrl_noSlashesBetween() {
        String baseUrl = VALID_BASE_URL;
        HttpURLConnectionCommunicator communicator = new HttpURLConnectionCommunicator(baseUrl, null);

        String url = communicator.buildUrl(VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtBaseUrl() {
        String baseUrl = VALID_BASE_URL + "/";
        HttpURLConnectionCommunicator communicator = new HttpURLConnectionCommunicator(baseUrl, null);

        String url = communicator.buildUrl(VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_startingSlashAtMethodPath() {
        String baseUrl = VALID_BASE_URL;
        HttpURLConnectionCommunicator communicator = new HttpURLConnectionCommunicator(baseUrl, null);

        String url = communicator.buildUrl("/" + VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtMethodPath() {
        String baseUrl = VALID_BASE_URL;
        HttpURLConnectionCommunicator communicator = new HttpURLConnectionCommunicator(baseUrl, null);

        String url = communicator.buildUrl(VALID_METHOD_PATH + "/");

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtBaseUrlAndStartingSlashAndEndingSlashAtMethodPath() {
        String baseUrl = VALID_BASE_URL + "/";
        HttpURLConnectionCommunicator communicator = new HttpURLConnectionCommunicator(baseUrl, null);

        String url = communicator.buildUrl("/" + VALID_METHOD_PATH + "/");

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }


    @Test
    public void toRequestParameters() {

    }


    @Test
    public void populatePostParams() {

    }


}