package table;

import java.io.FileNotFoundException;
import java.util.Iterator;

public interface Importer<FROM_FORMAT, ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> {
    Iterator<ROW_BUILDER> iterator(FROM_FORMAT table) throws ImporterException;
}
