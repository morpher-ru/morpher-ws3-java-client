package clients.ukrainian;

import clients.ukrainian.data.CorrectionEntry;
import clients.ukrainian.data.DeclensionResult;
import clients.ukrainian.data.Gender;
import clients.ukrainian.data.NumberSpellingResult;
import communicator.CommunicatorStub;
import communicator.LanguagePathCommunicator;
import communicator.PrefixAppender;
import exceptions.ArgumentEmptyException;
import exceptions.InvalidFlagsException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static communicator.Communicator.METHOD_DELETE;
import static communicator.Communicator.METHOD_GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UkrainianClientTest {
    private UkrainianClient ukrainianClient;
    private CommunicatorStub communicator;

    @Before
    public void setUp() throws Exception {
        String baseUrl = "https://ws3.morpher.ru";

        communicator = new CommunicatorStub();
        ukrainianClient = new UkrainianClient(new PrefixAppender(new LanguagePathCommunicator(baseUrl, communicator), "ukrainian"));
    }

    @Test
    public void declension_Success() throws IOException, InvalidFlagsException, ArgumentEmptyException {
        communicator.writeNextResponse("{" +
                "\"Р\": \"теста\"," +
                "\"Д\": \"тесту\"," +
                "\"З\": \"теста\"," +
                "\"О\": \"тестом\"," +
                "\"М\": \"тесті\"," +
                "\"К\": \"тесте\"," +
                "\"рід\": \"Чоловічий\"" +
                "}");

        DeclensionResult declensionResult = ukrainianClient.declension("тест");
        assertNotNull(declensionResult);
        assertEquals("тест", declensionResult.nominative);
        assertEquals("теста", declensionResult.genitive);
        assertEquals("тесту", declensionResult.dative);
        assertEquals("теста", declensionResult.accusative);
        assertEquals("тестом", declensionResult.instrumental);
        assertEquals("тесті", declensionResult.prepositional);
        assertEquals("тесте", declensionResult.vocative);
        assertEquals(Gender.Masculine, declensionResult.gender);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("тест", params.get("s"));
        assertEquals("https://ws3.morpher.ru/ukrainian/declension", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void spell_Success() throws IOException, InvalidFlagsException, ArgumentEmptyException {
        communicator.writeNextResponse("{" +
                "\"n\": {" +
                "\"Н\": \"десять\"," +
                "\"Р\": \"десяти\"," +
                "\"Д\": \"десяти\"," +
                "\"З\": \"десять\"," +
                "\"О\": \"десятьма\"," +
                "\"М\": \"десяти\"," +
                "\"К\": \"десять\"" +
                "}," +
                "\"unit\": {" +
                "\"Н\": \"рублів\"," +
                "\"Р\": \"рублів\"," +
                "\"Д\": \"рублям\"," +
                "\"З\": \"рублів\"," +
                "\"О\": \"рублями\"," +
                "\"М\": \"рублях\"," +
                "\"К\": \"рублів\"" +
                "}" +
                "}");

        NumberSpellingResult declensionResult = ukrainianClient.spell(10, "рубль");
        assertNotNull(declensionResult);

        // number
        assertEquals("десять", declensionResult.numberDeclension.nominative);
        assertEquals("десяти", declensionResult.numberDeclension.genitive);
        assertEquals("десяти", declensionResult.numberDeclension.dative);
        assertEquals("десять", declensionResult.numberDeclension.accusative);
        assertEquals("десятьма", declensionResult.numberDeclension.instrumental);
        assertEquals("десяти", declensionResult.numberDeclension.prepositional);
        assertEquals("десять", declensionResult.numberDeclension.vocative);

        // unit
        assertEquals("рублів", declensionResult.unitDeclension.nominative);
        assertEquals("рублів", declensionResult.unitDeclension.genitive);
        assertEquals("рублям", declensionResult.unitDeclension.dative);
        assertEquals("рублів", declensionResult.unitDeclension.accusative);
        assertEquals("рублями", declensionResult.unitDeclension.instrumental);
        assertEquals("рублях", declensionResult.unitDeclension.prepositional);
        assertEquals("рублів", declensionResult.unitDeclension.vocative);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("10", params.get("n"));
        assertEquals("рубль", params.get("unit"));
        assertEquals("https://ws3.morpher.ru/ukrainian/spell", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void removeFromUserDictionary_Success() throws IOException, InvalidFlagsException, ArgumentEmptyException {
        communicator.writeNextResponse("true");

        boolean found = ukrainianClient.removeFromUserDictionary("тест");
        assertTrue(found);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("тест", params.get("s"));
        assertEquals("https://ws3.morpher.ru/ukrainian/userdict", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_DELETE, httpMethod);
    }

    @Test
    public void fetchAllFromUserDictionary_Success() throws IOException, InvalidFlagsException, ArgumentEmptyException {
        communicator.writeNextResponse("[" +
                "{" +
                "\"singular\": {" +
                "\"Н\": \"Тест\"," +
                "\"Р\": \"ТестР\"," +
                "\"Д\": \"ТестД\"," +
                "\"З\": \"ТестЗ\"," +
                "\"О\": \"ТестО\"," +
                "\"М\": \"ТестМ\"," +
                "\"К\": \"ТестК\"" +
                "}" +
                "}" +
                "]");

        List<CorrectionEntry> correctionEntries = ukrainianClient.fetchAllFromUserDictionary();

        assertNotNull(correctionEntries);
        assertEquals(1, correctionEntries.size());
        CorrectionEntry entry = correctionEntries.get(0);

        assertEquals("Тест", entry.singular.nominative);
        assertEquals("ТестР", entry.singular.genitive);
        assertEquals("ТестД", entry.singular.dative);
        assertEquals("ТестЗ", entry.singular.accusative);
        assertEquals("ТестО", entry.singular.instrumental);
        assertEquals("ТестМ", entry.singular.prepositional);
        assertEquals("ТестК", entry.singular.vocative);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(0, params.size());
        assertEquals("https://ws3.morpher.ru/ukrainian/userdict", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

}