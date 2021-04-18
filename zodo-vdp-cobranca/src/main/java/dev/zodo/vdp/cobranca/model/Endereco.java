package dev.zodo.vdp.cobranca.model;

import dev.zodo.vdp.cobranca.util.Util;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;

    public String getLogradouroNumeroComplemento() {
        StringBuilder builder = new StringBuilder();
        if (Util.isNotBlank(logradouro)) {
            builder.append(logradouro.trim());
        }
        if (Util.isNotBlank(numero)) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(numero.trim());
        }
        if (Util.isNotBlank(complemento)) {
            if (builder.length() > 0) {
                builder.append(", ");
            }
            builder.append(complemento.trim());
        }
        return builder.toString();
    }

    public String getLogradouroNumeroComplementoBairro() {
        StringBuilder builder = new StringBuilder(getLogradouroNumeroComplemento());
        if (Util.isNotBlank(bairro)) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }
            builder.append(bairro.trim());
        }
        return builder.toString();
    }

    public String getCepCidadeUf() {
        StringBuilder builder = new StringBuilder();
        if (Util.isNotBlank(cep)) {
            builder.append(cep.trim());
        }
        if (Util.isNotBlank(cidade)) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }
            builder.append(cidade.trim());
        }
        if (Util.isNotBlank(uf)) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }
            builder.append(uf.trim());
        }
        return builder.toString();
    }
}
