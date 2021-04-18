package dev.zodo.vdp.cobranca.leitura.fields;

import dev.zodo.vdp.cobranca.model.Beneficiario;
import dev.zodo.vdp.cobranca.model.Endereco;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeneficiarioFields implements MapField<Beneficiario> {
    private static final String PREFIX = "beneficiario.";
    public static final String NOME = PREFIX + "nome";
    public static final String CPF_CNPJ = PREFIX + "cpfCnpj";
    public static final String ENDERECO_LOGRADOURO = PREFIX + "endereco.logradouro";
    public static final String ENDERECO_NUMERO = PREFIX + "endereco.numero";
    public static final String ENDERECO_COMPLEMENTO = PREFIX + "endereco.complemento";
    public static final String ENDERECO_BAIRRO = PREFIX + "endereco.bairro";
    public static final String ENDERECO_CEP = PREFIX + "endereco.cep";
    public static final String ENDERECO_CIDADE = PREFIX + "endereco.cidade";
    public static final String ENDERECO_UF = PREFIX + "endereco.uf";

    public static Set<String> allKeys() {
        return Set.of(NOME, CPF_CNPJ, ENDERECO_LOGRADOURO, ENDERECO_NUMERO,
                ENDERECO_COMPLEMENTO, ENDERECO_BAIRRO, ENDERECO_CEP, ENDERECO_CIDADE, ENDERECO_UF);
    }

    public static Beneficiario fromKeyValues(Map<String, Object> values) {
        BeneficiarioFields builder = new BeneficiarioFields();
        return builder.build(values);
    }

    @Override
    public Beneficiario build(Map<String, Object> values) {
        Endereco endereco = Endereco.builder()
                .logradouro(fromValue(ENDERECO_LOGRADOURO, values))
                .numero(fromValue(ENDERECO_NUMERO, values))
                .complemento(fromValue(ENDERECO_COMPLEMENTO, values))
                .bairro(fromValue(ENDERECO_BAIRRO, values))
                .cep(fromValue(ENDERECO_CEP, values))
                .cidade(fromValue(ENDERECO_CIDADE, values))
                .uf(fromValue(ENDERECO_UF, values))
                .build();
        return Beneficiario.builder()
                .nome(fromValue(NOME, values))
                .cpfCnpj(fromValue(CPF_CNPJ, values))
                .endereco(endereco)
                .build();
    }
}
