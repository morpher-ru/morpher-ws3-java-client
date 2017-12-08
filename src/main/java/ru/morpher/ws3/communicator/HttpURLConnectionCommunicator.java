package ru.morpher.ws3.communicator;

import ru.morpher.ws3.AccessDeniedException;
import ru.morpher.ws3.DailyLimitExceededException;
import ru.morpher.ws3.InvalidServerResponseException;
import ru.morpher.ws3.IpBlockedException;
import ru.morpher.ws3.TokenNotFoundException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpURLConnectionCommunicator implements Communicator {

    private final ConnectionHandler connectionHandler;

    public HttpURLConnectionCommunicator(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    public String sendRequest(String url, Map<String, String> params, String method) throws IOException, AccessDeniedException {
        boolean isContentBody = isContentBody(params, method);

        String requestParameters = isContentBody
                ? params.get(CONTENT_BODY_KEY)
                : toConcatenatedRequestParameters(params);

        if (!isPost(method)) {
            String appenderChar = url.contains("?") ? "&" : "?";
            url = url + appenderChar + requestParameters;
        }

        HttpURLConnection con = getHttpConnection(url, method);

        String contentType = isContentBody
                ? "text-plain"
                : "application/x-www-form-urlencoded";

        con.setRequestProperty("Content-Type", contentType);
        con.setRequestProperty("Accept", "application/json");
        
        if (isPost(method)) {
            populatePostParams(requestParameters, con);
        }

        con.connect();

        int responseCode = con.getResponseCode();
        if (responseCode != 200) {
            String responseErrorBody = toResponseBody(con.getErrorStream());
            handleErrors(responseCode, responseErrorBody);
        }

        return toResponseBody(con.getInputStream());
    }

    private static boolean isPost(String method) {
        return method.equalsIgnoreCase(METHOD_POST);
    }

    static String toConcatenatedRequestParameters(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder paramsString = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getKey() == null || entry.getKey().trim().length() == 0 ||
                    entry.getValue() == null || entry.getValue().trim().length() == 0) {
                // ignore empty entries
                continue;
            }

            String appenderChar = paramsString.length() == 0 ? "" : "&";
            paramsString.append(appenderChar);
            paramsString.append(URLEncoder.encode(entry.getKey(), "UTF8"));
            paramsString.append("=");
            paramsString.append(URLEncoder.encode(entry.getValue(), "UTF8"));
        }

        return paramsString.toString();
    }

    private static boolean isContentBody(Map<String, String> params, String method) {
        return METHOD_POST.equalsIgnoreCase(method) && params.size() == 1 && params.containsKey(CONTENT_BODY_KEY);
    }

    private HttpURLConnection getHttpConnection(String urlString, String method) throws IOException, AccessDeniedException {
        HttpURLConnection con = connectionHandler.openConnection(urlString);
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);
        con.setRequestMethod(method);
        con.setInstanceFollowRedirects(false);
        con.setUseCaches(false);
        con.setDoOutput(true);

        return con;
    }

    static void populatePostParams(String urlParameters, HttpURLConnection con) throws IOException, AccessDeniedException {
        byte[] postData = urlParameters.getBytes("UTF8");
        int postDataLength = postData.length;

        con.setRequestProperty("Content-Length", Integer.toString(postDataLength));

        DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
        outputStream.write(postData);
    }

    private static String toResponseBody(InputStream inputStream) throws IOException, AccessDeniedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            responseBuilder.append(inputLine);
        }

        in.close();

        return responseBuilder.toString();
    }


    private void handleErrors(int responseCode, String responseErrorBody) throws AccessDeniedException {

        switch (responseCode) {
            case 402:
                throw new DailyLimitExceededException("Превышен лимит на количество запросов");
            case 403:
                throw new IpBlockedException("IP-адрес заблокирован");
            case 498:
                throw new TokenNotFoundException("Переданный токен не найден");
            case 497:
                throw new InvalidServerResponseException(497, "Неверный формат токена");
            default:
                throw new InvalidServerResponseException(responseCode, "Сервер вернул неожиданный код. Возможно, у вас неактуальная версия клиента.");
        }
    }
}
