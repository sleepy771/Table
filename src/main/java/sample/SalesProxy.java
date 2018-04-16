package sample;

import table.Row;

public final class SalesProxy implements Row<SalesProxy> {

    private final String vendor;
    private final String country;
    private final String timescale;
    private final double units;

    SalesProxy(final String country, final String timescale, final String vendor, final double units) {
        this.vendor = vendor;
        this.country = country;
        this.timescale = timescale;
        this.units = units;
    }

    public String getCountry() {
        return this.country;
    }

    public String getTimescale() {
        return this.timescale;
    }

    public double getUnits() {
        return this.units;
    }

    public String getVendor() {
        return this.vendor;
    }

    @Override
    public SalesProxy copy() {
        return new SalesProxy(this.country, this.timescale, this.vendor, this.units);
    }
}
