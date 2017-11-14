package clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import communicator.Communicator;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractLanguageClient {

    protected Communicator communicator;

    protected abstract String getLanguage();

    protected void sendRequest(String operation, Map<String, String> params, String httpMethod) throws IOException, MorpherException {
        String methodPath = "/" + getLanguage() + "/" + operation;
        communicator.sendRequest(methodPath, params, httpMethod);
    }

    protected <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, Class<T> responseType) throws IOException, MorpherException {
        String methodPath = "/" + getLanguage() + "/" + operation;

        String responseBody = communicator.sendRequest(methodPath, params, httpMethod);
        return new ObjectMapper().readValue(responseBody, responseType);
    }

    protected <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType) throws IOException, MorpherException {
        String methodPath = "/" + getLanguage() + "/" + operation;

        String responseBody = communicator.sendRequest(methodPath, params, httpMethod);
        return new ObjectMapper().readValue(responseBody, responseType);
    }
}
