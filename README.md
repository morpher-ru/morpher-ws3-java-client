[![Build status](https://ci.appveyor.com/api/projects/status/jbktpg01dnrrd05r?svg=true)](https://ci.appveyor.com/project/morpher/morpher-ws3-java-client)

# morpher-ws3-java-client
Java-клиент веб-сервиса ["Морфер" 3.0](http://morpher.ru/ws3)
***

Если вам удобнее учиться на примерах, мы разработали для вас рабочий пример с демонстрацией всех функций веб-сервиса:

 * https://github.com/morpher-ru/morpher-ws3-java-client-sample

Этот пример [подключает](https://github.com/morpher-ru/morpher-ws3-java-client-sample/blob/master/pom.xml) данную библиотеку через Maven и вызывает функции веб-сервиса для [русского](https://github.com/morpher-ru/morpher-ws3-java-client-sample/blob/master/src/main/java/ru/morpher/ws3/client/sample/RussianDemo.java) и [украинского](https://github.com/morpher-ru/morpher-ws3-java-client-sample/blob/master/src/main/java/ru/morpher/ws3/client/sample/UkrainianDemo.java) языков.

## Использование

Работа с веб-сервисом осуществляется за счет основного класса `ru.morpher.ws3.Client`, который реализует все функции веб-сервиса.  

### Начало работы
Чтобы создать стандартный (без параметров) экземпляр класса `Client`, достаточно вызвать `ClientBuilder.build()`:
```java
import ru.morpher.ws3.*;

Client client = new ClientBuilder().build();
```
Однако для полноценной работы с веб-сервисом крайне рекомендуется [зарегистрироваться](http://morpher.ru/Register.aspx) и получить token, который потом передается в ClientBuilder:

```java
Client client = new ClientBuilder().useToken("my-token").build();
```
Отлично! Теперь веб-сервис вас ни с кем не спутает, случайно не заблокирует и у вас будет свой собственный счетчик запросов.

### Выполнение запросов

#### Русский язык
##### Склонение
```java
DeclensionResult russianDeclensionResult = client.getRussian().declension("ёлка");
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
SpellingResult russianSpellingResult = client.getRussian().spell(123, "ёлка");
String numberNominativeCase = russianSpellingResult.getNumber().getNominativeCase();//сто двадцать три
String unitNominativeCase = russianSpellingResult.getUnit().getNominativeCase();    //ёлки
String nominativeCase = russianSpellingResult.getNominativeCase();                  //Сто двадцать три ёлки
```
Склонение по падежам аналогично функции declension (см. выше), т.е. `.getGenitiveCase()`, `.getDativeCase()` и т.д.
***
##### Склонение прилагательных по родам
```java
AdjectiveGenders russianAdjectiveGenders = client.getRussian().adjectiveGenders("ёлочный");
String feminineGender = russianAdjectiveGenders.getFeminine();//Женский род
String neuterGender = russianAdjectiveGenders.getNeuter();    //Средний род
String plural = russianAdjectiveGenders.getPlural();          //Множественное число
```
***
##### Образование прилагательных:
```java
List<String> adjectives = client.getRussian().adjectivize("Мытищи");
```
***
#### Украинский язык
##### Склонение
```java
DeclensionResult ukrainianDeclensionResult = client.getUkrainian().declension("ялинка");
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
SpellingResult ukrainianSpellingResult = client.getUkrainian().spell(123, "ялинка");
String numberNominativeCase = ukrainianSpellingResult.getNumber().getNominativeCase();  //сто двадцять три 
String unitNominativeCase = ukrainianSpellingResult.getUnit().getNominativeCase();      //ялинки
String nominativeCase = ukrainianSpellingResult.getNominativeCase();                    //Сто двадцять три ялинки
```
Склонение по падежам аналогично функции declension (см. выше), т.е. `.getGenitiveCase()`, `.getDativeCase()` и т.д.
***

## Сборка и зависимости
Для использования библиотеки в своем проекте рекомендуется подключить ее через Maven:

Добавьте в файл pom.xml репозиторий, который содержит библиотеку:

```xml
    <repositories>
        <repository>
            <id>morpher-ws3-java-client-mvn-repo</id>
            <url>https://raw.github.com/morpher-ru/morpher-ws3-java-client/mvn-repo</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
```

Добавить зависимость на клиент:

```xml
     <dependencies>
         <dependency>
             <groupId>ru.morpher</groupId>
             <artifactId>ws3.client</artifactId>
             <version>1.0-SNAPSHOT</version>
             <scope>compile</scope>
         </dependency>
     </dependencies>
```
[1] http://morpher.ru/ws3#premium
