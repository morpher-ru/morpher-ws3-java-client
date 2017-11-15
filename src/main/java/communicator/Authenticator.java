package communicator;

import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

public class Authenticator implements Communicator {

    private final String token;
    private final Communicator communicator;

    public Authenticator(String token, Communicator communicator) {
        this.token = token;
        this.communicator = communicator;
    }

    public String sendRequest(String url, Map<String, String> params, String method) throws IOException, MorpherException {
        params.put("token", token);
        return communicator.sendRequest(url, params, method);
    }
}
