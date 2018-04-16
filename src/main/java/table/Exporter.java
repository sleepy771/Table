package table;

public interface Exporter<ROW extends Row<ROW>, FORMAT> {
    FORMAT export(Table<ROW, ? extends RowBuilder<ROW>> table);
}
