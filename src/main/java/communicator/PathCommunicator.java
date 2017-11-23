package communicator;

import exceptions.MorpherException;

import java.io.IOException;
import java.util.Map;

public interface PathCommunicator {
    <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType) throws IOException, MorpherException;
}
