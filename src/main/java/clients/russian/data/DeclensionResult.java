package clients.russian.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeclensionResult extends DeclensionForms{

    @JsonProperty("род")
    public Gender gender;

    @JsonProperty("где")
    public String where;
    @JsonProperty("куда")
    public String to;
    @JsonProperty("откуда")
    public String from;

    @JsonProperty("множественное")
    public DeclensionForms plural;

    @JsonProperty("ФИО")
    public FullName fullName;

}
