package sample;

import table.Row;

public class SalesWithShare implements Row<SalesWithShare> {

    private final String vendor;
    private final String country;
    private final String timescale;
    private final double units;
    private final double share;

    SalesWithShare(final String country, final String timescale, final String vendor, final double units, final double share) {
        this.vendor = vendor;
        this.country = country;
        this.timescale = timescale;
        this.units = units;
        this.share = share;
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

    public double getShare() {
        return this.share;
    }

    @Override
    public SalesWithShare copy() {
        return new SalesWithShare(country, timescale, vendor, units, share);
    }
}
