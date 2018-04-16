package table;

public interface CellFormatter<ROW extends Row<ROW>> {
    String formatCell(ROW row);
}
