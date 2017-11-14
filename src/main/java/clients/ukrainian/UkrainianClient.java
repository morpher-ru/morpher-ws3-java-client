package clients.ukrainian;

import clients.AbstractLanguageClient;
import clients.ukrainian.data.CorrectionEntry;
import clients.ukrainian.data.DeclensionResult;
import clients.ukrainian.data.NumberSpellingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import communicator.Communicator;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static communicator.Communicator.HTTP_METHOD_DELETE;
import static communicator.Communicator.HTTP_METHOD_GET;
import static communicator.Communicator.HTTP_METHOD_POST;

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

    public void addOrUpdateToUserDict(CorrectionEntry correctionEntry) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Н", correctionEntry.singular.nominative);
        params.put("Р", correctionEntry.singular.genitive);
        params.put("Д", correctionEntry.singular.dative);
        params.put("З", correctionEntry.singular.accusative);
        params.put("О", correctionEntry.singular.instrumental);
        params.put("М", correctionEntry.singular.prepositional);
        params.put("К", correctionEntry.singular.vocative);


        if (correctionEntry.plural != null) {
            //Plural population
            params.put("М_Н", correctionEntry.plural.nominative);
            params.put("М_Р", correctionEntry.plural.genitive);
            params.put("М_Д", correctionEntry.plural.dative);
            params.put("М_З", correctionEntry.plural.accusative);
            params.put("М_О", correctionEntry.plural.instrumental);
            params.put("М_М", correctionEntry.plural.prepositional);
            params.put("М_К", correctionEntry.plural.vocative);
        }

        sendRequest("userdict", params, HTTP_METHOD_POST);
    }

    public List<CorrectionEntry> fetchAllFromUserDictionary() throws MorpherException, IOException {
        return sendRequest("userdict", null, HTTP_METHOD_GET, new TypeReference<List<CorrectionEntry>>() {
        });
    }

    public boolean removeFromUserDictionary(String nominativeCorrection) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", nominativeCorrection);

        return sendRequest("userdict", params, HTTP_METHOD_DELETE, Boolean.class);
    }

    @Override
    protected String getLanguage() {
        return UKRAINIAN;
    }
}
