package table;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

public class CsvBeanImporter<ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> implements Importer<File, ROW, ROW_BUILDER> {

    private final Class<ROW_BUILDER> builderClass;

    public CsvBeanImporter(Class<ROW_BUILDER> builderClass) {
        this.builderClass = builderClass;
    }

    @Override
    public Iterator<ROW_BUILDER> iterator(File fileDescriptor) throws ImporterException {
        CsvToBeanBuilder<ROW_BUILDER> csvToBeanBuilder = null;
        try {
            csvToBeanBuilder = new CsvToBeanBuilder<>(new FileReader(fileDescriptor));
            List<ROW_BUILDER> rows = csvToBeanBuilder.withType(builderClass).build().parse();
            return rows.iterator();
        } catch (FileNotFoundException | IllegalStateException e) {
            throw new ImporterException(e);
        }
    }
}
