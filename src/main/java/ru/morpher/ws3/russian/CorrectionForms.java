package ru.morpher.ws3.russian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CorrectionForms {
    @JsonProperty("И")
    public String nominative;
    @JsonProperty("Р")
    public String genitive;
    @JsonProperty("Д")
    public String dative;
    @JsonProperty("В")
    public String accusative;
    @JsonProperty("Т")
    public String instrumental;
    @JsonProperty("П")
    public String prepositional;
    @JsonProperty("М")
    public String locative;

}
