package ru.morpher.ws3.russian;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdjectiveGendersResult {
    public String feminine;
    public String neuter;
    public String plural;
}
