package table;

public abstract class AbstAgg<FROM extends Row<FROM>, TO extends Row<TO>, B extends RowBuilder<TO>> implements Aggregator<FROM, TO, B> {

    private final Class<TO> rowClass;
    private final Class<B> builderClass;

    public AbstAgg(Class<TO> rowClass, Class<B> builderClass) {
        this.rowClass = rowClass;
        this.builderClass = builderClass;
    }

    @Override
    public final B apply(RowGroup<FROM> rowGroup) {
        B builder = getRowFactory().create();
        return applyOnGroup(builder, rowGroup);
    }

    protected abstract B applyOnGroup(B builder, RowGroup<FROM> group);

    @Override
    public final RowFactory<TO, B> getRowFactory() {
        try {
            return RowFactories.INSTANCE.getFactoryFor(this.rowClass, this.builderClass);
        } catch (RowFactoriesException e) {
            throw new RuntimeException(e);
        }
    }
}
