package dev.zodo.vdp.cobranca.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Boleto {
    private int sequencial;
    private String arquivoOrigem;
    private Pagador pagador;
    private Beneficiario beneficiario;
    private Cobranca cobranca;
    private Map<String, Object> customValues;

    public String getSequencialCode() {
        return String.format("BLT%05d", sequencial);
    }
}
