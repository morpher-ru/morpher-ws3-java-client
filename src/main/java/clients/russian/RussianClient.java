package clients.russian;

import clients.AbstractLanguageClient;
import clients.russian.data.AdjectiveGendersResult;
import clients.russian.data.CorrectionEntry;
import clients.russian.data.DeclensionResult;
import clients.russian.data.NumberSpellingResult;
import com.fasterxml.jackson.core.type.TypeReference;
import communicator.Communicator;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static communicator.Communicator.HTTP_METHOD_DELETE;
import static communicator.Communicator.HTTP_METHOD_GET;
import static communicator.Communicator.HTTP_METHOD_POST;


public class RussianClient extends AbstractLanguageClient {
    private static final String RUSSIAN = "russian";

    public RussianClient(Communicator communicator) {
        super(communicator);
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

    public List<String> adjectivize(String lemma) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", lemma);

        return sendRequest("adjectivize", params, HTTP_METHOD_GET, new TypeReference<ArrayList<String>>() {
        });
    }

    public AdjectiveGendersResult adjectiveGenders(String lemma) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("s", lemma);

        return sendRequest("genders", params, HTTP_METHOD_GET, AdjectiveGendersResult.class);
    }

    public void addOrUpdateUserDict(CorrectionEntry correctionEntry) throws MorpherException, IOException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("И", correctionEntry.singular.nominative);
        params.put("Р", correctionEntry.singular.genitive);
        params.put("Д", correctionEntry.singular.dative);
        params.put("В", correctionEntry.singular.accusative);
        params.put("Т", correctionEntry.singular.instrumental);
        params.put("П", correctionEntry.singular.prepositional);
        params.put("М", correctionEntry.singular.locative);

        if (correctionEntry.plural != null) {
            //Plural population
            params.put("М_И", correctionEntry.plural.nominative);
            params.put("М_Р", correctionEntry.plural.genitive);
            params.put("М_Д", correctionEntry.plural.dative);
            params.put("М_В", correctionEntry.plural.accusative);
            params.put("М_Т", correctionEntry.plural.instrumental);
            params.put("М_П", correctionEntry.plural.prepositional);
            params.put("М_М", correctionEntry.plural.locative);
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
        return RUSSIAN;
    }
}
