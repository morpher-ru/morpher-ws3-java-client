package ru.morpher.ws3.communicator;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class HttpURLConnectionStub extends HttpURLConnection {
    private Map<String, List<String>> requestProperties = new HashMap<String, List<String>>();
    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private ByteArrayInputStream inputStream = new ByteArrayInputStream("".getBytes());
    private ByteArrayInputStream errorStream = new ByteArrayInputStream("".getBytes());
    private String sourceUrl;
    private int customResponseCode = 200;

    public HttpURLConnectionStub(String url) throws MalformedURLException {
        super(new URL(url), "", 0);
        this.sourceUrl = url;
    }

    @Override
    public synchronized void setRequestProperty(String headerName, String value) {
        requestProperties.put(headerName, singletonList(value));
    }

    @Override
    public synchronized Map<String, List<String>> getRequestProperties() {
        return requestProperties;
    }

    @Override
    public synchronized String getRequestProperty(String headerName) {
        List<String> properties = requestProperties.get(headerName);
        if (properties == null || properties.size() == 0) {
            return null;
        }

        return properties.get(0);
    }

    @Override
    public synchronized OutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        return inputStream;
    }

    @Override
    public synchronized InputStream getErrorStream(){
        return errorStream;
    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public int getResponseCode() throws IOException {
        return customResponseCode;
    }

    void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    String getSourceUrl() {
        return sourceUrl;
    }

    void setResponseCode(int responseCodeToSet) {
        customResponseCode = responseCodeToSet;
    }

    void setErrorStream(ByteArrayInputStream errorStream) {
        this.errorStream = errorStream;
    }

    void setInputStream(ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
    }
}
