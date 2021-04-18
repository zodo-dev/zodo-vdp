package dev.zodo.vdp.cobranca.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface Util {
    DateTimeFormatter LOCAL_DATE_FORMATTER_PTBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    String PATTERN_ONLY_NUMBER = "\\d+";
    String PATTERN_NOT_NUMBER = "[^\\d]";
    String PATTERN_NOT_ALLOW_FILE_NAME = "[^a-zA-Z0-9_\\-.]";
    String PATTERN_LATIN_1_CHARS = "[^\\x00-\\x7F]";
    float DOTS_PER_INCH = 72;
    float MM_PER_INCH = 25.4f;
    float DOTS_PER_MM = DOTS_PER_INCH / MM_PER_INCH;
    LocalDate DATA_BASE_FATOR_VENCIMENTO = LocalDate.of(1997, 10, 7);
    String PESO_MODULO11 = "98765432";

    static boolean isBlank(String str) {
        return str == null || str.trim().isBlank();
    }

    static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    static String trimIfNotEmpty(String str) {
        return isNotBlank(str) ? str.trim() : str;
    }

    static boolean isOnlyNumber(String s) {
        return s.matches(PATTERN_ONLY_NUMBER);
    }

    static float mmToDot(float mm) {
        return mm * DOTS_PER_MM;
    }

    static String getPesoModulo11Forlenght(String code) {
        int pesoLength = PESO_MODULO11.length();
        int mult = Math.floorDiv(code.length(), pesoLength) + 1;
        StringBuilder pesoBuilder = new StringBuilder();
        pesoBuilder.append(PESO_MODULO11.repeat(Math.max(0, mult)));
        pesoBuilder.delete(0, (pesoLength * mult) - code.length());
        return pesoBuilder.toString();
    }

    static String padLeft(String str, int length, char charPad) {
        return ("" + charPad).repeat(length - str.length()) + str;
    }

    static String valueFrom(String line, int position, int length) {
        return valueFrom(line, position, length, true);
    }

    static String valueFrom(String line, int position, int length, boolean trim) {
        int start = position - 1;
        int startPlusLength = start + length;
        int end = Math.min(startPlusLength, line.length());
        String value = line.substring(start, end);
        return trim ? value.trim() : value;
    }

    static String normalizeFilename(String filename) {
        if (isBlank(filename)) {
            return filename;
        }
        return filename.toUpperCase().replace(" ", "_").replaceAll(PATTERN_NOT_ALLOW_FILE_NAME, "");
    }
    static String replaceNonLatin1Chars(String str) {
        return isNotBlank(str) ? str.replaceAll(PATTERN_LATIN_1_CHARS, " ") : str;
    }
}
