package dev.zodo.vdp.cobranca.pdf.itf25;

import dev.zodo.vdp.cobranca.exceptions.VdpCobrancaUncheckedException;
import dev.zodo.vdp.cobranca.util.Util;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

import static dev.zodo.vdp.cobranca.pdf.itf25.ITF25BarConfig.N;
import static dev.zodo.vdp.cobranca.pdf.itf25.ITF25BarConfig.W;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ITF25CodeTable {
    private static final Map<Character, ITF25BarConfig[]> TABLE_MAP = Map.ofEntries(
            Map.entry('0', new ITF25BarConfig[]{N, N, W, W, N}),
            Map.entry('1', new ITF25BarConfig[]{W, N, N, N, W}),
            Map.entry('2', new ITF25BarConfig[]{N, W, N, N, W}),
            Map.entry('3', new ITF25BarConfig[]{W, W, N, N, N}),
            Map.entry('4', new ITF25BarConfig[]{N, N, W, N, W}),
            Map.entry('5', new ITF25BarConfig[]{W, N, W, N, N}),
            Map.entry('6', new ITF25BarConfig[]{N, W, W, N, N}),
            Map.entry('7', new ITF25BarConfig[]{N, N, N, W, W}),
            Map.entry('8', new ITF25BarConfig[]{W, N, N, W, N}),
            Map.entry('9', new ITF25BarConfig[]{N, W, N, W, N})
    );

    private static final List<ITF25BarConfig> START_BAR = List.of(N, N, N, N);
    private static final List<ITF25BarConfig> END_BAR = List.of(W, N, N);

    public static List<ITF25BarConfig> getItf25BarConfigsFromCode(String code) {
        if (Util.isBlank(code)) {
            return Collections.emptyList();
        }
        if (!Util.isOnlyNumber(code)) {
            throw new VdpCobrancaUncheckedException(String.format("Código de barras deve conter apenas números [%s]", code));
        }

        if (code.length() % 2 != 0) {
            code = "0" + code;
        }
        List<ITF25BarConfig> configs = new ArrayList<>(START_BAR);
        for (int c = 0; c < code.length(); c+=2) {
            ITF25BarConfig[] c1 = TABLE_MAP.get(code.charAt(c));
            ITF25BarConfig[] c2 = TABLE_MAP.get(code.charAt(c+1));
            for (int i = 0; i < 5; i++) {
                configs.add(c1[i]);
                configs.add(c2[i]);
            }
        }
        configs.addAll(END_BAR);
        return configs;
    }
}
