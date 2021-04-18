package dev.zodo.vdp.cobranca.pdf.itf25;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ITF25BarConfig {
    N('n', .83f),
    W('W', 2f);
    private final char letter;
    private final float width;
}
