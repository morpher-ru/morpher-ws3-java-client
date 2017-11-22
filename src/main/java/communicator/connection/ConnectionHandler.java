package communicator.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionHandler {

    public HttpURLConnection openConnection(String urlString) throws IOException {
        URL url = new URL(urlString);

        return (HttpURLConnection) url.openConnection();
    }
}
