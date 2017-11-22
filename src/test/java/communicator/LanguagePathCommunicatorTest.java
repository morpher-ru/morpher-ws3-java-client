package communicator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LanguagePathCommunicatorTest {

    private static final String VALID_BASE_URL = "https://ws3.morpher.ru";
    private static final String VALID_LANGUAGE_PATH = "russian";
    private static final String VALID_METHOD_PATH = "spell";
    private static final String VALID_URL = VALID_BASE_URL + "/" + VALID_LANGUAGE_PATH + "/" + VALID_METHOD_PATH;

    private LanguagePathCommunicator communicator;

    @Before
    public void setUp() throws Exception {
        communicator = new LanguagePathCommunicator(VALID_BASE_URL, null);
    }

    @Test
    public void buildUrl_noSlashesBetween() {
        String url = communicator.buildUrl(VALID_LANGUAGE_PATH, VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtBaseUrl() {
        String baseUrl = VALID_BASE_URL + "/";
        LanguagePathCommunicator communicator = new LanguagePathCommunicator(baseUrl, null);

        String url = communicator.buildUrl(VALID_LANGUAGE_PATH, VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_startingSlashAtLanguagePath() {
        String url = communicator.buildUrl("/" +VALID_LANGUAGE_PATH,  VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtLanguagePath() {
        String url = communicator.buildUrl(VALID_LANGUAGE_PATH + "/",  VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_startingSlashAtMethodPath() {
        String url = communicator.buildUrl(VALID_LANGUAGE_PATH, "/" + VALID_METHOD_PATH);

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_endingSlashAtMethodPath() {
        String url = communicator.buildUrl(VALID_LANGUAGE_PATH, VALID_METHOD_PATH + "/");

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }

    @Test
    public void buildUrl_allStartingAndEndingSlashesRemoved() {
        String baseUrl = VALID_BASE_URL + "/";
        LanguagePathCommunicator communicator = new LanguagePathCommunicator(baseUrl, null);

        String url = communicator.buildUrl("/" + VALID_LANGUAGE_PATH + "/", "/" + VALID_METHOD_PATH + "/");

        assertNotNull(url);
        assertEquals(VALID_URL, url);
    }


}