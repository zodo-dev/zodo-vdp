package dev.zodo.vdp.cobranca.report;

import br.com.tecsinapse.dataio.ExporterFormatter;
import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.type.FileType;
import br.com.tecsinapse.dataio.util.ExporterUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReportUtil {
    public static <T> void generateReportCobranca(List<T> registros, List<ReportColumn<T>> cols, OutputStream outputStream) throws IOException {
        Table table = new Table(ExporterFormatter.BRAZILIAN);
        ReportColumn.addColumnHeader(table, cols);
        registros.forEach(reg -> {
            table.addNewRow();
            for (ReportColumn<T> col : cols) {
                col.addColumn(table, reg);
            }
        });
        ExporterUtil.writeData(List.of(table), FileType.SXLSX, outputStream, null, null);
    }
}
