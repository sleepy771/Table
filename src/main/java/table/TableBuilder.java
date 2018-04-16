package table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class TableBuilder<ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> {
    private List<ROW_BUILDER> rows;
    private final RowFactory<ROW, ROW_BUILDER> factory;

    public TableBuilder(Class<ROW> rowClass, Class<ROW_BUILDER> builderClass) throws RowFactoriesException {
        this.factory = RowFactories.INSTANCE.getFactoryFor(rowClass, builderClass);
        this.rows = new ArrayList<>();
    }

    public TableBuilder(RowFactory<ROW, ROW_BUILDER> factory) {
        this(new ArrayList<>(), factory);
    }

    TableBuilder(Table<ROW, ROW_BUILDER> table) {
        this(new ArrayList<>(), table.getRowFactory());
        for (ROW r : table) {
            this.rows.add(factory.fromExisting(r));
        }
    }

    TableBuilder(List<ROW_BUILDER> rows, RowFactory<ROW, ROW_BUILDER> factory) {
        this.rows = rows;
        this.factory = factory;
    }

    public void add(ROW row) {
        this.rows.add(this.factory.fromExisting(row));
    }

    public void expand(List<ROW> rows) {
        for (ROW r : rows) {
            this.add(r);
        }
    }

    public void addBuilder(ROW_BUILDER builder) {
        this.rows.add(builder);
    }

    public ROW_BUILDER createRow() {
        final ROW_BUILDER row = this.factory.create();
        this.rows.add(row);
        return row;
    }

    public RowBuilder<ROW> remove(int index) {
        return this.rows.remove(index);
    }

    public TableBuilder<ROW, ROW_BUILDER> filter(final FilterCondition<ROW> condition) {
        final List<ROW_BUILDER> filteredList = new ArrayList<>();
        this.rows.forEach((row) -> {
            final ROW r = row.build();
            if (condition.satisfies(r)) {
                filteredList.add(this.factory.fromExisting(r));
            }
        });
        this.rows.clear();
        this.rows = filteredList;
        return this;
    }

    public void clear() {
        this.rows.clear();
    }

    public final void sort(final Comparator<RowBuilder<ROW>> rowComparator) {
        this.rows.sort(rowComparator);
    }

    public final ROW_BUILDER setRow(int index, ROW_BUILDER row) {
        return this.rows.set(index, row);
    }

    public Table<ROW, ROW_BUILDER> build() {
        final List<ROW> rows = new ArrayList<>(this.rows.size());
        for (RowBuilder<ROW> r : this.rows) {
            rows.add(r.build());
        }
        return new Table<>(rows, this.factory);
    }

    public <TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> TableBuilder<TO, TO_BUILDER> apply(ForEach<ROW, TO, TO_BUILDER> forEach) {
        final List<TO_BUILDER> alteredRows = new ArrayList<>(this.rows.size());
        for (ROW_BUILDER row : this.rows) {
            alteredRows.add(forEach.apply(row.build()));
        }
        return new TableBuilder<>(alteredRows, forEach.getRowFactory());
    }

    public final <FROM_FORMAT> void importTable(FROM_FORMAT table, Importer<FROM_FORMAT, ROW, ROW_BUILDER> importer) throws ImporterException {
        Iterator<ROW_BUILDER> importerIterator = importer.iterator(table);
        while (importerIterator.hasNext()) {
            this.rows.add(importerIterator.next());
        }
    }
}
