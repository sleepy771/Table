package table;

import java.util.*;

public class GroupBy<FROM extends Row<FROM>> {

    private final Equivalence<FROM> rowEquivalence;
    private final GroupNameGenerator<FROM> nameGenerator;
    private final List<RowGroup<FROM>> groups;
    private final Table<FROM, ? extends RowBuilder<FROM>> table;

    GroupBy(Table<FROM, ? extends RowBuilder<FROM>> table, Equivalence<FROM> equivalence, GroupNameGenerator<FROM> nameGenerator) {
        this.rowEquivalence = equivalence;
        this.groups = new ArrayList<>();
        this.nameGenerator = nameGenerator;
        this.table = table;
    }

    public <TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> Table<TO, TO_BUILDER> aggregate(Aggregator<FROM, TO, TO_BUILDER> aggregator) {
        if (this.groups.isEmpty()) {
            this.groupIndices();
        }
        final TableBuilder<TO, TO_BUILDER> tableBuilder = new TableBuilder<>(aggregator.getRowFactory());
        for (RowGroup<FROM> group : this.groups) {
            tableBuilder.addBuilder(aggregator.apply(group));
        }
        return tableBuilder.build();
    }

    public Map<String, Map<String, ?>> aggregate(ResultAggregator<FROM> aggregator) {
        if (this.groups.isEmpty()) {
            this.groupIndices();
        }
        final Map<String, Map<String, ?>> results = new HashMap<>();
        for (RowGroup<FROM> group : this.groups) {
            results.put(group.getGroupName(), aggregator.aggregate(group));
        }
        return results;
    }

    private void groupIndices() {
        for (FROM row : this.table) {
            boolean foundGroup = false;
            for (RowGroup<FROM> rg : this.groups) {
                foundGroup = this.rowEquivalence.areInRelation(rg.getRepresenting(), row);
                if (foundGroup) {
                    break;
                }
            }
            if (!foundGroup) {
                this.groups.add(new RowGroupImpl<>(row, this.nameGenerator.generateName(row), this.table, this.rowEquivalence));
            }
        }
    }
}
