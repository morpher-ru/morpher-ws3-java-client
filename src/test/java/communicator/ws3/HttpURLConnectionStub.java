package communicator.ws3;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    HttpURLConnectionStub(String url) throws MalformedURLException {
        super(new URL(url), "", 0);
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
}
