package dev.zodo.vdp.cobranca.leitura.fields;

import java.util.Map;

public interface ParseCustomValues {
    Map<String, Object> apply(Map<String, Object> values);
}
