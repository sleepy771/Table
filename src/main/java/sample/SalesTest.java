package sample;

import table.*;

import java.io.File;
import java.util.*;

public class SalesTest {
    public static void main(String[] args) throws ImporterException, RowFactoriesException {

        RowFactories.INSTANCE.register(SalesProxy.class, SalesPOJO.class);
        RowFactories.INSTANCE.register(SalesWithShare.class, SalesPOJOWithShare.class);

        TableBuilder<SalesProxy, SalesPOJO> builder = new TableBuilder<>(SalesProxy.class, SalesPOJO.class);
        builder.importTable(new File("data.csv"), new CsvBeanImporter<>(SalesPOJO.class));

        Table<SalesProxy, SalesPOJO> table = builder.build();

        Table<SalesProxy, SalesPOJO> filtered = table.filter(row -> row.getCountry().equals("Czech Republic") && row.getTimescale().equals("2010 Q4"));

        Map<String, Map<String, ?>> groupResults = filtered.groupBy(((row1, row2) -> true), "count").aggregate(rowGroup -> {
            final Map<String, Double> cnt = new HashMap<>();
            double sum = 0.;
            for (SalesProxy proxy : rowGroup) {
                sum += proxy.getUnits();
            }
            cnt.put("count", sum);
            return cnt;
        });

        final double sum = (Double) groupResults.get("count").get("count");

        GroupBy<SalesProxy> grouped = filtered.groupBy((row1, row2) -> row1 == row2 || row1.getVendor().equals(row2.getVendor()), SalesProxy::getVendor);

        Table<SalesProxy, SalesPOJO> aggregated = grouped.aggregate(AggWrapper.forClass(SalesProxy.class, SalesPOJO.class, (output, salesProxies) -> {
            output.setCountry(salesProxies.getRepresenting().getCountry());
            output.setTimescale(salesProxies.getRepresenting().getTimescale());
            output.setVendor(salesProxies.getRepresenting().getVendor());
            double s = 0.;
            for (SalesProxy sales : salesProxies) {
                s += sales.getUnits();
            }
            output.setUnits(s);
        }));

        Table<SalesWithShare, SalesPOJOWithShare> withShares = aggregated.apply(new AbstractForeach<SalesProxy, SalesWithShare, SalesPOJOWithShare>(SalesWithShare.class, SalesPOJOWithShare.class) {
            @Override
            public void applyOnRow(SalesPOJOWithShare output, SalesProxy row) {
                output.setVendor(row.getVendor());
                output.setTimescale(row.getTimescale());
                output.setUnits(row.getUnits());
                output.setCountry(row.getCountry());
                output.setShare(row.getUnits() / sum);
            }
        });

        LinkedHashMap<String, CellFormatter<SalesWithShare>> formatters = new LinkedHashMap<>();
        formatters.put("Vendor", SalesWithShare::getVendor);
        formatters.put("Units", row -> String.valueOf(row.getUnits()));
        formatters.put("Share", row -> String.valueOf(row.getShare() * 100) + " %");

        HTMLTableExporter<SalesWithShare> sharesExporter = new HTMLTableExporter<>(formatters);
        sharesExporter.setFooterFormatter(table1 -> {
            StringBuilder footerBuilder = new StringBuilder("<tr><td>Sum</td><td>");
            double units = 0.;
            double share = 0.;
            for (SalesWithShare row : table1) {
                units += row.getUnits();
                share += row.getShare();
            }
            footerBuilder.append(units).append("</td><td>").append(share * 100).append(" %</td></tr>");
            return footerBuilder;
        });

        System.out.println(withShares.export(sharesExporter));

    }
}
