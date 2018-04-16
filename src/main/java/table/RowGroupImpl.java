package table;

import java.util.Iterator;
import java.util.Optional;

public class RowGroupImpl<ROW extends Row<ROW>> implements RowGroup<ROW> {
    private final ROW representing;
    private final String groupName;
    private final Table<ROW, ? extends RowBuilder<ROW>> tableRef;
    private final Equivalence<ROW> rowEquivalence;

    RowGroupImpl(ROW representing, String name, Table<ROW, ? extends RowBuilder<ROW>> tableRef, Equivalence<ROW> rowEquivalence) {
        this.groupName = name;
        this.representing = representing;
        this.tableRef = tableRef;
        this.rowEquivalence = rowEquivalence;
    }

    @Override
    public ROW getRepresenting() {
        return this.representing;
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public Iterator<ROW> iterator() {
        return new RowGroupIter(this.tableRef.iterator(), this.rowEquivalence, this.representing);
    }

    private final class RowGroupIter implements Iterator<ROW> {

        private final Iterator<ROW> tableIter;
        private final ROW representing;
        private final Equivalence<ROW> equivalence;
        private  Optional<ROW> current;

        RowGroupIter(final Iterator<ROW> tableIter, final Equivalence<ROW> equivalence, final ROW representing) {
            this.tableIter = tableIter;
            this.equivalence = equivalence;
            this.representing = representing;
            this.current = Optional.empty();
        }

        @Override
        public boolean hasNext() {
            if (current.isPresent()) {
                return true;
            }
            return findNext();
        }

        @Override
        public ROW next() {
            if (current.isPresent() || findNext()) {
                try {
                    return current.get();
                } finally {
                    current = Optional.empty();
                }
            } else {
                return tableIter.next();
            }
        }

        private boolean findNext() {
            while (this.tableIter.hasNext()) {
                ROW next = this.tableIter.next();
                if (this.equivalence.areInRelation(representing, next)) {
                    current = Optional.of(next);
                    return true;
                }
            }
            current = Optional.empty();
            return false;
        }
    }
}
