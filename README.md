[![](https://travis-ci.org/morlok1/morpher-ws3-java-client.svg?branch=master)](https://travis-ci.org/morlok1/morpher-ws3-java-client)
# morpher-ws3-java-client
Java-клиент веб-сервиса ["Морфер" 3.0](http://morpher.ru/ws3)
***
## Использование
Работа с веб-сервисом осуществляется за счет основного класса MorpherClient, который реализует все функции веб-сервиса.  
Результат выполнения запросов возвращается как объект класса соответствующей модели данных.  

### Начало работы
Перед использованием веб-сервиса требуется создать экземпляр класса MorpherClient:  
```java
morpherClient = new MorpherClient();
```
В случае использования [платной](1) версии веб-сервиса Морфер в конструктор передается access-token:
```java
final String ACCESS_TOKEN = "Some token";
morpherClient = new MorpherClient(ACCESS_TOKEN);
```
### Выполнение запросов

#### Русский язык
##### Склонение
```java
// Формат вызова:
// DeclensionResult russianDeclensionResult = morpherClient.getRussian().declension(<текст>);
DeclensionResult russianDeclensionResult = morpherClient.getRussian().declension("ёлка");
String nominativeCase = russianDeclensionResult.getNominativeCase();
// Для других падежей:
// .getGenitiveCase()           - Родительный падеж
// .getDativeCase()             - Дательный падеж
// .getAccusativeCase()         - Винительный падеж
// .getInstrumentalCase()       - Творительный падеж
// .getPrepositionalCase()      - Предложный падеж

if (russianDeclensionResult.getPlural() != null) {
    String pluralNominativeCase = russianDeclensionResult.getPlural().NominativeCase();
    // Для других падежей - аналогично единственному числу.
}
```
Также есть возможность получения дополнительной информации [(платно)](1):
```java
// .getPrepositionalCaseWithO()         - Предложный падеж с предлогом О/ОБ/ОБО
// .getWhere()                          - Предложный падеж с предлогом В/НА, отвечает на вопрос Где?
// .getTo()                             - Куда?
// .getFrom()                           - Откуда?
```
***
##### Определение рода [(платно)](1):
```java
String gender = russianDeclensionResult.getGender();
```
***
##### Разделение ФИО на части:
```java
Name name = russianDeclensionResult.getName();
if (name != null) { // Это ФИО
    String surname = name.getSurname();        //Фамилия
    String givenName = name.getGivenName();    //Имя
    String patronymic = name.getPatronymic();  //Отчество
}
```
***
##### Cумма прописью:
```java
//  Формат вызова:
//  SpellingResult russianSpellingResult = morpherClient.getRussian().spell(<число>, <единица измерения>);
SpellingResult russianSpellingResult = morpherClient.getRussian().spell(123, "ёлка");
String numberNominativeCase = russianSpellingResult.getNumber().getNominativeCase();//сто двадцать три
String unitNominativeCase = russianSpellingResult.getUnit().getNominativeCase();    //ёлки
String nominativeCase = russianSpellingResult.getNominativeCase();                  //Сто двадцать три ёлки
```
Склонение по падежам аналогично функции declension (см. выше), т.е. `.getGenitiveCase()`, `.getDativeCase()` и т.д.
***
##### Склонение прилагательных по родам
```java
// Формат вызова:
// AdjectiveGenders russianAdjectiveGenders = morpherClient.getRussian().adjectiveGenders(<прилагательное>);
AdjectiveGenders russianAdjectiveGenders = morpherClient.getRussian().adjectiveGenders("ёлочный");
String feminineGender = russianAdjectiveGenders.getFeminine();//Женский род
String neuterGender = russianAdjectiveGenders.getNeuter();  //Средний род
String plural = russianAdjectiveGenders.getPlural();        //Множественное число
```
***
##### Образование прилагательных:
```java
// Формат вызова:
// List<String> adjectives = morpherClient.getRussian().adjectivize(<слово>);
List<String> adjectives = morpherClient.getRussian().adjectivize("Мытищи");
```
***
#### Украинский язык
##### Склонение
```java
// Формат вызова:
// DeclensionResult ukrainianDeclensionResult = morpherClient.getUkrainian().declension(<текст>);
DeclensionResult ukrainianDeclensionResult = morpherClient.getUkrainian().declension("ялинка");
String nominativeCase = ukrainianDeclensionResult.getNominativeCase();
// Для других падежей
// .getGenitiveCase()       - родовий відмінок
// .getDativeCase()         - давальний відмінок
// .getAccusativeCase()     - знахідний відмінок
// .getInstrumentalCase()   - орудний відмінок
// .getLocativeCase()       - місцевий відмінок
// .getVocativeCase()       - кличний відмінок
```
***
##### Определение рода [(платно)](1):
```java
String gender = ukrainianDeclensionResult.getGender();
```
***
##### Сумма прописью
```java
// Формат вызова:
// SpellingResult ukrainianSpellingResult = morpherClient.getUkrainian().spell(<число>,<одиниця виміру>);
SpellingResult ukrainianSpellingResult = morpherClient.getUkrainian().spell(123, "ялинка");
String numberNominativeCase = ukrainianSpellingResult.getNumber().getNominativeCase();  //сто двадцять три 
String unitNominativeCase = ukrainianSpellingResult.getUnit().getNominativeCase();      //ялинки
String nominativeCase = ukrainianSpellingResult.getNominativeCase();                    //Сто двадцять три ялинки
```
Склонение по падежам аналогично функции declension (см. выше), т.е. `.getGenitiveCase()`, `.getDativeCase()` и т.д.
***
### Обработка исключений
В процессе работы с библиотекой может быть сгенерировано исключение MorpherWebServiceException по следующим причинам:  
* LimitExceededException - Превышен лимит на количество запросов в сутки.
* IpBlockedException - IP заблокирован.
* WrongMethodException - Склонение числительных в declension не поддерживается. Используйте метод spell.
* NoRussianWordsException - Не найдено русских слов.
* EmptyStringException - В функцию передана пустая строка.
* UnpaidServiceException - Необходимо оплатить услугу.
* TokenNotFoundException - Данный token не найден.
* WrongTokenFormatException - Неверный формат токена.
***
## Сборка
Позднее здесь будет раздел, содержащий способ подключения библиотеки в *gradle* и *maven*.

[1] http://morpher.ru/ws3#premium
