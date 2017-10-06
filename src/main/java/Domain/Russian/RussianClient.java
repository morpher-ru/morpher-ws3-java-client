package Domain.Russian;

import Exceptions.MorpherException;
import jdk.nashorn.internal.objects.annotations.Where;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;

/**
 * Created by Kraken on 26.09.2017.
 */
public class RussianClient {

    private Retrofit retrofit;
    private static RussianApi russianApi;
    private String token;

    public RussianClient(String token) {
        this.token = token;

        retrofit = new Retrofit.Builder()
                .baseUrl("http://ws3.morpher.ru/")
                .build();
        russianApi = retrofit.create(RussianApi.class);
    }

    /**
     * Публичные методы
     */

    public DeclensionResult declension(String word) throws MorpherException {
        DeclensionResult result = new DeclensionResult();
        word = word.replaceAll("<|>","");

        Response<ResponseBody> response;
        try {
            if (token == null) {
                response = russianApi.getDeclension(word).execute();
            } else {
                response = russianApi.getDeclension(token, word).execute();
            }
            //Проверка заголовка
            checkErrors(response.raw().toString());
            result.nominativeCase = word;
            String stringToParse = response.body().string();
            getDeclensionParse(result, stringToParse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Приватные методы
     */

    private void checkErrors(String stringToParse) throws MorpherException {
        //Здесь будем обрабатывать ошибку
        int code = Integer.parseInt(
                stringToParse.substring(
                        stringToParse.indexOf("code")+5,
                        stringToParse.indexOf("code")+8));//Integer.parseInt(stringToParse.substring(stringToParse.indexOf("<code>") + 6, stringToParse.indexOf("</code>")));
        switch (code) {
            case 402:
                throw new MorpherException("Превышен лимит на количество запросов");
            case 403:
                throw new MorpherException("IP-адрес заблокирован");
            case 495:
                throw new MorpherException("Для склонения числительных используйте метод spell");
            case 496:
                throw new MorpherException("Не найдено русских слов");
            case 400:
                throw new MorpherException("Передана пустая строка");
            //case 402:
            //    throw new MorpherException("Необходимо оплатить услугу");
            case 498:
                throw new MorpherException("Переданный токен не найден");
            case 497:
                throw new MorpherException("Неверный формат токена");
        }
    }

    private void getDeclensionParse(DeclensionResult result, String stringToParse) {

        //Если просто склонение
        if (stringToParse.contains("<множественное>")) {
            //Ед. число, род
            String declensionString = stringToParse.substring(0, stringToParse.indexOf("<множественное>"));
            //Мн. число, где\куда\откуда
            String pluralDeclensionString = stringToParse.substring(stringToParse.indexOf("<множественное>") + 15);

            singularDeclension(declensionString, result);
            pluralDeclension(pluralDeclensionString,result);

            //Обработка платных данных, если есть к ним доступ
            if (token != null) {
                paidData(declensionString, pluralDeclensionString, result);
            }
        }
        else {
            //Если ФИО передали
            String fullNameString = stringToParse.substring(stringToParse.indexOf("<ФИО>"));
            singularDeclension(stringToParse, result);
            fullName(fullNameString, result);
        }


    }

    private void singularDeclension(String declensionString, DeclensionResult result) {
        result.genitiveCase = getParameter(declensionString, ConstantManager.genitiveCase);
        result.dativeCase = getParameter(declensionString, ConstantManager.dativeCase);
        result.accusativeCase = getParameter(declensionString, ConstantManager.accusativeCase);
        result.instrumentalCase = getParameter(declensionString, ConstantManager.instrumentalCase);
        result.prepositionalCase = getParameter(declensionString, ConstantManager.prepositionalCase);
    }

    private void pluralDeclension(String pluralDeclensionString, DeclensionResult result) {
        result.pluralNominativeCase = getParameter(pluralDeclensionString, ConstantManager.nominativeCase);
        result.pluralGenitiveCase = getParameter(pluralDeclensionString, ConstantManager.genitiveCase);
        result.pluralDativeCase = getParameter(pluralDeclensionString, ConstantManager.dativeCase);
        result.pluralAccusativeCase = getParameter(pluralDeclensionString, ConstantManager.accusativeCase);
        result.pluralInstrumentalCase = getParameter(pluralDeclensionString, ConstantManager.instrumentalCase);
        result.pluralPrepositionalCase = getParameter(pluralDeclensionString, ConstantManager.prepositionalCase);
    }

    private void fullName(String str, DeclensionResult result) {
        result.name = getParameter(str, ConstantManager.name);
        result.givenName = getParameter(str, ConstantManager.givenName);
        result.patronymic = getParameter(str, ConstantManager.patronymic);
    }

    /**
     * Извлечение данных, предоставляемых платной версией сервиса
     * @param gender - строка, содержащая результат пола
     * @param WhereToFrom - строка содержащая результат "где", "куда", "откуда"
     * @param result - структура ответа сервера
     */
    private void paidData(String gender, String WhereToFrom, DeclensionResult result) {
        result.gender = getParameter(gender, ConstantManager.gender);
        result.where = getParameter(WhereToFrom, ConstantManager.where);
        result.to = getParameter(WhereToFrom, ConstantManager.to);
        result.from = getParameter(WhereToFrom, ConstantManager.from);
    }

    private String getParameter(String str, String CASE) {
        int from = str.indexOf("<" + CASE + ">") + CASE.length() + 2;
        str = str.substring(from, str.length());
        int to = str.indexOf("</" + CASE + ">");
        return str.substring(0, to);
    }
}
