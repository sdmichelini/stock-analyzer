package com.sdmichelini.finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PriceDataPoint extends DataPoint {

    public PriceDataPoint(Date d, double close) {
        super(d, close);
    }

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static PriceDataPoint createFromCsvRow(String csvRow) {
        String[] dataPoints = csvRow.split(",");
        if(dataPoints.length < 9) {
            throw new IllegalArgumentException("CSV Row had less than expected column count");
        }
        try {
            Date d = DATE_FORMAT.parse(dataPoints[0]);
            double close = Double.valueOf(dataPoints[5]);
            return new PriceDataPoint(d, close);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse date: " + dataPoints[0]);
        }
    }

    public final double getClose() {
        return super.value;
    }
}
