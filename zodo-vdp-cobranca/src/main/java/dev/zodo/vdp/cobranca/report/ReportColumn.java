package dev.zodo.vdp.cobranca.report;

import br.com.tecsinapse.dataio.Table;
import br.com.tecsinapse.dataio.style.Style;

import java.util.List;

public interface ReportColumn<D> {

    void addColumn(Table table, D d);

    String getHeader();

    static <O> void addColumnHeader(Table table, List<ReportColumn<O>> reportColumns) {
        table.addNewRow();
        reportColumns.forEach(t -> table.add(t.getHeader(),
                Style.TABLE_CELL_STYLE_HEADER_BOLD));
    }
}
