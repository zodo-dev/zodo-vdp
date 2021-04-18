package dev.zodo.vdp.cobranca.leitura.fields;

import java.util.Map;
import java.util.function.Function;

public interface MapField<T> {
    T build(Map<String, Object> values);

    default String fromValue(String key, Map<String, Object> values) {
        return fromValue(key, values, Object::toString);
    }

    default <O> O fromValue(String key, Map<String, Object> values, Function<Object, O> converter) {
        Object obj = values.get(key);
        if (obj == null) {
            return null;
        }
        return converter.apply(obj);
    }
}
