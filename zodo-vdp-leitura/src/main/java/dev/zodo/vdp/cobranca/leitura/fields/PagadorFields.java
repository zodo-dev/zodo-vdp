package dev.zodo.vdp.cobranca.leitura.fields;

import dev.zodo.vdp.cobranca.model.Endereco;
import dev.zodo.vdp.cobranca.model.Pagador;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PagadorFields implements MapField<Pagador> {
    public static final String NOME = "pagador.nome";
    public static final String CPF_CNPJ = "pagador.cpfCnpj";
    public static final String ENDERECO_LOGRADOURO = "pagador.endereco.logradouro";
    public static final String ENDERECO_NUMERO = "pagador.endereco.numero";
    public static final String ENDERECO_COMPLEMENTO = "pagador.endereco.complemento";
    public static final String ENDERECO_BAIRRO = "pagador.endereco.bairro";
    public static final String ENDERECO_CEP = "pagador.endereco.cep";
    public static final String ENDERECO_CIDADE = "pagador.endereco.cidade";
    public static final String ENDERECO_UF = "pagador.endereco.uf";

    public static Set<String> allKeys() {
        return Set.of(NOME, CPF_CNPJ, ENDERECO_LOGRADOURO, ENDERECO_NUMERO, ENDERECO_COMPLEMENTO, ENDERECO_BAIRRO,
                ENDERECO_CEP, ENDERECO_CIDADE, ENDERECO_UF);
    }

    public static Pagador fromKeyValues(Map<String, Object> values) {
        PagadorFields builder = new PagadorFields();
        return builder.build(values);
    }

    @Override
    public Pagador build(Map<String, Object> values) {
        Endereco endereco = Endereco.builder()
                .logradouro(fromValue(ENDERECO_LOGRADOURO, values))
                .numero(fromValue(ENDERECO_NUMERO, values))
                .complemento(fromValue(ENDERECO_COMPLEMENTO, values))
                .bairro(fromValue(ENDERECO_BAIRRO, values))
                .cep(fromValue(ENDERECO_CEP, values))
                .cidade(fromValue(ENDERECO_CIDADE, values))
                .uf(fromValue(ENDERECO_UF, values))
                .build();
        return Pagador.builder()
                .nome(fromValue(NOME, values))
                .cpfCnpj(fromValue(CPF_CNPJ, values))
                .endereco(endereco)
                .build();
    }
}
