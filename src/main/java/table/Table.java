package table;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public final class Table<ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> implements Copyable<Table<ROW, ROW_BUILDER>>, Iterable<ROW> {

    private final List<ROW> rows;
    private final RowFactory<ROW, ROW_BUILDER> factory;

    private class TableIterator implements Iterator<ROW> {

        private final Iterator<ROW> internalIteratpr;

        TableIterator(Iterator<ROW> listIterator) {
            this.internalIteratpr = listIterator;
        }

        @Override
        public boolean hasNext() {
            return internalIteratpr.hasNext();
        }

        @Override
        public ROW next() {
            // Maybe create copy of row.
            return this.internalIteratpr.next();
        }
    }

    Table(List<ROW> rows, RowFactory<ROW, ROW_BUILDER> factory) {
        this.rows = rows;
        this.factory = factory;
    }

    public final Table<ROW, ROW_BUILDER> sort(final Comparator<ROW> rowComparator) {
        Table<ROW, ROW_BUILDER> tableCopy = this.copy();
        tableCopy.rows.sort(rowComparator);
        return tableCopy;
    }

    public ROW getRow(int index) {
        return this.rows.get(index);
    }

    @Override
    public Table<ROW, ROW_BUILDER> copy() {
        List<ROW> clonedRows = new ArrayList<>();
        for (ROW r : this.rows) {
            clonedRows.add(r.copy());
        }
        Table<ROW, ROW_BUILDER> copy = new Table<>(this.rows, this.factory);
        return copy;
    }

    @Override
    public Iterator<ROW> iterator() {
        return new TableIterator(this.rows.iterator());
    }

    public final GroupBy<ROW> groupBy(final Equivalence<ROW> rowEquivalence, final GroupNameGenerator<ROW> groupNameGenerator) {
        return new GroupBy<>(this, rowEquivalence, groupNameGenerator);
    }

    public final GroupBy<ROW> groupBy(final Equivalence<ROW> rowEquivalence, final String groupName) {
        return this.groupBy(rowEquivalence, row -> groupName);
    }

    public final Table<ROW, ROW_BUILDER> filter(final FilterCondition<ROW> condition) {
        final TableBuilder<ROW, ROW_BUILDER> builder = new TableBuilder<>(this.factory);
        this.forEach((row) -> {
            if (condition.satisfies(row)) {
                builder.add(row);
            }
        });
        return builder.build();
    }

    RowFactory<ROW, ROW_BUILDER> getRowFactory() {
        return this.factory;
    }

    public final TableBuilder<ROW, ROW_BUILDER> toBuilder() {
        return new TableBuilder<>(this);
    }

    public final <TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> Table<TO, TO_BUILDER> apply(ForEach<ROW, TO, TO_BUILDER> forEach) {
        return this.toBuilder().apply(forEach).build();
    }

    public <FORMAT> FORMAT export(Exporter<ROW, FORMAT> exporter) {
        return exporter.export(this);
    }
}
