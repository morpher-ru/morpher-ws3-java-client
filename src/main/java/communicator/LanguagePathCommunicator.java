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

    String buildUrl(String language, String methodPath) {
        String baseUrlNoEndingSlash = baseUrl;
        if (baseUrl.endsWith("/")) {
            baseUrlNoEndingSlash = baseUrl.substring(0, baseUrl.length() - 1);
        }

        String methodPathNoStartingSlash = methodPath;
        if (methodPathNoStartingSlash.startsWith("/")) {
            methodPathNoStartingSlash = methodPath.substring(1, methodPath.length());
        }

        if (methodPathNoStartingSlash.endsWith("/")) {
            methodPathNoStartingSlash = methodPathNoStartingSlash.substring(0, methodPathNoStartingSlash.length() - 1);
        }

        String languageNoStartingSlash = language;
        if (languageNoStartingSlash.startsWith("/")) {
            languageNoStartingSlash = language.substring(1, language.length());
        }

        if (languageNoStartingSlash.endsWith("/")) {
            languageNoStartingSlash = languageNoStartingSlash.substring(0, languageNoStartingSlash.length() - 1);
        }

        return baseUrlNoEndingSlash + "/" + languageNoStartingSlash + "/" + methodPathNoStartingSlash;
    }
}
