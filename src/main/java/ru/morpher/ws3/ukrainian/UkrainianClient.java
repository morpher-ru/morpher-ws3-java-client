package ru.morpher.ws3.ukrainian;

import ru.morpher.ws3.ukrainian.data.CorrectionEntry;
import ru.morpher.ws3.ukrainian.data.DeclensionResult;
import ru.morpher.ws3.ukrainian.data.NumberSpellingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import ru.morpher.ws3.communicator.PathCommunicator;
import ru.morpher.ws3.AccessDeniedException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.morpher.ws3.communicator.Communicator.METHOD_DELETE;
import static ru.morpher.ws3.communicator.Communicator.METHOD_GET;
import static ru.morpher.ws3.communicator.Communicator.METHOD_POST;

public class UkrainianClient {
    private PathCommunicator communicator;

    public UkrainianClient(PathCommunicator communicator) {
        this.communicator = communicator;
    }

    public DeclensionResult declension(String lemma) throws IOException, AccessDeniedException {
        TypeReference<DeclensionResult> responseType = new TypeReference<DeclensionResult>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", lemma);

        DeclensionResult declensionResult = communicator.sendRequest("declension", params, METHOD_GET, responseType);
        declensionResult.nominative = lemma;

        return declensionResult;
    }

    public NumberSpellingResult spell(int number, String unit) throws IOException, AccessDeniedException {
        TypeReference<NumberSpellingResult> responseType = new TypeReference<NumberSpellingResult>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("n", String.valueOf(number));
        params.put("unit", unit);

        return communicator.sendRequest("spell", params, METHOD_GET, responseType);
    }

    public void addOrUpdateToUserDict(CorrectionEntry correctionEntry) throws IOException, AccessDeniedException {
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

        communicator.sendRequest("userdict", params, METHOD_POST, null);
    }

    public List<CorrectionEntry> fetchAllFromUserDictionary() throws IOException, AccessDeniedException {
        TypeReference<List<CorrectionEntry>> responseType = new TypeReference<List<CorrectionEntry>>() {
        };

        return communicator.sendRequest("userdict", new HashMap<String, String>(), METHOD_GET, responseType);
    }

    public boolean removeFromUserDictionary(String nominativeCorrection) throws IOException, AccessDeniedException {
        TypeReference<Boolean> responseType = new TypeReference<Boolean>() {
        };

        Map<String, String> params = new HashMap<String, String>();
        params.put("s", nominativeCorrection);

        return communicator.sendRequest("userdict", params, METHOD_DELETE, responseType);
    }

}
