package clients.ukrainian;

import clients.AbstractLanguageClient;
import clients.ukrainian.data.DeclensionResult;
import clients.ukrainian.data.NumberSpellingResult;
import communicator.Communicator;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static communicator.Communicator.HTTP_METHOD_GET;

public class UkrainianClient extends AbstractLanguageClient {

    private static final String UKRAINIAN = "ukrainian";

    public UkrainianClient(Communicator communicator) {
        this.communicator = communicator;
    }

    public DeclensionResult declension(String lemma) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", lemma);

        DeclensionResult declensionResult = sendRequest("declension", params, HTTP_METHOD_GET, DeclensionResult.class);
        declensionResult.nominative = lemma;

        return declensionResult;
    }

    public NumberSpellingResult spell(int number, String unit) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("n", String.valueOf(number));
        params.put("unit", unit);

        return sendRequest("spell", params, HTTP_METHOD_GET, NumberSpellingResult.class);
    }

    @Override
    protected String getLanguage() {
        return UKRAINIAN;
    }
}
