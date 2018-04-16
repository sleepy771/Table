package table;

public interface HTMLFooterFormatter<ROW extends Row<ROW>> {
    StringBuilder createFooter(Table<ROW, ? extends RowBuilder<ROW>> table);
}
