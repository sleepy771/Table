package table;

public interface Equivalence<ROW extends Row<ROW>> {
    boolean areInRelation(ROW row1, ROW row2);
}
