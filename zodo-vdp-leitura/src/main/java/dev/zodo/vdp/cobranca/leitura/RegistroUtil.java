package dev.zodo.vdp.cobranca.leitura;

import dev.zodo.vdp.cobranca.leitura.fields.BeneficiarioFields;
import dev.zodo.vdp.cobranca.leitura.fields.CobrancaFields;
import dev.zodo.vdp.cobranca.leitura.fields.PagadorFields;
import dev.zodo.vdp.cobranca.leitura.fields.ParseCustomValues;
import dev.zodo.vdp.cobranca.model.Boleto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegistroUtil {

    public static Boleto registroToBoleto(Registro registro, ParseCustomValues parseCustomFunction, String arquivoOrigem, int sequencial) {
        Map<String, Object> values = registro.getValues();
        Map<String, Object> customValues = customValuesFromRegistro(values);
        return Boleto.builder()
                .sequencial(sequencial)
                .arquivoOrigem(arquivoOrigem)
                .beneficiario(BeneficiarioFields.fromKeyValues(values))
                .pagador(PagadorFields.fromKeyValues(values))
                .cobranca(CobrancaFields.fromKeyValues(values))
                .customValues(parseCustomFunction.apply(customValues))
                .build();
    }

    private static Map<String, Object> customValuesFromRegistro(Map<String, Object> values) {
        Set<String> definedKeys = new HashSet<>();
        definedKeys.addAll(BeneficiarioFields.allKeys());
        definedKeys.addAll(PagadorFields.allKeys());
        definedKeys.addAll(CobrancaFields.allKeys());
        return values.entrySet().stream()
                .filter(e -> !definedKeys.contains(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
