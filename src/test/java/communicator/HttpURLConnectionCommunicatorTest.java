package communicator;

import communicator.connection.ConnectionHandler;
import exceptions.ArgumentEmptyException;
import exceptions.DailyLimitExceededException;
import exceptions.InvalidFlagsException;
import exceptions.InvalidServerResponseException;
import exceptions.IpBlockedException;
import exceptions.TokenNotFoundException;
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
import static communicator.Communicator.CONTENT_BODY_KEY;
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
    public void sendRequest_urlParamsPopulatedForGETParams() throws IOException {
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
    public void sendRequest_urlParamsPopulatedForPOSTParams() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", "тест");

        communicator.sendRequest("russian/some-operation", params, METHOD_POST);

        // URL doesn't contain request params
        assertEquals(httpURLConnection.getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation");

        // METHOD_POST body is populated with request url-encoded key-value params
        assertEquals(httpURLConnection.getOutputStream().toString(), "s=%D1%82%D0%B5%D1%81%D1%82");
        assertEquals(httpURLConnection.getRequestProperty("Content-Length"), "26");
        assertEquals(httpURLConnection.getRequestProperty("Content-Type"), "application/x-www-form-urlencoded");
    }

    @Test
    public void sendRequest_contentBodyPopulatedForPOSTParamsWithoutKey() throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put(CONTENT_BODY_KEY, "текст без преобразования через URLEncoder");

        communicator.sendRequest("russian/some-operation", params, METHOD_POST);

        // URL doesn't contain request params
        assertEquals(httpURLConnection.getSourceUrl(), "https://ws3.morpher.ru/russian/some-operation");

        // METHOD_POST body is populated with request params
        assertEquals(httpURLConnection.getOutputStream().toString(), "текст без преобразования через URLEncoder");
        assertEquals(httpURLConnection.getRequestProperty("Content-Length"), "68");
        assertEquals(httpURLConnection.getRequestProperty("Content-Type"), "text-plain");
    }

    @Test
    public void sendRequest_urlParamsPopulatedForDELETEParams() throws IOException {
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
    public void sendRequest_validateErrorResponseCode_400_InvalidServerResponseException() throws IOException, InvalidFlagsException {
        httpURLConnection.setResponseCode(400);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw ArgumentEmptyException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (InvalidServerResponseException e) {
            assertEquals(e.getResponseCode(), 400);
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_402_DailyLimitExceededException() throws IOException {
        httpURLConnection.setResponseCode(402);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw DailyLimitExceededException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (DailyLimitExceededException e) {
            assertEquals(e.getMessage(), "Превышен лимит на количество запросов");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_403_IpBlockedException() throws IOException {
        httpURLConnection.setResponseCode(403);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw IpBlockedException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (IpBlockedException e) {
            assertEquals(e.getMessage(), "IP-адрес заблокирован");
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_495_InvalidServerResponseException() throws IOException {
        httpURLConnection.setResponseCode(495);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw InvalidServerResponseException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (InvalidServerResponseException e) {
            assertEquals(e.getResponseCode(), 495);
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_496() throws IOException {
        httpURLConnection.setResponseCode(496);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw InvalidServerResponseException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (InvalidServerResponseException e) {
            assertEquals(e.getResponseCode(), 496);
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_497() throws IOException {
        httpURLConnection.setResponseCode(497);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw InvalidServerResponseException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (InvalidServerResponseException e) {
            assertEquals(e.getMessage(), "Неверный формат токена");
            assertEquals(e.getResponseCode(), 497);
        }
    }

    @Test
    public void sendRequest_validateErrorResponseCode_498() throws IOException {
        httpURLConnection.setResponseCode(498);

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw TokenNotFoundException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (TokenNotFoundException e) {
            assertEquals(e.getMessage(), "Переданный токен не найден");
        }
    }

    @Test
    public void sendRequest_validateUnknownErrorResponseCode_500() throws IOException {
        httpURLConnection.setResponseCode(500);
        httpURLConnection.setErrorStream(new ByteArrayInputStream("We have maintenance, please try again later".getBytes()));

        try {
            communicator.sendRequest("russian/some-operation", new HashMap<String, String>(), METHOD_GET);
            fail("Should throw InvalidServerResponseException");
        } catch (IOException e) {
            fail("Should not throw IOException  should be thrown instead");
        } catch (InvalidServerResponseException e) {
            assertEquals(e.getMessage(), "Сервер вернул неожиданный код. Возможно, у вас неактуальная версия клиента.");
            assertEquals(e.getResponseCode(), 500);
        }
    }

    @Test
    public void sendRequest_ResponseInputStreamShouldBeConvertedToStringResponse() throws IOException {
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