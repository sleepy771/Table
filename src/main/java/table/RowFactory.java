package table;

public abstract class RowFactory<ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> {
    public abstract ROW_BUILDER create();

    public final ROW_BUILDER fromExisting(ROW row) {
        ROW_BUILDER builder = this.create();
        builder.fromExisting(row);
        return builder;
    }
}
