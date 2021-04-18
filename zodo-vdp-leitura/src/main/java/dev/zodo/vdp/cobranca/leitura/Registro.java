package dev.zodo.vdp.cobranca.leitura;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
public class Registro {
    private final Map<String, Object> parsedValues = new HashMap<>();
    private final Map<String, Object> initialValues = new HashMap<>();
    @Getter
    private boolean empty = true;

    public void definirValoresPadrao(Map<String, Object> initialValues) {
        this.initialValues.clear();
        this.initialValues.putAll(initialValues);
    }

    public void reset() {
        parsedValues.clear();
        empty = true;
    }

    public Map<String, Object> getValues() {
        Map<String, Object> mergedValues = new HashMap<>(initialValues);
        mergedValues.putAll(parsedValues);
        return mergedValues;
    }

    public void addValue(String key, String value) {
        markNotEmpty();
        parsedValues.put(key, value);
    }

    public void addValueCumulative(String key, String value) {
        markNotEmpty();
        if (parsedValues.containsKey(key)) {
            Object vl = parsedValues.get(key);
            List<Object> values;
            if (vl instanceof List) {
                values = new ArrayList<>((Collection<?>) vl);
            } else {
                values = new ArrayList<>();
                values.add(vl);
            }
            values.add(value);
            parsedValues.put(key, values);
            return;
        }
        parsedValues.put(key, value);
    }

    private void markNotEmpty() {
        if (empty) {
            empty = false;
        }
    }

}
