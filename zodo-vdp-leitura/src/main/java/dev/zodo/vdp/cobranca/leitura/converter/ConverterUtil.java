package dev.zodo.vdp.cobranca.leitura.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConverterUtil {

    private static final DateTimeFormatter LOCAL_DATE_PTBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static LocalDate objToLocalDate(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof LocalDate) {
            return (LocalDate) obj;
        }
        String dt = obj.toString();
        if (dt.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dt, LOCAL_DATE_PTBR);
    }

    @SuppressWarnings("unchecked")
    public static List<String> objToStringList(Object obj) {
        if (obj == null) {
            return new ArrayList<>();
        }
        if (obj instanceof Collection) {
            return new ArrayList<>((Collection<String>) obj);
        }
        String st = obj.toString();
        return List.of(st);
    }
}
