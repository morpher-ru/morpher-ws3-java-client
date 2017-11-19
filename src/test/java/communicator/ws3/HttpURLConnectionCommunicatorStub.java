package communicator.ws3;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class HttpURLConnectionCommunicatorStub extends HttpURLConnectionCommunicator{
    private HttpURLConnectionStub connectionStub;

    public HttpURLConnectionCommunicatorStub(String baseUrl, Authenticator authenticator) throws MalformedURLException {
        super(baseUrl, authenticator);
        connectionStub = new HttpURLConnectionStub(baseUrl);
    }

    @Override
    HttpURLConnection getHttpConnection(String urlString, String method) throws IOException {
        connectionStub.setSourceUrl(urlString);
        connectionStub.setRequestMethod(method);

        return connectionStub;
    }

    HttpURLConnectionStub getConnectionStub() {
        return connectionStub;
    }
}
