package communicator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

//TODO: think about move it to clients package
public class LanguagePathCommunicator implements PathCommunicator {
    private final String baseUrl;
    private final Communicator communicator;

    public LanguagePathCommunicator(String baseUrl, Communicator communicator) {
        this.baseUrl = baseUrl;
        this.communicator = communicator;
    }

    public <T> T sendRequest(String operation, Map<String, String> params, String httpMethod) throws IOException, MorpherException {
        String url = buildUrl(operation);
        String responseBody = communicator.sendRequest(url, params, httpMethod);

        return new ObjectMapper().readValue(responseBody, new TypeReference<T>() {});
    }

    String buildUrl(String methodPath) {
        return Path.combine(baseUrl, methodPath);
    }
}

