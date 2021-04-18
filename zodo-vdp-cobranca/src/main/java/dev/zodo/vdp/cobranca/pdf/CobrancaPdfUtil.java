package dev.zodo.vdp.cobranca.pdf;

import dev.zodo.vdp.cobranca.model.Boleto;
import dev.zodo.vdp.cobranca.model.BoletosGerados;
import dev.zodo.vdp.cobranca.pdf.itf25.ITF25Barcode;
import dev.zodo.vdp.cobranca.report.ReportCobrancaColumn;
import dev.zodo.vdp.cobranca.report.ReportUtil;
import dev.zodo.vdp.cobranca.util.CobrancaUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class CobrancaPdfUtil {

    private static final String TEMPLATE_FICHA_COMPENSACAO = "/templates/ficha-compensacao-A4-mask.pdf";

    private final Map<String, PDDocument> templates = new HashMap<>();

    private COSDictionary cobrancaPageTemplate;
    @Getter
    private PDDocument currentDocument;
    @Getter
    private int currentPageIndex;
    @Getter
    private PDPage currentPage;
    @Getter
    private PDPageContentStream currentPgStream;

    private PDFont currentFont;
    private float currentSize;

    protected abstract void afterProcessCobranca(Boleto boleto) throws IOException;

    public final void generatePdf(BoletosGerados boletosGerados, OutputStream output) {
        generatePdf(boletosGerados, output, null, null);
    }

    public final void generatePdf(BoletosGerados boletosGerados, OutputStream output, OutputStream reportOutput) {
        generatePdf(boletosGerados, output, reportOutput, null);
    }

    public final void generatePdf(BoletosGerados boletosGerados, OutputStream output, OutputStream xlsxReportOutput, Integer limite) {
        try {
            currentDocument = new PDDocument();
            int reg = 0;
            for (Boleto boleto : boletosGerados.getBoletos()) {
                reg++;
                if (limite != null && limite < reg) {
                    continue;
                }
                novaPaginaCobranca(boleto);
            }
            save(output);
        } catch (IOException e) {
            log.error("Erro ao processar as cobranças.", e);
        }
        closeTemplates();
        try {
            if (xlsxReportOutput != null) {
                ReportUtil.generateReportCobranca(boletosGerados.getBoletos(), List.of(ReportCobrancaColumn.values()), xlsxReportOutput);
            }
        } catch (IOException e) {
            log.error("Erro ao gerar o relatório.", e);
        }
    }

    protected void novaPagina(PDRectangle pdRectangle) throws IOException {
        PDPage pdPage = pdRectangle != null ? new PDPage(pdRectangle) : new PDPage(PDRectangle.A4);
        defineCurrentpage(pdPage);
    }

    protected void novaPagina(COSDictionary cosDictionary) throws IOException {
        PDPage pdPage = cosDictionary != null ? new PDPage(cosDictionary) : new PDPage(PDRectangle.A4);
        defineCurrentpage(pdPage);
    }

    private void defineCurrentpage(PDPage pdPage) throws IOException {
        currentDocument.addPage(pdPage);
        setCurrentPage(currentDocument.getNumberOfPages() - 1);
    }

    protected void setCurrentPage(int pageIndex) throws IOException {
        closePdPageStreamIfOpened();
        this.currentPageIndex = pageIndex;
        this.currentPage = currentDocument.getPage(currentPageIndex);
        currentPgStream = new PDPageContentStream(currentDocument, currentPage, PDPageContentStream.AppendMode.APPEND, true, true);
    }

    protected void novaPaginaCobranca(Boleto boleto) throws IOException {
        novaPagina(cloneCOSDictionaryTemplate());
        FichaCompensacaoPdf.processarFichaCompensacao(this, boleto);
        afterProcessCobranca(boleto);
    }

    protected COSDictionary loadPageCOSDictionaryTemplate(InputStream templateFile, String filename, int pageIndex) {
        try {
            PDDocument document = PDDocument.load(templateFile);
            templates.putIfAbsent(filename, document);
            PDPage pageTemplate = document.getPage(pageIndex);
            return new COSDictionary(pageTemplate.getCOSObject());
        } catch (Exception e) {
            log.error("Erro ao carregar template.", e);
        }
        return null;
    }

    private void loadCobrancaPageTemplate() {
        InputStream cobrancaTemplateFile = CobrancaUtil.getResource(TEMPLATE_FICHA_COMPENSACAO);
        this.cobrancaPageTemplate = loadPageCOSDictionaryTemplate(cobrancaTemplateFile, TEMPLATE_FICHA_COMPENSACAO, 0);
    }

    private COSDictionary cloneCOSDictionaryTemplate() {
        if (this.cobrancaPageTemplate == null) {
            loadCobrancaPageTemplate();
        }
        return cloneCOSDictionaryTemplate(this.cobrancaPageTemplate);
    }

    protected COSDictionary cloneCOSDictionaryTemplate(COSDictionary cosDictionary) {
        return new COSDictionary(cosDictionary);
    }

    protected void save(OutputStream output) throws IOException {
        closePdPageStreamIfOpened();
        currentDocument.save(output);
        currentDocument.close();
        closeTemplates();
    }

    private void closePdPageStreamIfOpened() {
        try {
            if (currentPgStream != null) {
                currentPgStream.close();
            }
        } catch (Exception e) {
            log.warn("Erro ao tentar stream aberto.", e);
        }
    }

    private void closeTemplates() {
        templates.forEach((s, pdDocument) -> {
            try {
                pdDocument.close();
            } catch (Exception e) {
                log.info("Erro ao tentar fechar o template aberto {}.", s, e);
            }
        });
        templates.clear();
    }

    public void setCurrentFontSize(float size) throws IOException {
        currentSize = size;
        currentPgStream.setFont(currentFont, currentSize);
    }

    public void setCurrentFont(PDFont pdFont, float size) throws IOException {
        this.currentFont = pdFont;
        setCurrentFontSize(size);
    }

    public void drawTextCenter(String text, float x, float y) throws IOException {
        drawText(text, x, y, TextAlign.CENTER);
    }

    public void drawTextRight(String text, float x, float y) throws IOException {
        drawText(text, x, y, TextAlign.RIGHT);
    }

    public void drawText(String text, float x, float y) throws IOException {
        drawText(text, x, y, TextAlign.LEFT);
    }

    public void drawText(String text, float x, float y, TextAlign textAlign) throws IOException {
        if (text == null) {
            return;
        }
        currentPgStream.beginText();
        currentPgStream.newLineAtOffset(textAlign.calculate(text, x, currentFont, currentSize), y);
        currentPgStream.showText(text);
        currentPgStream.endText();
    }

    public void drawTextMultilineNoControlTextBox(List<String> text, float leading) throws IOException {
        if (text == null) {
            return;
        }
        currentPgStream.setLeading(leading);
        for (String tx : text) {
            currentPgStream.showText(tx);
            textNewLine();
        }
    }

    public void beginText(float x, float y) throws IOException {
        currentPgStream.beginText();
        currentPgStream.newLineAtOffset(x, y);
    }

    public void endText() throws IOException {
        currentPgStream.endText();
    }

    public void textNewLine() throws IOException {
        currentPgStream.newLine();
    }

    public void drawTextMultiline(List<String> text, float x, float y, float leading) throws IOException {
        if (text == null) {
            return;
        }
        beginText(x, y);
        drawTextMultilineNoControlTextBox(text, leading);
        endText();
    }

    public void drawFillBox(float x, float y, float width, float height, Color color) throws IOException {
        currentPgStream.saveGraphicsState();
        currentPgStream.addRect(x, y, width, height);
        currentPgStream.setNonStrokingColor(color);
        currentPgStream.fill();
        currentPgStream.restoreGraphicsState();
    }

    public void drawStrokeBox(float x, float y, float width, float height, float lineWidth, Color color) throws IOException {
        currentPgStream.saveGraphicsState();
        currentPgStream.addRect(x, y, width, height);
        currentPgStream.setStrokingColor(color);
        currentPgStream.setLineWidth(lineWidth);
        currentPgStream.stroke();
        currentPgStream.restoreGraphicsState();
    }

    public void drawGrid() throws IOException {
        // 595 842
        currentPgStream.saveGraphicsState();
        int space = 20;
        for (int i = space; i < 595; i += space) {
            currentPgStream.addRect(i, 10, 0.2f, 822);
        }
        for (int i = space; i < 842; i += space) {
            currentPgStream.addRect(10, i, 575, 0.2f);
        }
        currentPgStream.setNonStrokingColor(255, 50, 0);
        currentPgStream.fill();
        currentPgStream.restoreGraphicsState();
        currentPgStream.setFont(PDType1Font.HELVETICA, 5);
        for (int i = space; i < 595; i += space) {
            drawText(i + "", i + 3f, 20);
        }
        for (int i = space; i < 595; i += space) {
            drawText(i + "", i + 3f, 330);
        }
        for (int i = space; i < 842; i += space) {
            drawText(i + "", 15, i + 3f);
        }
    }

    public void drawITF25Barcode(String code, float x, float y, float height) throws IOException {
        ITF25Barcode.drawBarcode(currentPgStream, code, x, y, height);
    }

}