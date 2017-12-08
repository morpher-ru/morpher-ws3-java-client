import ru.morpher.ws3.*;
import ru.morpher.ws3.russian.*;
import ru.morpher.ws3.russian.data.*;

import java.io.IOException;
import java.util.List;

class RussianDemo extends Log
{
   static void demo(RussianClient russianClient) throws IOException, AccessDeniedException {
        russianDeclensionsAndGenderExample(russianClient);
        russianNamesDeclensionsExample(russianClient);
        russianAdjectivesGendersExample(russianClient);
        russianAdjectivizeExample(russianClient);
        russianSpellWithCorrectionExample(russianClient);
        russianNumberSpellingResult(russianClient);
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
        } catch (NumeralsDeclensionNotSupportedException e) {
            log(e.getMessage());
        } catch (ArgumentNotRussianException e) {
            log(e.getMessage());
        } catch (InvalidFlagsException e) {
            log(e.getMessage());
        } catch (ArgumentEmptyException e) {
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
        } catch (ArgumentNotRussianException e) {
            throw new RuntimeException("Морфер не считает слово рубль русским.", e);
        } catch (ArgumentEmptyException e) {
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
            log("Провоцируем ошибку");
            russianClient.declension("wuf");
        } catch (NumeralsDeclensionNotSupportedException e) {
            // Во входном словосочетании было числительное, например "три кота".
            // Пока Морфер не умеет их склонять.
            log(e.getMessage());
        } catch (ArgumentNotRussianException e) {
            // Во входном словосочетании не нашлось русских слов, например "Vasily Pupkin".
            // Решить проблему можно, например, добавлением родового слова.
            // Например, если вы склоняете имена пользователей, то добавьте слово пользователь:
            // и просклоняйте словосочетание "пользователь Vasily Pupkin".
            log(e.getMessage());
        } catch (InvalidFlagsException e) {
            log(e.getMessage());
        } catch (ArgumentEmptyException e) {
            log(e.getMessage());
        }
        log("");
    }

    private static void russianNamesDeclensionsExample(RussianClient russianClient) throws IOException, AccessDeniedException {
        try {
            log("Разделение ФИО на части:");

            DeclensionResult nameResult = russianClient.declension("Полад Бюльбюль-оглы Мамедов");
            log("Ф: " + nameResult.fullName.surname);
            log("И: " + nameResult.fullName.name);
            log("О: " + nameResult.fullName.patronymic);
            log("");
        } catch (NumeralsDeclensionNotSupportedException e) {
            log(e.getMessage());
        } catch (ArgumentNotRussianException e) {
            log(e.getMessage());
        } catch (InvalidFlagsException e) {
            log(e.getMessage());
        } catch (ArgumentEmptyException e) {
            log(e.getMessage());
        }
    }
}
