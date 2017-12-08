import clients.russian.data.*;
import clients.russian.*;
import clients.ukrainian.*;
import ru.morpher.ws3.*;

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

        MorpherClient morpherClient = new MorpherClient.ClientBuilder()
                .useToken("a8dab5fe-7a47-4c17-84ea-46facb7d19fe")
                .useUrl("http://ws3.morpher.ru")
                .build();

        try {
            demo(morpherClient.russian());
            demo(morpherClient.ukrainian());

            log("Остаток запросов на сегодня: " + morpherClient.queriesLeftForToday());
            log("");

            log("Провоцируем ошибку");
            morpherClient.russian().declension("wuf");
        } catch (IOException e) {
            log("Ошибка коммуникации: " + e.getMessage());
        } catch (ArgumentNotRussianException e) {
            log("Ошибка аргументов неверного языка: " + e.getMessage());
        } catch (AccessDeniedException e) {
            log("Ошибка доступа: " + e.getMessage());
        } catch (NumeralsDeclensionNotSupportedException e) {
            log("Ошибка неверных числовых аргументов: " + e.getMessage());
        } catch (ArgumentEmptyException e) {
            log("Ошибка пустых аргументов: " + e.getMessage());
        } catch (InvalidFlagsException e) {
            log("Ошибка неверных флагов: " + e.getMessage());
        }
    }

    private static void demo(RussianClient russianClient) throws IOException, AccessDeniedException {
        russianDeclensionsAndGenderExample(russianClient);
        russianNamesDeclensionsExample(russianClient);
        russianAdjectivesGendersExample(russianClient);
        russianAdjectivizeExample(russianClient);
        russianSpellWithCorrectionExample(russianClient);
        russianNumberSpellingResult(russianClient);
    }

    private static void demo(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        ukrainianDeclensionsAndGenderExample(ukrainianClient);
        ukrainianNumberSpellingResult(ukrainianClient);
        ukrainianSpellWithCorrectionExample(ukrainianClient);
    }

    private static void russianSpellWithCorrectionExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        try {
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

            russianClient.addOrUpdateUserDict(correctionEntry);

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
                if (correction.plural != null) {
                    log("   М_Р: " + correction.plural.genitive);
                    log("   М_Д: " + correction.plural.dative);
                    log("   М_В: " + correction.plural.accusative);
                    log("   М_Т: " + correction.plural.instrumental);
                    log("   М_П: " + correction.plural.prepositional);
                    log("   М_М: " + correction.plural.locative);
                }
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
        catch(NumeralsDeclensionNotSupportedException e){
            log(e.getMessage());
        }
        catch(ArgumentNotRussianException e) {
            log(e.getMessage());
        }
        catch(InvalidFlagsException e) {
            log(e.getMessage());
        }
        catch(ArgumentEmptyException e) {
            log(e.getMessage());
        }
    }

    private static void russianAdjectivizeExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        log("Образование прилагательных:");
        List<String> adjectives = russianClient.adjectivize("Мытищи");
        for (String adjective : adjectives) {
            log(adjective);
        }

        log("");
    }

    private static void russianNumberSpellingResult(RussianClient russianClient) throws IOException, AccessDeniedException {
        log("Сумма прописью:");

        try {
            int number = 2513;
            NumberSpellingResult result = russianClient.spell(number, "рубль");
            log("В размере %s (%s) %s", number,
                    result.numberDeclension.genitive,
                    result.unitDeclension.genitive);
        }
        catch(ArgumentNotRussianException e) {
            throw new RuntimeException("Морфер не считает слово рубль русским.", e);
        }
        catch(ArgumentEmptyException e) {
            throw new RuntimeException("Морфер считает слово рубль пустой строкой.", e);
        }
    }

    private static void russianAdjectivesGendersExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        log("Склонение прилагательных по родам:");

        AdjectiveGendersResult adjectiveGenders = russianClient.adjectiveGenders("уважаемый");
        log("Женский род:         " + adjectiveGenders.feminine);
        log("Средний род:         " + adjectiveGenders.neuter);
        log("Множественное число: " + adjectiveGenders.plural);
        log("");
    }

    private static void russianDeclensionsAndGenderExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        log("Склонение на русском языке:");

        try {
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
        catch(NumeralsDeclensionNotSupportedException e){
            // Во входном словосочетании было числительное, например "три кота".
            // Пока Морфер не умеет их склонять.
            log(e.getMessage());
        }
        catch(ArgumentNotRussianException e) {
            // Во входном словосочетании не нашлось русских слов, например "Vasily Pupkin".
            // Решить проблему можно, например, добавлением родового слова.
            // Например, если вы склоняете имена пользователей, то добавьте слово пользователь:
            // и просклоняйте словосочетание "пользователь Vasily Pupkin".
            log(e.getMessage());
        }
        catch(InvalidFlagsException e) {
            log(e.getMessage());
        }
        catch(ArgumentEmptyException e) {
            log(e.getMessage());
        }
    }

    private static void russianNamesDeclensionsExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        try {
            log("Разделение ФИО на части:");

            DeclensionResult nameResult = russianClient.declension("Полад Бюльбюль-оглы Мамедов");
            log("Ф: " + nameResult.fullName.surname);
            log("И: " + nameResult.fullName.name);
            log("О: " + nameResult.fullName.patronymic);
            log("");
        }
        catch(NumeralsDeclensionNotSupportedException e){
            log(e.getMessage());
        }
        catch(ArgumentNotRussianException e) {
            log(e.getMessage());
        }
        catch(InvalidFlagsException e) {
            log(e.getMessage());
        }
        catch(ArgumentEmptyException e) {
            log(e.getMessage());
        }
    }

    private static void ukrainianSpellWithCorrectionExample(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        // Функции пользовательского словаря для ws3.morpher.ru работают только при наличии токена.
        // Для local сервиса токен не нужен.

        // Украинский язык
        // Добавляем новое пользовательское исправление
        clients.ukrainian.data.CorrectionForms singular = new clients.ukrainian.data.CorrectionForms();
        singular.nominative = "Сергій";
        singular.prepositional = "Сергієві";

        clients.ukrainian.data.CorrectionEntry correctionEntry = new clients.ukrainian.data.CorrectionEntry();
        correctionEntry.singular = singular;

        ukrainianClient.addOrUpdateToUserDict(correctionEntry);

        log("Склонение с исправлением:");
        clients.ukrainian.data.DeclensionResult spellWithCorrection = ukrainianClient.declension("Сергій");
        log("Називний вiдмiнок: %s", spellWithCorrection.nominative);
        log("Мiсцевий вiдмiнок: %s", spellWithCorrection.prepositional);
        log("");

        log("Получаем список всех исправлений:");
        List<clients.ukrainian.data.CorrectionEntry> corrections = ukrainianClient.fetchAllFromUserDictionary();
        for (clients.ukrainian.data.CorrectionEntry correction : corrections) {
            log(correction.singular.nominative + ":");
            log("   Р: " + correction.singular.genitive);
            log("   Д: " + correction.singular.dative);
            log("   З: " + correction.singular.accusative);
            log("   О: " + correction.singular.instrumental);
            log("   М: " + correction.singular.prepositional);
            log("   К: " + correction.singular.vocative);
            if (correction.plural != null) {
                log("   М_Р: " + correction.plural.genitive);
                log("   М_Д: " + correction.plural.dative);
                log("   М_З: " + correction.plural.accusative);
                log("   М_О: " + correction.plural.instrumental);
                log("   М_М: " + correction.plural.prepositional);
                log("   М_К: " + correction.plural.vocative);
            }
        }
        log("");

        // Удаляем исправление.
        // True если исправление было удалено успешно, false если исправление не найдено.
        boolean found = ukrainianClient.removeFromUserDictionary("Сергій");
        log("Исправление найдено: %s", found ? "Да" : "Нет");

        log("Склонение после удаления исправления:");
        clients.ukrainian.data.DeclensionResult spellWithoutCorrection = ukrainianClient.declension("Сергій");
        log("Називний вiдмiнок: %s", spellWithoutCorrection.nominative);
        log("Мiсцевий вiдмiнок: %s", spellWithoutCorrection.prepositional);
        log("");
    }

    private static void ukrainianNumberSpellingResult(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        log("Сумма прописью на укранинском:");
        int number = 2513;
        clients.ukrainian.data.NumberSpellingResult ukrainianNumberSpellingResult = ukrainianClient.spell(number, "рубль");
        log("У розмірі %s (%s) %s", number,
                ukrainianNumberSpellingResult.numberDeclension.genitive,
                ukrainianNumberSpellingResult.unitDeclension.genitive);
        log("");
    }

    private static void ukrainianDeclensionsAndGenderExample(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        log("Склонение ФИО на украинском языке:");
        clients.ukrainian.data.DeclensionResult ukrainianDeclensionResult = ukrainianClient.declension("Тест");
        log(" Називний вiдмiнок: " + ukrainianDeclensionResult.nominative);
        log("  Родовий вiдмiнок: " + ukrainianDeclensionResult.genitive);
        log("Давальний вiдмiнок: " + ukrainianDeclensionResult.dative);
        log("Знахідний вiдмiнок: " + ukrainianDeclensionResult.accusative);
        log("  Орудний вiдмiнок: " + ukrainianDeclensionResult.instrumental);
        log(" Місцевий вiдмiнок: " + ukrainianDeclensionResult.prepositional);
        log("  Кличний вiдмiнок: " + ukrainianDeclensionResult.vocative);
        log("");

        log("Определение рода на украинском языке:");
        log("Род: %s", ukrainianDeclensionResult.gender != null ? ukrainianDeclensionResult.gender : PREMIUM);
        log("");
    }

    private static void log(String message) {
        System.out.println(message);
    }

    private static void log(String messageWithPlaceholders, Object... params) {
        System.out.println(format(messageWithPlaceholders, params));
    }
}
