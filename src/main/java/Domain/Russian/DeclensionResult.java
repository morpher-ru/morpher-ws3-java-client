package Domain.Russian;

import lombok.Getter;

/**
 * Created by Kraken on 25.09.2017.
 *
 * Инкапсуляция ответа сервера по запросу на русском
 */

public class DeclensionResult {

    //Падежи в единственном числе
    public String nominativeCase;
    public String genitiveCase;
    public String dativeCase;
    public String accusativeCase;
    public String instrumentalCase;
    public String prepositionalCase;
    public String prepositionalCaseWithO;

    //Падежи в множественном числе
    public String pluralNominativeCase;
    public String pluralGenitiveCase;
    public String pluralDativeCase;
    public String pluralAccusativeCase;
    public String pluralInstrumentalCase;
    public String pluralPrepositionalCase;
    public String pluralPrepositionalCaseWithO;

    //Род
    public String gender;

    //Где
    public String where;
    //Куда
    public String to;
    //Откуда
    public String from;

    //ФИО
    public String name;
    public String givenName;
    public String patronymic;
}
