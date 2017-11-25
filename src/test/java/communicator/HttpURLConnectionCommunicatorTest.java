package communicator;

import communicator.connection.ConnectionHandler;
import exceptions.MorpherException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import static communicator.Communicator.METHOD_DELETE;
import static communicator.Communicator.METHOD_GET;
import static communicator.Communicator.METHOD_POST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class HttpURLConnectionCommunicatorTest {
    private HttpURLConnectionCommunicator communicator;
    private HttpURLConnectionStub httpURLConnection;

    @Before
    public void setUp() throws Exception {
        httpURLConnection = new HttpURLConnectionStub("https://ws3.morpher.ru/");

        ConnectionHandler connectionHandler = new ConnectionHandler() {
            @Override
            public HttpURLConnection openConnection(String urlString) throws IOException {
                //build correct url here instead of passing it in each testcase
                httpURLConnection.setSourceUrl(httpURLConnection.getSourceUrl() + urlString);
                return httpURLConnection;
            }
        };
        
        communicator = new HttpURLConnectionCommunicator(connectionHandler);
    }

    @Test
    public void toConcatenatedRequestParameters_emptyRequestParamsFromEmptyParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();

        String requestParameters = HttpURLConnectionCommunicator.toConcatenatedRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "");
    }

    @Test
    public void toConcatenatedRequestParameters_emptyRequestParamsFromSingleItemInParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        String requestParameters = HttpURLConnectionCommunicator.toConcatenatedRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "s=test");
    }

    @Test
    public void toConcatenatedRequestParameters_emptyRequestParamsFromMultipleItemsInParamsMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");
        params.put("n", "25");
        params.put("unit", "twenty five");

        String requestParameters = HttpURLConnectionCommunicator.toConcatenatedRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "unit=twenty+five&s=test&n=25");
    }

    @Test
    public void toConcatenatedRequestParameters_ignoreEmptyEntriesInMap() throws UnsupportedEncodingException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("nullValue", null);
        params.put(null, "nullKey");
        params.put("", "emptyKey");
        params.put("  ", "blankKey");
        params.put("emptyValue", "");
        params.put("blankValue", " ");
        params.put("normalValue", "normalKey");

        String requestParameters = HttpURLConnectionCommunicator.toConcatenatedRequestParameters(params);

        assertNotNull(requestParameters);
        assertEquals(requestParameters, "normalValue=normalKey");
    }

    @Test
    public void populatePostParams_outputStreamOfConnectionPopulatedWithUrlParameters() throws IOException {
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
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, METHOD_GET);

        // METHOD_GET request params are part of Url
        assertEquals(httpURLConnection.getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation?s=test");

        // Request body is empty
        assertEquals(httpURLConnection.getOutputStream().toString(), "");
        assertNull(httpURLConnection.getRequestProperty("Content-Length"));
    }

    @Test
    public void sendRequest_urlParamsPopulatedForPOSTParams() throws IOException, MorpherException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, METHOD_POST);

        // URL doesn't contain request params
        assertEquals(httpURLConnection.getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation");

        // METHOD_POST body is populated with request params
        assertEquals(httpURLConnection.getOutputStream().toString(), "s=test");
        assertEquals(httpURLConnection.getRequestProperty("Content-Length"), "6");
    }

    @Test
    public void sendRequest_urlParamsPopulatedForDELETEParams() throws IOException, MorpherException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "test");

        communicator.sendRequest("russian/some-operation", params, METHOD_DELETE);

        // METHOD_GET request params are part of Url
        assertEquals(httpURLConnection.getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation?s=test");

        // Request body is empty
        assertEquals(httpURLConnection.getOutputStream().toString(), "");
        assertNull(httpURLConnection.getRequestProperty("Content-Length"));
    }

    @Test
    public void sendRequest_validateErrorResponseCode_400() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(400);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Передана пустая строка");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_402() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(402);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Превышен лимит на количество запросов");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_403() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(403);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "IP-адрес заблокирован");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_495() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(495);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Для склонения числительных используйте метод spell");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_496() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(496);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Не найдено русских слов");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_497() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(497);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Неверный формат токена");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_498() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(498);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            assertEquals(e.getMessage(), "Переданный токен не найден");
        }
    }

    @Test
    public void sendRequest_validateUnknownErrorResponseCode_500() throws IOException, MorpherException {
        httpURLConnection.setResponseCode(500);
        httpURLConnection.setErrorStream(new ByteArrayInputStream("We have maintenance, please try again later".getBytes()));

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw MorpherException");
        } catch (IOException e) {
            fail("Should not throw IOException, MorpherException should be thrown instead");
        } catch (MorpherException e) {
            fail("Should not throw MorpherException, RuntimeException should be thrown instead");
        } catch (RuntimeException e) {
            assertEquals(e.getMessage(), "Unexpected error: We have maintenance, please try again later");
        }
    }

    @Test
    public void sendRequest_ResponseInputStreamShouldBeConvertedToStringResponse() throws IOException, MorpherException {
        String expectedResponseString = "{\"feminine\": \"тестовая\",\"neuter\": \"тестовое\",\"plural\": \"тестовые\"}";

        httpURLConnection.setResponseCode(200);
        httpURLConnection.setInputStream(new ByteArrayInputStream(expectedResponseString.getBytes()));

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("s", "тестовый");
        params.put("format", "json");

        String response = communicator.sendRequest("russian/genders", params, METHOD_GET);

        assertEquals(response, expectedResponseString);
    }

}