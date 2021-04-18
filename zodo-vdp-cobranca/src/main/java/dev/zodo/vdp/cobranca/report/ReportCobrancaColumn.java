package dev.zodo.vdp.cobranca.report;

import br.com.tecsinapse.dataio.Table;
import dev.zodo.vdp.cobranca.model.Boleto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

@Getter
@RequiredArgsConstructor
public enum ReportCobrancaColumn implements ReportColumn<Boleto> {
    SEQUENCIAL("Sequencial") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getSequencialCode());
        }
    },
    BENEFICIARIO("Beneficiário") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getBeneficiario().getNome());
        }
    },
    BENEFICIARIO_CODIGO("Código beneficiário") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getCobranca().getCodigoBeneficiario());
        }
    },
    PAGADOR("Pagador") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getPagador().getNome());

        }
    },
    CPF_CNPJ("CPF/CNPJ") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getPagador().getCpfCnpj());
        }
    },
    VENCIMENTO("Vencimento") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            LocalDate vencimento = boleto.getCobranca().getVencimento();
            table.add(vencimento != null ? Date.from(vencimento.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) : "");
        }
    },
    VALOR("Valor") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            String valor = boleto.getCobranca().getValorDocumento();
            try {
                Number number = NumberFormat.getNumberInstance(Locale.forLanguageTag("pt-BR")).parse(valor);
                table.add(number);
            } catch (ParseException e) {
                table.add(valor);
            }
        }
    },
    NOSSO_NUMERO("Nosso número") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getCobranca().getNossoNumero());
        }
    },
    CODIGO_BARRAS("Código de Barras") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getCobranca().getCodigoBarras());
        }
    },
    LINHA_DIGITAVEL("Linha digitável") {
        @Override
        public void addColumn(Table table, Boleto boleto) {
            table.add(boleto.getCobranca().getLinhaDigitavel());
        }
    };

    private final String header;

}
