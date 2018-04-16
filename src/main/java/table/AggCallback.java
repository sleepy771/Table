package table;

public interface AggCallback<ROW extends Row<ROW>, BUILDER extends RowBuilder<ROW>> {
    void aggregate(BUILDER output, RowGroup<ROW> rows);
}
