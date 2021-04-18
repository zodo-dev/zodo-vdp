package dev.zodo.vdp.cobranca.util;

import dev.zodo.vdp.cobranca.exceptions.VdpCobrancaUncheckedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CobrancaUtil {

    public static String codigoBarrasFromLinhaDigitavel(String linha) {
        // TODO Implementar/Validar dados
        throw new VdpCobrancaUncheckedException("Não implementado");
    }

    public static String linhaDigitavelFromCodigoBarras(String codigo) {
        validarCodigoBarras(codigo);
        StringBuilder linhaDigitavel = new StringBuilder();
        String c1 = codigo.substring(0, 4) + codigo.charAt(19) + "." + codigo.substring(20, 24);
        linhaDigitavel.append(c1);
        linhaDigitavel.append(modulo10(c1.replaceAll(Util.PATTERN_NOT_NUMBER, "")));
        linhaDigitavel.append(" ");

        String c2 = codigo.substring(24, 29) + "." + codigo.substring(29, 34);
        linhaDigitavel.append(c2);
        linhaDigitavel.append(modulo10(c2.replaceAll(Util.PATTERN_NOT_NUMBER, "")));
        linhaDigitavel.append(" ");

        String c3 = codigo.substring(34, 39) + "." + codigo.substring(39, 44);
        linhaDigitavel.append(c3);
        linhaDigitavel.append(modulo10(c3.replaceAll(Util.PATTERN_NOT_NUMBER, "")));
        linhaDigitavel.append(" ");

        linhaDigitavel.append(codigo, 4, 5);
        linhaDigitavel.append(" ");

        linhaDigitavel.append(codigo, 5, 19);
        return linhaDigitavel.toString();
    }

    public static String formataCpfCnpj(String cpfCnpj) {
        if (Util.isBlank(cpfCnpj)) {
            return cpfCnpj;
        }
        cpfCnpj = cpfCnpj.replaceAll(Util.PATTERN_NOT_NUMBER, "");
        if (cpfCnpj.length() > 11) {
            cpfCnpj = Util.padLeft(cpfCnpj, 14, '0');
            return cpfCnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        cpfCnpj = Util.padLeft(cpfCnpj, 11, '0');
        return cpfCnpj.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static void validarCodigoBarras(String codigo) {
        if (Util.isBlank(codigo)) {
            throw new VdpCobrancaUncheckedException("Código de barras vazio");
        }

        if (codigo.length() != 44) {
            throw new VdpCobrancaUncheckedException("Código de deve ter 44 dígitos: {0}", codigo);
        }

        if (!Util.isOnlyNumber(codigo)) {
            throw new VdpCobrancaUncheckedException("Código de deve conter apenas números: {0}", codigo);
        }

        String dv = codigo.substring(4,5);
        String codigoSemDv = codigo.substring(0,4) + codigo.substring(5);
        String dvCalculado = modulo11(codigoSemDv, true);
        if (!dv.equals(dvCalculado)) {
            throw new VdpCobrancaUncheckedException("Dígito verificador inválido. Informado: {0} calculado: {1}", dv, dvCalculado);
        }

    }

    public static String modulo10(String numeros) {
        if (!Util.isOnlyNumber(numeros)) {
            throw new VdpCobrancaUncheckedException("Módulo 10 só pode ser calculado para números: {0}", numeros);
        }
        int nextPeso = 2;
        int somaTotal = 0;
        for (int i = numeros.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numeros.substring(i, i+1));
            int nMultiPeso = n * nextPeso;
            somaTotal += somaNumeroModulo10(nMultiPeso);
            nextPeso = nextPeso == 2 ? 1 : 2;
        }
        int resto = somaTotal % 10;
        if (resto == 0) {
            return "0";
        }
        int dv = 10 - resto;
        return String.valueOf(dv);
    }

    private static int somaNumeroModulo10(int numero) {
        if (numero < 10) {
            return numero;
        }
        String numeroStr = String.valueOf(numero);
        int n1 = Integer.parseInt(numeroStr.substring(0, 1));
        int n2 = Integer.parseInt(numeroStr.substring(1, 2));
        return n1 + n2;
    }

    public static String modulo11(String numeros, boolean calculoParaCodigoBarras) {
        String peso = Util.getPesoModulo11Forlenght(numeros);
        int somaTotal = 0;
        for (int i = numeros.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numeros.substring(i, i+1));
            int nextPeso = Integer.parseInt(peso.substring(i, i+1));
            somaTotal += n * nextPeso;
        }
        int resto = somaTotal % 11;
        int dv = 11 - resto;
        if (dv == 10 || dv == 11) {
            return calculoParaCodigoBarras ? "1" : "0";
        }
        return String.valueOf(dv);
    }

    public static String fatorVencimento(LocalDate localDate) {
        return String.format("%04d", ChronoUnit.DAYS.between(Util.DATA_BASE_FATOR_VENCIMENTO, localDate));
    }

    public static InputStream getResource(String resourceName) {
        return CobrancaUtil.class.getResourceAsStream(resourceName);
    }

}
