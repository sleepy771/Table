package table;

public interface GroupNameGenerator<ROW extends Row<ROW>> {
    String generateName(ROW row);
}
