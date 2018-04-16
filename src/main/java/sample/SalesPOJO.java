package sample;

import com.opencsv.bean.CsvBindByName;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import table.RowBuilder;

public final class SalesPOJO implements RowBuilder<SalesProxy> {

    @CsvBindByName(column = "Timescale", required = true)
    private String timescale;

    @CsvBindByName(column = "Vendor", required = true)
    private String vendor;

    @CsvBindByName(column = "Country", required = true)
    private String country;

    @CsvBindByName(column = "Units", required = true)
    private double units;

    public String getTimescale() {
        return timescale;
    }

    public void setTimescale(String timescale) {
        this.timescale = timescale;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    @Override
    public SalesProxy build() {
        return new SalesProxy(this.country, this.timescale, this.vendor, this.units);
    }

    @Override
    public void fromExisting(SalesProxy data) {
        this.country = data.getCountry();
        this.timescale = data.getTimescale();
        this.units = data.getUnits();
        this.vendor = data.getVendor();
    }

    @Override
    public void fromStrings(String[] values) {
        throw new NotImplementedException();
    }
}
