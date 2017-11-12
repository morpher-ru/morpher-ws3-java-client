import clients.russian.RussianClient;
import clients.russian.data.AdjectiveGendersResult;
import clients.russian.data.CorrectionEntry;
import clients.russian.data.CorrectionForms;
import clients.russian.data.DeclensionResult;
import clients.russian.data.NumberSpellingResult;
import exceptions.MorpherException;

import java.io.IOException;
import java.util.List;

import static java.lang.String.format;

public class UsageExample {

    private static final String PREMIUM = "*****";

    public static void main(String[] argv) {
        // Вы можете передать токен в качестве аргумента конструктора.
        // String token = "17ce56c3-934f-453a-9ef7-cc1feec4e344";
        // !!! Не используйте этот токен в production !!!
        //
        // Если вы используете "Морфер.Сервер" (http://morpher.ru/webservice/local/),
        // то вы можете указать в качестве url адрес вашего локального сервера:
        // String url = "http://ws3.morpher.ru"
        //
        // MorpherClient morpherClient = new MorpherClient(token, url);
        // Для удобства можно использовать встроенный билдер:

        MorpherClient morpherClient = MorpherClient.createNewClient()
                .useToken("a8dab5fe-7a47-4c17-84ea-46facb7d19fe")
                .useUrl("http://ws3.morpher.ru")
                .build();

        try {
            runRussianClientExamples(morpherClient);

        } catch (MorpherException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void runRussianClientExamples(MorpherClient morpherClient) throws MorpherException, IOException {
        RussianClient russianClient = morpherClient.russian();

        russianDeclensionsAndGenderExample(russianClient);
        russianNamesDeclensionsExample(russianClient);
        russianAdjectivesGendersExample(russianClient);
        russianAdjectivizeExample(russianClient);
        russianSpellWithCorrectionExample(russianClient);
        russianNumberSpellingResult(russianClient);
    }

    private static void russianSpellWithCorrectionExample(RussianClient russianClient) throws MorpherException, IOException {
        // Функции пользовательского словаря для ws3.morpher.ru работают только при наличии токена.
        // Для local сервиса токен не нужен.

        // Русский язык
        // Добавляем новое пользовательское исправление
        CorrectionForms singular = new CorrectionForms();
        singular.nominative = "Кошка";
        singular.dative = "Пантере";

        CorrectionForms plural = new CorrectionForms();
        plural.dative = "Пантерам";

        CorrectionEntry correctionEntry = new CorrectionEntry();
        correctionEntry.singular = singular;
        correctionEntry.plural = plural;

        russianClient.addOrUpdateToUserDict(correctionEntry);

        log("Склонение с исправлением:");
        DeclensionResult declensionWithCorrection = russianClient.declension("Кошка");
        log("            Именительный падеж: %s", declensionWithCorrection.nominative);
        log("               Дательный падеж: %s", declensionWithCorrection.dative);
        log("Дательный падеж множественного: %s", declensionWithCorrection.plural.dative);
        log("");

        log("Получаем список всех исправлений:");
        List<CorrectionEntry> corrections = russianClient.fetchAllFromUserDictionary();
        for (CorrectionEntry correction : corrections) {
            log(correction.singular.nominative + ":");
            log("   Р: " + correction.singular.genitive);
            log("   Д: " + correction.singular.dative);
            log("   В: " + correction.singular.accusative);
            log("   Т: " + correction.singular.instrumental);
            log("   П: " + correction.singular.prepositional);
            log("   М: " + correction.singular.locative);
            log("   М_Р: " + correction.plural.genitive);
            log("   М_Д: " + correction.plural.dative);
            log("   М_В: " + correction.plural.accusative);
            log("   М_Т: " + correction.plural.instrumental);
            log("   М_П: " + correction.plural.prepositional);
            log("   М_М: " + correction.plural.locative);
        }
        log("");

        log("Удаляем исправление.");
        // True если исправление было удалено успешно, false если исправление не найдено.
        boolean found = russianClient.removeFromUserDictionary("Кошка");
        log("Исправление найдено: %s", found ? "Да" : "Нет");

        log("Склонение после удаления исправления:");
        DeclensionResult spellWithoutCorrection = russianClient.declension("Кошка");
        log("            Именительный падеж: %s", spellWithoutCorrection.nominative);
        log("               Дательный падеж: %s", spellWithoutCorrection.dative);
        log("Дательный падеж множественного: %s", spellWithoutCorrection.plural.dative);
        log("");

    }

    private static void russianAdjectivizeExample(RussianClient russianClient) throws MorpherException, IOException {
        log("Образование прилагательных:");
        List<String> adjectives = russianClient.adjectivize("Мытищи");
        for (String adjective : adjectives) {
            log(adjective);
        }

        log("");
    }

    private static void russianNumberSpellingResult(RussianClient russianClient) throws MorpherException, IOException {
        log("Сумма прописью:");

        int number = 2513;
        NumberSpellingResult result = russianClient.spell(number, "рубль");
        log("В размере %s (%s) %s", number,
                result.numberDeclension.genitive,
                result.unitDeclension.genitive);
    }

    private static void russianAdjectivesGendersExample(RussianClient russianClient) throws MorpherException, IOException {
        log("Склонение прилагательных по родам:");

        AdjectiveGendersResult adjectiveGenders = russianClient.adjectiveGenders("уважаемый");
        log("Женский род:         " + adjectiveGenders.feminine);
        log("Средний род:         " + adjectiveGenders.neuter);
        log("Множественное число: " + adjectiveGenders.plural);
        log("");
    }

    private static void russianDeclensionsAndGenderExample(RussianClient russianClient) throws MorpherException, IOException {
        log("Склонение на русском языке:");

        DeclensionResult result = russianClient.declension("Соединенное королевство");
        log("Именительный падеж: %s", result.nominative);
        log(" Родительный падеж: %s", result.genitive);
        log("   Дательный падеж: %s", result.dative);
        log(" Винительный падеж: %s", result.accusative);
        log("Творительный падеж: %s", result.instrumental);
        log("  Предложный падеж: %s", result.prepositional);
        log("     Местный падеж: %s", result.prepositionalWithO != null ? result.prepositionalWithO : PREMIUM);
        log("               Где? %s", result.where != null ? result.where : PREMIUM);
        log("              Куда? %s", result.to != null ? result.to : PREMIUM);
        log("            Откуда? %s", result.from != null ? result.from : PREMIUM);

        if (result.plural != null) {
            log("Именительный падеж: %s", result.plural.nominative);
            log(" Родительный падеж: %s", result.plural.genitive);
            log("   Дательный падеж: %s", result.plural.dative);
            log(" Винительный падеж: %s", result.plural.accusative);
            log("Творительный падеж: %s", result.plural.instrumental);
            log("  Предложный падеж: %s", result.plural.prepositional);
            log("     Местный падеж: %s", result.prepositionalWithO != null ? result.prepositionalWithO : PREMIUM);
        }
        log("");

        log("Определение рода на русском языке:");
        log("Род: %s", result.gender != null ? result.gender : PREMIUM);
        log("");
    }

    private static void russianNamesDeclensionsExample(RussianClient russianClient) throws MorpherException, IOException {
        log("Разделение ФИО на части:");

        DeclensionResult nameResult = russianClient.declension("Полад Бюльбюль-оглы Мамедов");
        log("Ф: " + nameResult.fullName.surname);
        log("И: " + nameResult.fullName.name);
        log("О: " + nameResult.fullName.patronymic);
        log("");
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void log(String messageWithPlaceholders, Object... params) {
        System.out.println(format(messageWithPlaceholders, params));
    }
}
