package table;

public abstract class AbstractForeach<FROM extends Row<FROM>, TO extends Row<TO>, TO_BUILDER extends RowBuilder<TO>> implements ForEach<FROM, TO, TO_BUILDER> {

    private final Class<TO> rowClass;
    private final Class<TO_BUILDER> builderClass;

    public AbstractForeach(Class<TO> rowClass, Class<TO_BUILDER> builderClass) {
        this.rowClass = rowClass;
        this.builderClass = builderClass;
    }

    public abstract void applyOnRow(TO_BUILDER output, FROM row);

    @Override
    public final TO_BUILDER apply(FROM row) {
        TO_BUILDER builder = getRowFactory().create();
        applyOnRow(builder, row);
        return builder;
    }

    @Override
    public final RowFactory<TO, TO_BUILDER> getRowFactory() {
        try {
            return RowFactories.INSTANCE.getFactoryFor(this.rowClass, this.builderClass);
        } catch (RowFactoriesException e) {
            throw new RuntimeException(e);
        }
    }
}
