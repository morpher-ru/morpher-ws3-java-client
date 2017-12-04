package communicator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.ArgumentEmptyException;
import exceptions.InvalidFlagsException;

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

    public <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType) throws IOException, InvalidFlagsException, ArgumentEmptyException {
        String url = buildUrl(operation);
        String responseBody = communicator.sendRequest(url, params, httpMethod);

        if (responseType == null) {
            return null;
        }

        return new ObjectMapper().readValue(responseBody, responseType);
    }

    String buildUrl(String methodPath) {
        return Path.combine(baseUrl, methodPath);
    }
}

