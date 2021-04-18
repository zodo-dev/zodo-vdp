package dev.zodo.vdp.cobranca.util;

import dev.zodo.vdp.cobranca.model.Cobranca;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum Banco {
    BB("001", "9", "Banco do Brasil"),
    SANTANDER("033", "7", "SANTANDER"),
    AILOS("085", "0", "AILOS"),
    CAIXA("104", "0", "CAIXA") {
        @Override
        public void configurarCobranca(Cobranca cobranca) {
            if (Util.isBlank(cobranca.getCodigoBarras())) {
                return;
            }
            String codBar = cobranca.getCodigoBarras();
            String codigoBeneficiario = cobranca.getCodigoBeneficiario();
            if (Util.isBlank(codigoBeneficiario) || Util.isBlank(codigoBeneficiario.replaceAll(Util.PATTERN_NOT_NUMBER, "").trim())) {
                cobranca.setCodigoBeneficiario(Util.valueFrom(codBar, 20, 6) + "-" + Util.valueFrom(codBar, 26, 1));
            }
        }
    },
    UNICRED("136", "8", "UNICRED"),
    BRADESCO("237", "2", "BRADESCO"),
    ITAU("341", "7", "ITAÚ"),
    SICOOB("756", "0", "SICOOB"),
    INDEFINIDO("000", "0", "INDEFINIDO"),
    ;
    private final String codigo;
    private final String digito;
    private final String nome;
    private static final Map<String, Banco> byCodigo = Map.copyOf(Stream.of(values())
            .collect(Collectors.toMap(Banco::getCodigo, Function.identity())));

    public static Banco fromCodigo(String codigo) {
        return byCodigo.getOrDefault(codigo, INDEFINIDO);
    }

    public void configurarCobranca(Cobranca cobranca) {
        // Extender o enum do banco quando precisar ajustar algo.
    }

}
