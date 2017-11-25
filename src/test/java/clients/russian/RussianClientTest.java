package clients.russian;

import clients.russian.data.AdjectiveGendersResult;
import clients.russian.data.CorrectionEntry;
import clients.russian.data.DeclensionResult;
import clients.russian.data.Gender;
import clients.russian.data.NumberSpellingResult;
import communicator.CommunicatorStub;
import communicator.LanguagePathCommunicator;
import communicator.PrefixAppender;
import exceptions.MorpherException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static communicator.Communicator.METHOD_DELETE;
import static communicator.Communicator.METHOD_GET;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RussianClientTest {
    private RussianClient russianClient;
    private CommunicatorStub communicator;

    @Before
    public void setUp() throws Exception {
        String baseUrl = "https://ws3.morpher.ru";

        communicator = new CommunicatorStub();
        russianClient = new RussianClient(new PrefixAppender(new LanguagePathCommunicator(baseUrl, communicator), "russian"));
    }

    @Test
    public void declension_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("{\n" +
                "  \"Р\": \"теста\",\n" +
                "  \"Д\": \"тесту\",\n" +
                "  \"В\": \"тест\",\n" +
                "  \"Т\": \"тестом\",\n" +
                "  \"П\": \"тесте\",\n" +
                "  \"П_о\": \"о тесте\",\n" +
                "  \"род\": \"Мужской\",\n" +
                "  \"множественное\": {\n" +
                "    \"И\": \"тесты\",\n" +
                "    \"Р\": \"тестов\",\n" +
                "    \"Д\": \"тестам\",\n" +
                "    \"В\": \"тесты\",\n" +
                "    \"Т\": \"тестами\",\n" +
                "    \"П\": \"тестах\",\n" +
                "    \"П_о\": \"о тестах\"\n" +
                "  },\n" +
                "  \"где\": \"в тесте\",\n" +
                "  \"куда\": \"в тест\",\n" +
                "  \"откуда\": \"из теста\"\n" +
                "}");

        DeclensionResult declensionResult = russianClient.declension("тест");
        assertNotNull(declensionResult);
        assertNotNull(declensionResult.plural);
        assertEquals("тест", declensionResult.nominative);
        assertEquals("теста", declensionResult.genitive);
        assertEquals("тесту", declensionResult.dative);
        assertEquals("тест", declensionResult.accusative);
        assertEquals("тестом", declensionResult.instrumental);
        assertEquals("тесте", declensionResult.prepositional);
        assertEquals("о тесте", declensionResult.prepositionalWithO);

        assertEquals("в тесте", declensionResult.where);
        assertEquals("в тест", declensionResult.to);
        assertEquals("из теста", declensionResult.from);

        assertEquals("тесты", declensionResult.plural.nominative);
        assertEquals("тестов", declensionResult.plural.genitive);
        assertEquals("тестам", declensionResult.plural.dative);
        assertEquals("тесты", declensionResult.plural.accusative);
        assertEquals("тестами", declensionResult.plural.instrumental);
        assertEquals("тестах", declensionResult.plural.prepositional);
        assertEquals("о тестах", declensionResult.plural.prepositionalWithO);

        assertEquals(Gender.Masculine, declensionResult.gender);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("тест", params.get("s"));

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void declension_SplitFio() throws MorpherException, IOException {
        communicator.writeNextResponse("{\n" +
                "  \"Р\": \"Александра Сергеевича Пушкина\",\n" +
                "  \"Д\": \"Александру Сергеевичу Пушкину\",\n" +
                "  \"В\": \"Александра Сергеевича Пушкина\",\n" +
                "  \"Т\": \"Александром Сергеевичем Пушкиным\",\n" +
                "  \"П\": \"Александре Сергеевиче Пушкине\",\n" +
                "  \"ФИО\": {\n" +
                "    \"Ф\": \"Пушкин\",\n" +
                "    \"И\": \"Александр\",\n" +
                "    \"О\": \"Сергеевич\"\n" +
                "  }\n" +
                "}");

        DeclensionResult declensionResult = russianClient.declension("Александр Сергеевич Пушкин");
        assertNotNull(declensionResult);
        assertNotNull(declensionResult.fullName);
        assertEquals("Пушкин", declensionResult.fullName.surname);
        assertEquals("Александр", declensionResult.fullName.name);
        assertEquals("Сергеевич", declensionResult.fullName.patronymic);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("Александр Сергеевич Пушкин", params.get("s"));
        assertEquals("https://ws3.morpher.ru/russian/declension", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void declension_nullGenitive() throws MorpherException, IOException {
        communicator.writeNextResponse("{\"Д\": \"теляти\",\"В\": \"теля\"}");

        DeclensionResult declensionResult = russianClient.declension("теля");
        assertNotNull(declensionResult);
        assertNull(declensionResult.genitive);
        assertEquals("теля", declensionResult.nominative);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("теля", params.get("s"));
        assertEquals("https://ws3.morpher.ru/russian/declension", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void spell_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("{\n" +
                "  \"n\": {\n" +
                "    \"И\": \"десять\",\n" +
                "    \"Р\": \"десяти\",\n" +
                "    \"Д\": \"десяти\",\n" +
                "    \"В\": \"десять\",\n" +
                "    \"Т\": \"десятью\",\n" +
                "    \"П\": \"десяти\"\n" +
                "  },\n" +
                "  \"unit\": {\n" +
                "    \"И\": \"рублей\",\n" +
                "    \"Р\": \"рублей\",\n" +
                "    \"Д\": \"рублям\",\n" +
                "    \"В\": \"рублей\",\n" +
                "    \"Т\": \"рублями\",\n" +
                "    \"П\": \"рублях\"\n" +
                "  }\n" +
                "}");

        NumberSpellingResult declensionResult = russianClient.spell(10, "рубль");
        assertNotNull(declensionResult);

        // number
        assertEquals("десять", declensionResult.numberDeclension.nominative);
        assertEquals("десяти", declensionResult.numberDeclension.genitive);
        assertEquals("десяти", declensionResult.numberDeclension.dative);
        assertEquals("десять", declensionResult.numberDeclension.accusative);
        assertEquals("десятью", declensionResult.numberDeclension.instrumental);
        assertEquals("десяти", declensionResult.numberDeclension.prepositional);


        // unit
        assertEquals("рублей", declensionResult.unitDeclension.nominative);
        assertEquals("рублей", declensionResult.unitDeclension.genitive);
        assertEquals("рублям", declensionResult.unitDeclension.dative);
        assertEquals("рублей", declensionResult.unitDeclension.accusative);
        assertEquals("рублями", declensionResult.unitDeclension.instrumental);
        assertEquals("рублях", declensionResult.unitDeclension.prepositional);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(2, params.size());
        assertEquals("10", params.get("n"));
        assertEquals("рубль", params.get("unit"));
        assertEquals("https://ws3.morpher.ru/russian/spell", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void adjectiveGenders_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("{\n" +
                "  \"feminine\": \"уважаемая\",\n" +
                "  \"neuter\": \"уважаемое\",\n" +
                "  \"plural\": \"уважаемые\"\n" +
                "}");

        AdjectiveGendersResult adjectiveGenders = russianClient.adjectiveGenders("уважаемый");
        assertNotNull(adjectiveGenders);

        assertEquals("уважаемая", adjectiveGenders.feminine);
        assertEquals("уважаемое", adjectiveGenders.neuter);
        assertEquals("уважаемые", adjectiveGenders.plural);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("уважаемый", params.get("s"));
        assertEquals("https://ws3.morpher.ru/russian/genders", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void adjectivize_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("[\n" +
                "  \"мытыщинский\",\n" +
                "  \"мытыщенский\"\n" +
                "]");

        List<String> adjList = russianClient.adjectivize("мытыщи");
        assertNotNull(adjList);
        assertEquals("мытыщинский", adjList.get(0));
        assertEquals("мытыщенский", adjList.get(1));
        assertEquals("https://ws3.morpher.ru/russian/adjectivize", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }

    @Test
    public void removeFromUserDictionary_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("true");

        boolean found = russianClient.removeFromUserDictionary("кошка");
        assertTrue(found);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(1, params.size());
        assertEquals("кошка", params.get("s"));
        assertEquals("https://ws3.morpher.ru/russian/userdict", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_DELETE, httpMethod);
    }

    @Test
    public void fetchAllFromUserDictionary_Success() throws MorpherException, IOException {
        communicator.writeNextResponse("[\n" +
                "    {\n" +
                "        \"singular\": {\n" +
                "            \"И\": \"Кошка\",\n" +
                "            \"Р\": \"Пантеры\",\n" +
                "            \"Д\": \"Пантере\",\n" +
                "            \"В\": \"Пантеру\",\n" +
                "            \"Т\": \"Пантерой\",\n" +
                "            \"П\": \"о Пантере\",\n" +
                "            \"М\": \"в Пантере\"\n" +
                "        },\n" +
                "        \"plural\": {\n" +
                "            \"И\": \"Пантеры\",\n" +
                "            \"Р\": \"Пантер\",\n" +
                "            \"Д\": \"Пантерам\",\n" +
                "            \"В\": \"Пантер\",\n" +
                "            \"Т\": \"Пантерами\",\n" +
                "            \"П\": \"о Пантерах\",\n" +
                "            \"М\": \"в Пантерах\"\n" +
                "        }\n" +
                "    }\n" +
                "]");

        List<CorrectionEntry> correctionEntries = russianClient.fetchAllFromUserDictionary();

        assertNotNull(correctionEntries);
        assertEquals(1, correctionEntries.size());
        CorrectionEntry entry = correctionEntries.get(0);

        assertEquals("Кошка", entry.singular.nominative);
        assertEquals("Пантеры", entry.singular.genitive);
        assertEquals("Пантере", entry.singular.dative);
        assertEquals("Пантеру", entry.singular.accusative);
        assertEquals("Пантерой", entry.singular.instrumental);
        assertEquals("о Пантере", entry.singular.prepositional);
        assertEquals("в Пантере", entry.singular.locative);

        assertEquals("Пантеры", entry.plural.nominative);
        assertEquals("Пантер", entry.plural.genitive);
        assertEquals("Пантерам", entry.plural.dative);
        assertEquals("Пантер", entry.plural.accusative);
        assertEquals("Пантерами", entry.plural.instrumental);
        assertEquals("о Пантерах", entry.plural.prepositional);
        assertEquals("в Пантерах", entry.plural.locative);

        Map<String, String> params = communicator.readLastParamsPassed();
        assertNotNull(params);
        assertEquals(0, params.size());
        assertEquals("https://ws3.morpher.ru/russian/userdict", communicator.readLastUrlPassed());

        String httpMethod = communicator.readLastHttpMethodPassed();
        assertEquals(METHOD_GET, httpMethod);
    }
}