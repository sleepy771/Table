package table;

public interface RowBuilder<ROW extends Row<ROW>> {
    ROW build();

    void fromExisting(ROW data);

    void fromStrings(final String[] values);
}
