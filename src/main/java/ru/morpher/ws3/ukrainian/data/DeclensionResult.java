package ru.morpher.ws3.ukrainian.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclensionResult extends DeclensionForms {
    @JsonProperty("рід")
    public Gender gender;
}
