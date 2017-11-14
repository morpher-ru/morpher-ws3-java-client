package communicator;

import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

public interface Communicator {
    String HTTP_METHOD_GET = "GET";
    String HTTP_METHOD_DELETE = "DELETE";
    String HTTP_METHOD_POST = "POST";

    String sendRequest(String url, Map<String, String> params, String method) throws IOException, MorpherException;
}
