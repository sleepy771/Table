package table;

public interface FilterCondition<ROW extends Row<ROW>> {
    boolean satisfies(final ROW row);
}
