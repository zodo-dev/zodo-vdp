package dev.zodo.vdp.cobranca.pdf.itf25;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ITF25Barcode {

    private final PDPageContentStream pgStream;

    public static void drawBarcode(PDPageContentStream pgStream, String code, float x, float y, float height) throws IOException {
        ITF25Barcode barcode = new ITF25Barcode(pgStream);
        barcode.drawBarcode(code, x, y, height);
    }

    private void drawBarcode(String code, float x, float y, float height) throws IOException {
        final List<ITF25BarConfig> barData = ITF25CodeTable.getItf25BarConfigsFromCode(code);
        pgStream.moveTo(x, y);
        pgStream.saveGraphicsState();
        float variableX = x;
        boolean black = true;
        for (ITF25BarConfig bar : barData) {
            if (black) {
                pgStream.addRect(variableX, y, bar.getWidth(), height);
            }
            variableX += bar.getWidth();
            black = !black;
        }
        pgStream.fill();
        pgStream.restoreGraphicsState();
    }

}
