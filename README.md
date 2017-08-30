# morpher-ws3-java-client
Java-клиент веб-сервиса ["Морфер" 3.0](http://morpher.ru/ws3)
***
## Сборка
Позднее здесь будет раздел, содержащий способ подключения библиотеки в *gradle* и *maven*.
***
## Использование
Работа с web-сервисом осуществляется за счет основного класса Morpher, который реализует все функции web-сервиса.
#### Инициализация
Перед использованием web-сервиса требуется инициализировать Morpher, указав требуемый язык.  
```java
Morpher.initialization(Languages.RUSSIAN);
```
В случае использования платной версии web-сервиса Морфер при инициализации передается access-token.
```java
final String ACCESS_TOKEN = "some token";
Morpher.initialization(Language.RUSSIAN, ACCESS_TOKEN);
```
#### Выполнение запросов
Обмен запросами с web-сервисом осуществляется асинхронно, поэтому методам класса Morpher, кроме основных параметров,
необходимо передавать экземпляр класса MorpherRequestListener.
***
##### Выполнение склонения
```java
//Формат вызова:
//Morpher.getDeclension(<текст>, <падеж>, <число>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getDeclension("ёлка", Cases.NOMINATIVE_CASE, Quantity.SINGULAR, new MorpherRequestListener() {
    public void onResponse(String result) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка ошибки
    }
});
```
***
##### Выделение ФИО
```java
//Формат вызова:
//Morpher.getSeparatedFullName(<полное имя>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getSeparatedFullName("Иванов Иван Иванович", new MorpherRequestListener() {
    public void onResponse(String name, String surname, String middleName) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка ошибки
    }
});
```
***
##### Согласование единицы измерения с числом
```java
//Формат вызова:
//Morpher.getNumeralAndUnit(<текст>, <число>, <падеж>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getNumeralAndUnit("ёлка", 10, Cases.NOMINATIVE_CASE, new MorpherRequestListener() {
    public void onResponse(String result) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка
    }
});
```
***
##### Определение пола по ФИО
```java
//Формат вызова:
//Morpher.getGender(<ФИО>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getGender("Иванов Иван Иванович", new MorpherRequestListener() {
    public void onResponse(String result) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка
    }
});
```
***
##### Склонение прилагательного по родам
```java
//Формат вызова:
//Morpher.getAdjectiveByGender(<прилагательное>, <род>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getAdjectiveByGender("ёлочный", Gender.MALE, new MorpherRequestListener() {
    public void onResponse(String result) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка
    }
});
```
***
##### Образование прилагательных от названий городов и стран
```java
//Формат вызова:
//Morpher.getAdjectivize(<название>, <экземпляр MorpherRequestListener>);
//Пример вызова:
Morpher.getAdjectivize("Москва", new MorpherRequestListener() {
    public void onResponse(Vector<String> result) {
    //Манипулация результатом
    }
    public void onFailure(String error) {
    //Обработка
    }
});
```
***
#### Исключения
В процессе работы с библиотекой могут порождаться следующие исключения:  
* LimitExceededException - Превышен лимит на количество запросов в сутки.
* IpBlockedException - IP заблокирован.
* WrongMethodException - Склонение числительных в declension не поддерживается. Используйте метод spell.
* NoRussianWordsException - Не найдено русских слов.
* NoRequiredParameterException - Не указан обязательный параметр s.
* UnpaidServiceException - Необходимо оплатить услугу.
* TokenNotFoundException - Данный token не найден.
* WrongTokenFormatException - Неверный формат токена.
