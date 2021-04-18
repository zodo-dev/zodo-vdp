package dev.zodo.vdp.cobranca.leitura.fields;

import dev.zodo.vdp.cobranca.leitura.converter.ConverterUtil;
import dev.zodo.vdp.cobranca.model.Cobranca;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CobrancaFields implements MapField<Cobranca> {
    private static final String PREFIX = "cobranca.";
    public static final String VENCIMENTO = PREFIX + "vencimento";
    public static final String DATA_PROCESSAMENTO = PREFIX + "dataProcessamento";
    public static final String DATA_DOCUMENTO = PREFIX + "dataDocumento";
    public static final String VALOR_DOCUMENTO = PREFIX + "valorDocumento";
    public static final String DESCONTO = PREFIX + "desconto";
    public static final String OUTRAS_DEDUCOES = PREFIX + "outrasDeducoes";
    public static final String MORA_MULTA_JUROS = PREFIX + "moraMultaJuros";
    public static final String OUTROS_ACRESCIMOS = PREFIX + "outrosAcrescimos";
    public static final String VALOR_COBRADO = PREFIX + "valorCobrado";
    public static final String LINHA_DIGITAVEL = PREFIX + "linhaDigitavel";
    public static final String CODIGO_BARRAS = PREFIX + "codigoBarras";
    public static final String NOSSO_NUMERO = PREFIX + "nossoNumero";
    public static final String LOCAL_PAGAMENTO = PREFIX + "localPagamento";
    public static final String CARTEIRA = PREFIX + "carteira";
    public static final String CODIGO_BENEFICIARIO = PREFIX + "codigoBeneficiario";
    public static final String AGENCIA = PREFIX + "agencia";
    public static final String BANCO_CODIGO = PREFIX + "bancoCodigo";
    public static final String BANCO_NOME = PREFIX + "bancoNome";
    public static final String ACEITE = PREFIX + "aceite";
    public static final String ESPECIE_DOCUMENTO = PREFIX + "especieDocumento";
    public static final String ESPECIE_MOEDA = PREFIX + "especieMoeda";
    public static final String INSCRUCOES = PREFIX + "instrucoes";

    public static Set<String> allKeys() {
        return Set.of(VENCIMENTO, DATA_PROCESSAMENTO, DATA_DOCUMENTO, VALOR_DOCUMENTO, DESCONTO, OUTRAS_DEDUCOES,
                MORA_MULTA_JUROS, OUTROS_ACRESCIMOS, VALOR_COBRADO, LINHA_DIGITAVEL, CODIGO_BARRAS, NOSSO_NUMERO,
                LOCAL_PAGAMENTO, CARTEIRA, CODIGO_BENEFICIARIO, AGENCIA, BANCO_CODIGO, BANCO_NOME, ACEITE, ESPECIE_DOCUMENTO,
                ESPECIE_MOEDA, INSCRUCOES);
    }

    public static Cobranca fromKeyValues(Map<String, Object> values) {
        CobrancaFields builder = new CobrancaFields();
        return builder.build(values).configureAfterSetValues();
    }

    @Override
    public Cobranca build(Map<String, Object> values) {
        return Cobranca.builder()
                .vencimento(fromValue(VENCIMENTO, values, ConverterUtil::objToLocalDate))
                .dataProcessamento(fromValue(DATA_PROCESSAMENTO, values, ConverterUtil::objToLocalDate))
                .dataDocumento(fromValue(DATA_DOCUMENTO, values, ConverterUtil::objToLocalDate))
                .valorDocumento(fromValue(VALOR_DOCUMENTO, values))
                .desconto(fromValue(DESCONTO, values))
                .outrasDeducoes(fromValue(OUTRAS_DEDUCOES, values))
                .moraMultaJuros(fromValue(MORA_MULTA_JUROS, values))
                .outrosAcrescimos(fromValue(OUTROS_ACRESCIMOS, values))
                .valorCobrado(fromValue(VALOR_COBRADO, values))
                .linhaDigitavel(fromValue(LINHA_DIGITAVEL, values))
                .codigoBarras(fromValue(CODIGO_BARRAS, values))
                .nossoNumero(fromValue(NOSSO_NUMERO, values))
                .localPagamento(fromValue(LOCAL_PAGAMENTO, values))
                .carteira(fromValue(CARTEIRA, values))
                .codigoBeneficiario(fromValue(CODIGO_BENEFICIARIO, values))
                .bancoCodigo(fromValue(BANCO_CODIGO, values))
                .bancoNome(fromValue(BANCO_NOME, values))
                .aceite(fromValue(ACEITE, values))
                .especieDocumento(fromValue(ESPECIE_DOCUMENTO, values))
                .especieMoeda(fromValue(ESPECIE_MOEDA, values))
                .instrucoes(fromValue(INSCRUCOES, values, ConverterUtil::objToStringList))
                .build();
    }
}
