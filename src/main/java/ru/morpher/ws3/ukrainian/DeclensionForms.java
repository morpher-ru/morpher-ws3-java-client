package ru.morpher.ws3.ukrainian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclensionForms {
    @JsonProperty("Н")
    public String nominative;
    @JsonProperty("Р")
    public String genitive;
    @JsonProperty("Д")
    public String dative;
    @JsonProperty("З")
    public String accusative;
    @JsonProperty("О")
    public String instrumental;
    @JsonProperty("М")
    public String prepositional;
    @JsonProperty("К")
    public String vocative;

}
