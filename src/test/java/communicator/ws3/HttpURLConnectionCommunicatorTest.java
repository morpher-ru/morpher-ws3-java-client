package communicator.ws3;

import exceptions.MorpherException;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static communicator.Communicator.HTTP_METHOD_DELETE;
import static communicator.Communicator.HTTP_METHOD_GET;
import static communicator.Communicator.HTTP_METHOD_POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void toRequestParameters_emptyRequestParamsFromNullParamsMap() throws UnsupportedEncodingException {
        String requestParameters = HttpURLConnectionCommunicator.toRequestParameters(null);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "");
    }

    @Test
    public void toRequestParameters_emptyRequestParamsFromEmptyParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();

        String requestParameters = HttpURLConnectionCommunicator.toRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "");
    }

    @Test
    public void toRequestParameters_emptyRequestParamsFromSingleItemInParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        String requestParameters = HttpURLConnectionCommunicator.toRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "s=test");
    }

    @Test
    public void toRequestParameters_emptyRequestParamsFromMultipleItemsInParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");
        params.put("n", "25");
        params.put("unit", "twenty five");

        String requestParameters = HttpURLConnectionCommunicator.toRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "unit=twenty+five&s=test&n=25");
    }

    @Test
    public void toRequestParameters_ignoreEmptyEntriesInMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nullValue", null);
        params.put(null, "nullKey");
        params.put("", "emptyKey");
        params.put("  ", "blankKey");
        params.put("emptyValue", "");
        params.put("blankValue", " ");
        params.put("normalValue", "normalKey");

        String requestParameters = HttpURLConnectionCommunicator.toRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "normalValue=normalKey");
    }

    @Test
    public void populatePostParams_outputSreamOfConnectionPopulatedWithUrlParameters() throws IOException {
        String urlParameters = "unit=twenty+five&s=test&n=25";
        HttpURLConnectionStub con = new HttpURLConnectionStub("http://test.com");
        OutputStream outputStreamPopulatedWithParams = con.getOutputStream();

        // Before population it is empty
        assertEquals(outputStreamPopulatedWithParams.toString(), "");

        // Populating
        HttpURLConnectionCommunicator.populatePostParams(urlParameters, con);

        // After population it contains the same urlParameters that were passed
        assertEquals(outputStreamPopulatedWithParams.toString(), urlParameters);
    }

    @Test
    public void sendRequest_urlParamsPopulatedForGETParams() throws IOException, MorpherException {
        HttpURLConnectionCommunicatorStub communicator = new HttpURLConnectionCommunicatorStub(VALID_BASE_URL, null);

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, HTTP_METHOD_GET);

        // GET request params are part of Url
        assertEquals(communicator.getConnectionStub().getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation?s=test");

        // Request body is empty
        assertEquals(communicator.getConnectionStub().getOutputStream().toString(), "");
        assertNull(communicator.getConnectionStub().getRequestProperty("Content-Length"));
    }

    @Test
    public void sendRequest_urlParamsPopulatedForPOSTParams() throws IOException, MorpherException {
        HttpURLConnectionCommunicatorStub communicator = new HttpURLConnectionCommunicatorStub(VALID_BASE_URL, null);

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, HTTP_METHOD_POST);

        // URL doesn't contain request params
        assertEquals(communicator.getConnectionStub().getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation");

        // POST body is populated with request params
        assertEquals(communicator.getConnectionStub().getOutputStream().toString(), "s=test");
        assertEquals(communicator.getConnectionStub().getRequestProperty("Content-Length"), "6");
    }

    @Test
    public void sendRequest_urlParamsPopulatedForDELETEParams() throws IOException, MorpherException {
        HttpURLConnectionCommunicatorStub communicator = new HttpURLConnectionCommunicatorStub(VALID_BASE_URL, null);

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, HTTP_METHOD_DELETE);

        // GET request params are part of Url
        assertEquals(communicator.getConnectionStub().getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation?s=test");

        // Request body is empty
        assertEquals(communicator.getConnectionStub().getOutputStream().toString(), "");
        assertNull(communicator.getConnectionStub().getRequestProperty("Content-Length"));
    }


}