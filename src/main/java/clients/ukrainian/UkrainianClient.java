package clients.ukrainian;

import clients.ukrainian.data.CorrectionEntry;
import clients.ukrainian.data.DeclensionResult;
import clients.ukrainian.data.NumberSpellingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import communicator.LanguagePathCommunicator;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static communicator.Communicator.METHOD_DELETE;
import static communicator.Communicator.METHOD_GET;
import static communicator.Communicator.METHOD_POST;

public class UkrainianClient{
    private static final String UKRAINIAN = "ukrainian";
    
    private LanguagePathCommunicator communicator;

    public UkrainianClient(LanguagePathCommunicator communicator) {
        this.communicator = communicator;
    }

    public DeclensionResult declension(String lemma) throws MorpherException, IOException {
        TypeReference<DeclensionResult> responseType = new TypeReference<DeclensionResult>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", lemma);

        DeclensionResult declensionResult = communicator.sendRequest(UKRAINIAN, "declension", params, METHOD_GET, responseType);
        declensionResult.nominative = lemma;

        return declensionResult;
    }

    public NumberSpellingResult spell(int number, String unit) throws MorpherException, IOException {
        TypeReference<NumberSpellingResult> responseType = new TypeReference<NumberSpellingResult>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("n", String.valueOf(number));
        params.put("unit", unit);

        return communicator.sendRequest(UKRAINIAN, "spell", params, METHOD_GET, responseType);
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

        communicator.sendRequest(UKRAINIAN, "userdict", params, METHOD_POST, null);
    }

    public List<CorrectionEntry> fetchAllFromUserDictionary() throws MorpherException, IOException {
        TypeReference<List<CorrectionEntry>> responseType = new TypeReference<List<CorrectionEntry>>() {
        };

        return communicator.sendRequest(UKRAINIAN, "userdict", null, METHOD_GET, responseType);
    }

    public boolean removeFromUserDictionary(String nominativeCorrection) throws MorpherException, IOException {
        TypeReference<Boolean> responseType = new TypeReference<Boolean>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", nominativeCorrection);

        return communicator.sendRequest(UKRAINIAN, "userdict", params, METHOD_DELETE, responseType);
    }

}
