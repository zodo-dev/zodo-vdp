package dev.zodo.vdp.cobranca.model;

import dev.zodo.vdp.cobranca.exceptions.VdpCobrancaUncheckedException;
import dev.zodo.vdp.cobranca.util.Banco;
import dev.zodo.vdp.cobranca.util.CobrancaUtil;
import dev.zodo.vdp.cobranca.util.Util;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cobranca {
    private LocalDate vencimento;
    private LocalDate dataProcessamento;
    private LocalDate dataDocumento;

    private String valorDocumento;
    private String desconto;
    private String outrasDeducoes;
    private String moraMultaJuros;
    private String outrosAcrescimos;
    private String valorCobrado;
    private String linhaDigitavel;
    private String codigoBarras;
    private String nossoNumero;
    private String localPagamento;
    private String carteira;
    private String codigoBeneficiario;
    private String bancoCodigo;
    private String bancoNome;
    private String aceite;
    private String especieDocumento;
    private String especieMoeda;
    private List<String> instrucoes;
    private Banco banco;

    public Cobranca configureAfterSetValues() {
        configureCodigoBarras();
        configureLinhaDigitavel();
        configureBanco();
        return this;
    }

    private void configureCodigoBarras() {
        if (Util.isBlank(codigoBarras)) {
            codigoBarras = CobrancaUtil.codigoBarrasFromLinhaDigitavel(linhaDigitavel);
        }
    }

    private void configureLinhaDigitavel() {
        if (Util.isBlank(linhaDigitavel)) {
            linhaDigitavel = CobrancaUtil.linhaDigitavelFromCodigoBarras(codigoBarras);
        }
    }

    private void configureBanco() {
        if (Util.isNotBlank(bancoCodigo) && Util.isNotBlank(bancoNome)) {
            return;
        }
        if (Util.isNotBlank(bancoCodigo)) {
            banco = Banco.fromCodigo(bancoCodigo.substring(0, 3));
        } else if (Util.isNotBlank(codigoBarras)) {
            banco = Banco.fromCodigo(codigoBarras.substring(0, 3));
        } else {
            banco = Banco.INDEFINIDO;
            throw new VdpCobrancaUncheckedException(String.format("Banco não configurado. [%s]", bancoCodigo));
        }
        if (Util.isBlank(bancoCodigo)) {
            bancoCodigo = banco.getCodigo() + "-" + banco.getDigito();
        }
        if (Util.isBlank(bancoNome)) {
            bancoNome = banco.getNome();
        }
        banco.configurarCobranca(this);
    }
}
