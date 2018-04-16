package table;

public final class AggWrapper<ROW extends Row<ROW>, ROW_BUILDER extends RowBuilder<ROW>> implements Aggregator<ROW, ROW, ROW_BUILDER> {

    private final Class<ROW> rowClass;
    private final Class<ROW_BUILDER> builderClass;
    private final AggCallback<ROW, ROW_BUILDER> callback;

    public AggWrapper(Class<ROW> rowClass, Class<ROW_BUILDER> builderClass, AggCallback<ROW, ROW_BUILDER> callback) {
        this.callback = callback;
        this.rowClass = rowClass;
        this.builderClass = builderClass;
    }

    @Override
    public ROW_BUILDER apply(final RowGroup<ROW> rowGroup) {
        final ROW_BUILDER builder = this.getRowFactory().create();
        this.callback.aggregate(builder, rowGroup);
        return builder;
    }

    @Override
    public RowFactory<ROW, ROW_BUILDER> getRowFactory() {
        try {
            return RowFactories.INSTANCE.getFactoryFor(this.rowClass, this.builderClass);
        } catch (RowFactoriesException e) {
            throw new RuntimeException(e); // because other implementations does not expect to not have factory present.
        }
    }

    public static <R extends Row<R>, B extends RowBuilder<R>> AggWrapper<R, B> forClass(Class<R> row, Class<B> builder, AggCallback<R, B> callback) {
        return new AggWrapper<>(row, builder, callback);
    }
}
