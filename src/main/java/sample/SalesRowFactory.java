package sample;

import table.RowFactory;

public final class SalesRowFactory extends RowFactory<SalesProxy, SalesPOJO> {
    @Override
    public SalesPOJO create() {
        return new SalesPOJO();
    }
}
