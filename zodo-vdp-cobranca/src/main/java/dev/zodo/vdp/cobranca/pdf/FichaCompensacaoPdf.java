package dev.zodo.vdp.cobranca.pdf;

import dev.zodo.vdp.cobranca.model.Beneficiario;
import dev.zodo.vdp.cobranca.model.Boleto;
import dev.zodo.vdp.cobranca.model.Cobranca;
import dev.zodo.vdp.cobranca.model.Pagador;
import dev.zodo.vdp.cobranca.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static dev.zodo.vdp.cobranca.util.Util.mmToDot;

@Slf4j
@RequiredArgsConstructor
public class FichaCompensacaoPdf {
    private final CobrancaPdfUtil pdfUtil;

    public static void processarFichaCompensacao(CobrancaPdfUtil pdfUtil, Boleto boleto) throws IOException {
        FichaCompensacaoPdf compensacaoPdf = new FichaCompensacaoPdf(pdfUtil);
        compensacaoPdf.preencherFichaCobranca(boleto);
    }



    private void preencherFichaCobranca(Boleto boleto) throws IOException {
        Beneficiario beneficiario = boleto.getBeneficiario();
        Pagador pagador = boleto.getPagador();
        Cobranca cobranca = boleto.getCobranca();

        pdfUtil.setCurrentFont(PDType1Font.HELVETICA_BOLD, 11.5f);
        pdfUtil.drawText(String.format("%s | %s |", cobranca.getBancoNome(), cobranca.getBancoCodigo()), 55, 248);
        pdfUtil.drawTextRight(cobranca.getLinhaDigitavel(), 540, 248);
        pdfUtil.setCurrentFont(PDType1Font.HELVETICA, 7.5f);

        // Recibo Pagador: Linha 1
        pdfUtil.drawText(beneficiario.getNome(), 52, 304);
        pdfUtil.drawTextRight(beneficiario.getCpfCnpj(), 325, 310);
        pdfUtil.drawTextRight(cobranca.getCodigoBeneficiario(), 424, 304);
        pdfUtil.drawTextCenter(cobranca.getDataDocumento().format(Util.LOCAL_DATE_FORMATTER_PTBR), 458, 304);
        pdfUtil.drawTextCenter(cobranca.getVencimento().format(Util.LOCAL_DATE_FORMATTER_PTBR), 518, 304);

        // Recibo Pagador: Linha 2
        pdfUtil.drawText(pagador.getNome(), 52, 287);
        pdfUtil.drawTextRight(cobranca.getNossoNumero(), 424, 287);
        pdfUtil.drawTextRight(cobranca.getValorDocumento(), 540, 287);

        // Ficha compensação
        pdfUtil.drawText(cobranca.getLocalPagamento(), 52, 230);
        pdfUtil.drawText(beneficiario.getNome(), 52, 214);
        pdfUtil.drawTextRight(beneficiario.getCpfCnpj(), 426, 214);

        pdfUtil.drawText(cobranca.getDataDocumento().format(Util.LOCAL_DATE_FORMATTER_PTBR), 52, 198);
        pdfUtil.drawText(cobranca.getNossoNumero(), 112, 198);
        pdfUtil.drawText(cobranca.getEspecieDocumento(), 210, 198);
        pdfUtil.drawText(cobranca.getAceite(), 290, 198);
        pdfUtil.drawText(cobranca.getDataProcessamento().format(Util.LOCAL_DATE_FORMATTER_PTBR), 350, 198);

        pdfUtil.drawTextCenter(cobranca.getCarteira(), 124, 182);
        pdfUtil.drawText(cobranca.getEspecieMoeda(), 142, 182);

        pdfUtil.drawTextRight(cobranca.getVencimento().format(Util.LOCAL_DATE_FORMATTER_PTBR), 540, 230);
        pdfUtil.drawTextRight(cobranca.getCodigoBeneficiario(), 540, 214);
        pdfUtil.drawTextRight(cobranca.getNossoNumero(), 540, 198);
        pdfUtil.drawTextRight(cobranca.getValorDocumento(), 540, 182);
        pdfUtil.drawTextRight(cobranca.getDesconto(), 540, 166);
        pdfUtil.drawTextRight(cobranca.getOutrasDeducoes(), 540, 150);
        pdfUtil.drawTextRight(cobranca.getMoraMultaJuros(), 540, 134);
        pdfUtil.drawTextRight(cobranca.getOutrosAcrescimos(), 540, 118);
        pdfUtil.drawTextRight(cobranca.getValorCobrado(), 540, 102);

        pdfUtil.setCurrentFontSize(6.5f);
        int y = 166;
        for (String instrucao : cobranca.getInstrucoes()) {
            pdfUtil.drawText(instrucao, 52, y);
            y -= 8;
        }

        pdfUtil.setCurrentFont(PDType1Font.HELVETICA_BOLD, 7f);
        pdfUtil.drawText(pagador.getNome(), 77, 93);

        pdfUtil.setCurrentFont(PDType1Font.HELVETICA, 7f);
        pdfUtil.drawText(pagador.getCpfCnpj(), 438, 86);

        List<String> dadosPagador = new ArrayList<>();
        dadosPagador.add(pagador.getEndereco().getLogradouroNumeroComplementoBairro());
        dadosPagador.add(pagador.getEndereco().getCepCidadeUf());
        pdfUtil.drawTextMultiline(dadosPagador, 77, 84, 8);
        pdfUtil.drawText(pagador.getNome(), 100, 63);

        pdfUtil.drawITF25Barcode(cobranca.getCodigoBarras(), 55, mmToDot(5), mmToDot(13));
    }
}
