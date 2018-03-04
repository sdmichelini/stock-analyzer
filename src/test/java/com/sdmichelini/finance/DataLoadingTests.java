package com.sdmichelini.finance;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataLoadingTests {
    @Test(expected = IllegalArgumentException.class)
    public void errorIfNotEnoughColumns() {
        PriceDataPoint.createFromCsvRow("2018-03-02,91.5800,93.1500,90.8600,93.0500,93.0500");
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorIfImproperDate() {
        PriceDataPoint.createFromCsvRow("2018-03,91.5800,93.1500,90.8600,93.0500,93.0500,32208063,0.0000,1.0000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void errorIfUnparseableClosePrice() {
        PriceDataPoint.createFromCsvRow("2018-03-02,91.5800,93.1500,90.8600,93.0500,xyz,32208063,0.0000,1.0000");
    }

    @Test
    public void parsesRowProperly() {
        PriceDataPoint p = PriceDataPoint.createFromCsvRow("2018-03-02,91.5800,93.1500,90.8600,93.0500,93.0500,32208063,0.0000,1.0000");
        assertEquals(93.05, p.getClose(), 0.01);
        Calendar c = Calendar.getInstance();
        c.setTime(p.date);
        assertEquals(2, c.get(Calendar.MONTH));
        assertEquals(2, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(2018, c.get(Calendar.YEAR));
    }

    @Test
    public void loadsFile() {
        IDataPointLoader dpl = new FileDataPointLoader("test_data.csv");
        List<DataPoint> dps = dpl.loadDataPoints();
        assertEquals(93.05, dps.get(0).value, 0.01);
        Calendar c = Calendar.getInstance();
        c.setTime(dps.get(0).date);
        assertEquals(2, c.get(Calendar.MONTH));
        assertEquals(2, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(2018, c.get(Calendar.YEAR));
    }
}
