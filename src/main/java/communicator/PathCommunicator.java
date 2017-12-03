package communicator;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.Map;

public interface PathCommunicator {
    <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType)
            throws IOException;
}
