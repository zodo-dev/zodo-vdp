package dev.zodo.vdp.cobranca.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

class CobrancaUtilTest {

    @ParameterizedTest
    @CsvSource({"2000-07-04,1001", "2002-05-01,1667", "2021-01-05,8491", "2025-02-21,9999"})
    void fatorVencimentoTest(String dataStr, String expected) {
        Assertions.assertEquals(expected, CobrancaUtil.fatorVencimento(LocalDate.parse(dataStr)));
    }

    @ParameterizedTest
    @CsvSource({"001905009,5", "4014481606,9", "0680935031,4"})
    void modulo10Test(String numeros, String expected) {
        Assertions.assertEquals(expected, CobrancaUtil.modulo10(numeros));
    }

    @ParameterizedTest
    @CsvSource({"11122233344,111.222.333-44", "122233344,001.222.333-44", "11222333444455,11.222.333/4444-55", "222333444455,00.222.333/4444-55"})
    void formataCpfCnpjTest(String semFormatacao, String expected) {
        Assertions.assertEquals(expected, CobrancaUtil.formataCpfCnpj(semFormatacao));
    }

    @ParameterizedTest
    @EnumSource(Banco.class)
    void modulo11TestBanco(Banco banco) {
        Assertions.assertEquals(banco.getDigito(), CobrancaUtil.modulo11(banco.getCodigo(), false), banco.getNome());
    }

    @ParameterizedTest
    @CsvSource({"00199.99994 99999.999990 99999.999990 3 10010000110000,00193100100001100009999999999999999999999999"})
    void codigoBarrasFromLinhaDigitavelTest(String linhaDigitavel, String CodigoBarras) {
        CobrancaUtil.validarCodigoBarras(CodigoBarras);
        Assertions.assertEquals(linhaDigitavel, CobrancaUtil.linhaDigitavelFromCodigoBarras(CodigoBarras));
        Assertions.assertEquals(CodigoBarras, CobrancaUtil.codigoBarrasFromLinhaDigitavel(linhaDigitavel));
    }

}