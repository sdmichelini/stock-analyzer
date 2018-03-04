package com.sdmichelini.finance;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataPointTests {
    private static Date addDays(Date d, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    private static List<DataPoint> makeDataPoints(Date startDate, double... values) {
        int days = 0;
        List<DataPoint> ret = new ArrayList<>();
        for(double d: values) {
            ret.add(new DataPoint(addDays(startDate, days), d));
            days++;
        }
        return ret;
    }

    @Test
    public void twoListsAdded() {
        Date d = new Date();
        List<DataPoint> dp1 = makeDataPoints(d, 12.0, 24.0);
        List<DataPoint> dp2 = makeDataPoints(d, 12.0, 24.0);

        List<DataPoint> added = DataPoint.addLists(dp1, dp2);
        assertNotNull(added);
        assertEquals(24.0, added.get(0).value, 0.01);
        assertEquals(48.0, added.get(1).value, 0.01);
    }

    @Test
    public void twoListsAddedNotOfSameSize() {
        Date d = new Date();
        List<DataPoint> dp1 = makeDataPoints(d, 12.0, 24.0, 36.0);
        List<DataPoint> dp2 = makeDataPoints(d, 12.0, 24.0);

        List<DataPoint> added = DataPoint.addLists(dp1, dp2);
        assertNotNull(added);
        assertEquals(24.0, added.get(0).value, 0.01);
        assertEquals(48.0, added.get(1).value, 0.01);
        assertEquals(2, added.size());
    }

    @Test
    public void dataPointsToMap() {
        Date d = new Date();
        List<DataPoint> dp1 = makeDataPoints(d, 12.0, 24.0);
        List<DataPoint> dp2 = makeDataPoints(d, 12.0, 24.0);

        Map<Date, List<Double>> ret = DataPoint.mergeDataPointCollection(dp1, dp2);

        assertEquals( 2, ret.size());
        assertEquals(2, ret.size());
        assertEquals(2, ret.get(d).size());
        assertEquals(12.0, ret.get(d).get(0), 0.01);
        assertEquals(12.0, ret.get(d).get(1), 0.01);
        assertEquals(24.0, ret.get(addDays(d, 1)).get(0), 0.01);
        assertEquals(24.0, ret.get(addDays(d, 1)).get(1), 0.01);
    }

    @Test
    public void dataPointsToMapExtraDay() {
        Date d = new Date();
        List<DataPoint> dp1 = makeDataPoints(d, 12.0, 24.0, 36.0);
        List<DataPoint> dp2 = makeDataPoints(d, 12.0, 24.0);

        Map<Date, List<Double>> ret = DataPoint.mergeDataPointCollection(dp1, dp2);

        assertEquals( 2, ret.size());
        assertEquals(2, ret.size());
        assertEquals(2, ret.get(d).size());
        assertEquals(12.0, ret.get(d).get(0), 0.01);
        assertEquals(12.0, ret.get(d).get(1), 0.01);
        assertEquals(24.0, ret.get(addDays(d, 1)).get(0), 0.01);
        assertEquals(24.0, ret.get(addDays(d, 1)).get(1), 0.01);
    }
}
