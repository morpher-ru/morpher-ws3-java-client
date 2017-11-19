package communicator.ws3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class HttpURLConnectionCommunicatorStub extends HttpURLConnectionCommunicator{
    private HttpURLConnectionStub connectionStub;

    public HttpURLConnectionCommunicatorStub(String baseUrl, Authenticator authenticator) throws MalformedURLException {
        super(baseUrl, authenticator);
    }

    @Override
    HttpURLConnection getHttpConnection(String urlString, String method) throws IOException {
        connectionStub = new HttpURLConnectionStub(urlString);
        return connectionStub;
    }

    HttpURLConnectionStub getConnectionStub() {
        return connectionStub;
    }
}
