package ru.morpher.ws3.ukrainian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberSpellingResult {

    @JsonProperty("n")
    public DeclensionForms numberDeclension;

    @JsonProperty("unit")
    public DeclensionForms unitDeclension;

}
