package table;

import java.util.Map;

public interface ResultAggregator<ROW extends Row<ROW>> {
    Map<String, ?> aggregate(RowGroup<ROW> rowGroup);
}
