package clients.ukrainian.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CorrectionForms {

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
