package communicator;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.morpher.ws3.AccessDeniedException;

import java.io.IOException;
import java.util.Map;

public interface PathCommunicator {
    <T> T sendRequest(String operation, Map<String, String> params, String httpMethod, TypeReference<T> responseType)
            throws IOException, AccessDeniedException;
}
