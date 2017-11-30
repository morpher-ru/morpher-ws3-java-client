package communicator;

import exceptions.DailyLimitExceededException;

import java.io.IOException;
import java.util.Map;

public class CommunicatorStub implements Communicator {
    private String nextResponse;
    private String lastUrlPassed;
    private Map<String, String> lastParamsPassed;
    private String lastHttpMethodPassed;
    private RuntimeException nextException;

    public String sendRequest(String url, Map<String, String> params, String method) throws IOException {
        lastUrlPassed = url;
        lastParamsPassed = params;
        lastHttpMethodPassed = method;

        if(nextException != null){
            throw nextException;
        }

        if (nextResponse == null) {
            return "";
        }

        String response = nextResponse;
        nextResponse = null;

        return response;
    }

    public void writeNextResponse(String nextResponse) {
        this.nextResponse = nextResponse;
    }

    public String readLastUrlPassed() {
        String url = lastUrlPassed;
        lastUrlPassed = null;

        return url;
    }

    public Map<String, String> readLastParamsPassed() {
        Map<String, String> params = lastParamsPassed;
        lastParamsPassed = null;

        return params;
    }

    public String readLastHttpMethodPassed() {
        String method = lastHttpMethodPassed;
        lastHttpMethodPassed = null;

        return method;
    }

    public void throwOnNextCall(RuntimeException nextException) {
        this.nextException = nextException;
    }
}
