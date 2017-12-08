import ru.morpher.ws3.*;
import ru.morpher.ws3.ukrainian.*;

import java.io.IOException;
import java.util.List;

class UkrainianDemo extends Log
{
    static void demo(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        declensionsAndGenderExample(ukrainianClient);
        numberSpellingExample(ukrainianClient);
        spellWithCorrectionExample(ukrainianClient);
    }

    private static void spellWithCorrectionExample(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        // Функции пользовательского словаря для ws3.morpher.ru работают только при наличии токена.
        // Для local сервиса токен не нужен.

        // Украинский язык
        // Добавляем новое пользовательское исправление
        CorrectionForms singular = new CorrectionForms();
        singular.nominative = "Сергій";
        singular.prepositional = "Сергієві";

        CorrectionEntry correctionEntry = new CorrectionEntry();
        correctionEntry.singular = singular;

        ukrainianClient.addOrUpdateToUserDict(correctionEntry);

        log("Склонение с исправлением:");
        DeclensionResult spellWithCorrection = ukrainianClient.declension("Сергій");
        log("Називний вiдмiнок: %s", spellWithCorrection.nominative);
        log("Мiсцевий вiдмiнок: %s", spellWithCorrection.prepositional);
        log("");

        log("Получаем список всех исправлений:");
        List<CorrectionEntry> corrections = ukrainianClient.fetchAllFromUserDictionary();
        for (CorrectionEntry correction : corrections) {
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
        DeclensionResult spellWithoutCorrection = ukrainianClient.declension("Сергій");
        log("Називний вiдмiнок: %s", spellWithoutCorrection.nominative);
        log("Мiсцевий вiдмiнок: %s", spellWithoutCorrection.prepositional);
        log("");
    }

    private static void numberSpellingExample(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        log("Сумма прописью на укранинском:");
        int number = 2513;
        NumberSpellingResult numberSpellingResult = ukrainianClient.spell(number, "рубль");
        log("У розмірі %s (%s) %s", number,
                numberSpellingResult.numberDeclension.genitive,
                numberSpellingResult.unitDeclension.genitive);
        log("");
    }

    private static void declensionsAndGenderExample(UkrainianClient ukrainianClient) throws IOException, AccessDeniedException {
        log("Склонение ФИО на украинском языке:");
        DeclensionResult declensionResult = ukrainianClient.declension("Тест");
        log(" Називний вiдмiнок: " + declensionResult.nominative);
        log("  Родовий вiдмiнок: " + declensionResult.genitive);
        log("Давальний вiдмiнок: " + declensionResult.dative);
        log("Знахідний вiдмiнок: " + declensionResult.accusative);
        log("  Орудний вiдмiнок: " + declensionResult.instrumental);
        log(" Місцевий вiдмiнок: " + declensionResult.prepositional);
        log("  Кличний вiдмiнок: " + declensionResult.vocative);
        log("");

        log("Определение рода на украинском языке:");
        log("Род: %s", declensionResult.gender != null ? declensionResult.gender : PREMIUM);
        log("");
    }
}
