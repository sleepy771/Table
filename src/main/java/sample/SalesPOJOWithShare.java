package sample;

import com.opencsv.bean.CsvBindByName;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import table.RowBuilder;

public final class SalesPOJOWithShare implements RowBuilder<SalesWithShare> {

    @CsvBindByName(column = "Timescale", required = true)
    private String timescale;

    @CsvBindByName(column = "Vendor", required = true)
    private String vendor;

    @CsvBindByName(column = "Country", required = true)
    private String country;

    @CsvBindByName(column = "Units", required = true)
    private double units;

    @CsvBindByName(column = "Share", required = true)
    private double share;

    public String getTimescale() {
        return timescale;
    }

    public SalesPOJOWithShare setTimescale(String timescale) {
        this.timescale = timescale;
        return this;
    }

    public String getVendor() {
        return vendor;
    }

    public SalesPOJOWithShare setVendor(String vendor) {
        this.vendor = vendor;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public SalesPOJOWithShare setCountry(String country) {
        this.country = country;
        return this;
    }

    public double getUnits() {
        return units;
    }

    public SalesPOJOWithShare setUnits(double units) {
        this.units = units;
        return this;
    }

    public double getShare() {
        return share;
    }

    public SalesPOJOWithShare setShare(double share) {
        this.share = share;
        return this;
    }

    @Override
    public SalesWithShare build() {
        return new SalesWithShare(country, timescale, vendor, units, share);
    }

    @Override
    public void fromExisting(SalesWithShare data) {
        setCountry(data.getCountry());
        setShare(data.getShare());
        setUnits(data.getUnits());
        setTimescale(data.getTimescale());
        setVendor(data.getVendor());
    }

    @Override
    public void fromStrings(String[] values) {
        throw new NotImplementedException();
    }
}
