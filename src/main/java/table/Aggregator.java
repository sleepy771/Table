package table;

import java.util.List;

public interface Aggregator<FROM extends Row<FROM>, TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> {
    TO_BUILDER apply(RowGroup<FROM> rowGroup);

    RowFactory<TO, TO_BUILDER> getRowFactory();
}
