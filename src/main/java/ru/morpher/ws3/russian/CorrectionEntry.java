package ru.morpher.ws3.russian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CorrectionEntry {
    @JsonProperty("singular")
    public CorrectionForms singular;
    @JsonProperty("plural")
    public CorrectionForms plural;
    @JsonProperty("gender")
    public Gender gender;
}
