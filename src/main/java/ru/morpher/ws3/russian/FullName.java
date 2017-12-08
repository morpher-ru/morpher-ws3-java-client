package ru.morpher.ws3.russian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FullName {
    @JsonProperty("И")
    public String name;
    @JsonProperty("Ф")
    public String surname;
    @JsonProperty("О")
    public String patronymic;
}
