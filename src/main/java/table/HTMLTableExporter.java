package table;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HTMLTableExporter<ROW extends Row<ROW>> implements Exporter<ROW, String> {
    private final LinkedHashMap<String, CellFormatter<ROW>> formaters;
    private Optional<HTMLFooterFormatter<ROW>> footerFormatter;

    public HTMLTableExporter(final List<String> columns, final Map<String, CellFormatter<ROW>> formaters) {
        this.footerFormatter = Optional.empty();
        this.formaters = new LinkedHashMap<>();
        for (String column : columns) {
            this.formaters.put(column, formaters.get(column));
        }
    }

    public HTMLTableExporter(final LinkedHashMap<String, CellFormatter<ROW>> formatters) {
        this.formaters = formatters;
        this.footerFormatter = Optional.empty();
    }

    public final void setFooterFormatter(final HTMLFooterFormatter<ROW> formatter) {
        this.footerFormatter = Optional.of(formatter);
    }

    public final void unsetFooterFormatter() {
        this.footerFormatter = Optional.empty();
    }

    @Override
    public String export(final Table<ROW, ? extends RowBuilder<ROW>> table) {
        final StringBuilder htmlTableBuilder = new StringBuilder("<table>");
        htmlTableBuilder.append("<thead><tr>").append(this.createHeader()).append("</tr></thead>");
        htmlTableBuilder.append("<tbody>");
        for (ROW row : table) {
            htmlTableBuilder.append("<tr>").append(this.createRow(row)).append("</tr>");
        }
        htmlTableBuilder.append("</tbody>");
        footerFormatter.ifPresent(htmlFooterFormatter -> htmlTableBuilder.append("<tfoot>")
                .append(htmlFooterFormatter.createFooter(table))
                .append("</tfoot>"));
        htmlTableBuilder.append("</table>");
        return htmlTableBuilder.toString();
    }

    private StringBuilder createHeader() {
        final StringBuilder headerBuilder = new StringBuilder();
        for (String column : this.formaters.keySet()) {
            headerBuilder.append("<th>").append(column).append("</th>");
        }
        return headerBuilder;
    }

    private StringBuilder createRow(final ROW row) {
        final StringBuilder rowBuilder = new StringBuilder();
        for (final Map.Entry<String, CellFormatter<ROW>> entry : this.formaters.entrySet()) {
            final CellFormatter<ROW> formatter = entry.getValue();
            rowBuilder.append("<td>").append(formatter.formatCell(row)).append("</td>");
        }
        return rowBuilder;
    }
}
