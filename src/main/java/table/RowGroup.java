package table;

public interface RowGroup<ROW extends Row<ROW>> extends Iterable<ROW> {

    ROW getRepresenting();

    String getGroupName();
}
