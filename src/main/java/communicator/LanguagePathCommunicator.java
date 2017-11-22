package communicator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

//TODO: think about move it to clients package
public class LanguagePathCommunicator {
    private final String baseUrl;
    private final Communicator communicator;

    public LanguagePathCommunicator(String baseUrl, Communicator communicator) {
        this.baseUrl = baseUrl;
        this.communicator = communicator;
    }

    public <T> T sendRequest(String language, String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType) throws IOException, MorpherException {
        String url = buildUrl(language, operation);
        String responseBody = communicator.sendRequest(url, params, httpMethod);
        
        if(responseType == null){
            return null;
        }

        return new ObjectMapper().readValue(responseBody, responseType);
    }

    private static String stripSlashes(String s) {
        if (s.startsWith("/")) s = s.substring(1);
        if (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        return s;
    }

    String buildUrl(String language, String methodPath) {
        return stripSlashes(baseUrl) + "/" + stripSlashes(language) + "/" + stripSlashes(methodPath);
    }
}
