package communicator;

import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

public interface Communicator {
    String sendRequest(String url, Map<String, String> params, String token, String method) throws IOException, MorpherException;
}
