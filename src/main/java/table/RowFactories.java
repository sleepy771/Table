package table;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class RowFactories {
    public static final RowFactories INSTANCE = new RowFactories();

    private final Map<RowClassPair, RowFactory> factories;

    private RowFactories() {
        factories = new HashMap<>();
    }

    public <R extends Row<R>, B extends RowBuilder<R>> void register(final Class<R> rowClass, final Class<B> builderClass, RowFactory<R, B> factory) throws RowFactoriesException {
        RowClassPair<R, B> pair = new RowClassPair<>(rowClass, builderClass);

        if (this.factories.containsKey(pair)) {
            throw new RowFactoriesException(String.format("Factory for Row<%s, %s> already registered", rowClass, builderClass));
        }

        this.factories.put(pair, factory);
    }


    public <R extends Row<R>, B extends RowBuilder<R>> void register(final Class<R> rowClass, final Class<B> builderClass) throws RowFactoriesException {
        RowClassPair<R, B> pair = new RowClassPair<>(rowClass, builderClass);

        if (this.factories.containsKey(pair)) {
            throw new RowFactoriesException(String.format("Factory for Row<%s, %s> already registered", rowClass, builderClass));
        }

        this.factories.put(pair, new RowFactory() {
            @Override
            public RowBuilder create() {
                try {
                    return builderClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public <R extends Row<R>, B extends RowBuilder<R>> RowFactory<R, B> getFactoryFor(Class<R> rowClass, Class<B> builderClass) throws RowFactoriesException {
        RowClassPair<R, B> pair = new RowClassPair<>(rowClass, builderClass);

        if (!this.factories.containsKey(pair)) {
            throw new RowFactoriesException(String.format("Factory for (%s, %s) not found!", rowClass, builderClass));
        }

        return (RowFactory<R, B>) this.factories.get(pair);
    }

    private class RowClassPair<R extends Row<R>, B extends RowBuilder<R>> {
        private final Class<R> rowClass;
        private final Class<B> builderClass;

        RowClassPair(Class<R> rowClass, Class<B> builderClass) {
            this.rowClass = rowClass;
            this.builderClass = builderClass;
        }

        @Override
        public int hashCode() {
            int hashCode = 17;
            hashCode = 31 * hashCode + this.rowClass.hashCode();
            hashCode = 31 * hashCode + this.builderClass.hashCode();
            return hashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RowClassPair)) return false;
            RowClassPair<?, ?> that = (RowClassPair<?, ?>) o;
            return Objects.equals(rowClass, that.rowClass) &&
                    Objects.equals(builderClass, that.builderClass);
        }
    }
}
