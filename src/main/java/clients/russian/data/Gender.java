package clients.russian.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum Gender {
    Masculine,
    Feminine,
    Neuter,
    Plural;

    private static Map<String, Gender> namesMap = new HashMap<String, Gender>(4);

    static {
        namesMap.put("Мужской", Masculine);
        namesMap.put("Женский", Feminine);
        namesMap.put("Средний", Neuter);
        namesMap.put("Множественное", Plural);
    }

    @JsonCreator
    public static Gender forValue(String value) {
        return namesMap.get(value);
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, Gender> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }
}
