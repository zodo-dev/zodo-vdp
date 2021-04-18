package dev.zodo.vdp.cobranca.exceptions;

import java.text.MessageFormat;

public class VdpCobrancaUncheckedException extends RuntimeException {
    public VdpCobrancaUncheckedException(String message) {
        super(message);
    }

    public VdpCobrancaUncheckedException(String message, Object ... valueParam) {
        super(MessageFormat.format(message, valueParam));
    }
}
