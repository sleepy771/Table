package table;

public interface ForEach<FROM extends Row<FROM>, TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> {
    TO_BUILDER apply(FROM row);

    RowFactory<TO, TO_BUILDER> getRowFactory();
}
