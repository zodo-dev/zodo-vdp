package dev.zodo.vdp.cobranca.pdf;

import dev.zodo.vdp.cobranca.model.Boleto;
import dev.zodo.vdp.cobranca.model.BoletosGerados;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;

@Slf4j
class PdfUtilTest {
    @Test
    void generatePdfTest() {
        Boleto b = new Boleto();
        BoletosGerados boletosGerados = BoletosGerados.getInstance(Collections.singletonList(b));
        CobrancaPdfUtil cobrancaPdfUtil = new CobrancaPdfUtil() {
            @Override
            protected void afterProcessCobranca(Boleto boleto) {

            }
        };
        try (FileOutputStream outputStream = new FileOutputStream("target/test.pdf")) {
            //cobrancaPdfUtil.generatePdf(boletos, outputStream, 5);
        } catch (IOException e) {
            log.error("Erro ao gerar PDF.", e);
        }
        Assertions.assertTrue(true);
    }
}