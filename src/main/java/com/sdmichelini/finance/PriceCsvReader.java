package com.sdmichelini.finance;

import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;

public class PriceCsvReader {
    protected static List<DataPoint> readFromBufferedReader(BufferedReader reader) {
        return reader.lines().skip(1).map(PriceDataPoint::createFromCsvRow).collect(Collectors.toList());
    }
}
