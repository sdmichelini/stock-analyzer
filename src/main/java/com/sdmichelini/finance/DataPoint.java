package com.sdmichelini.finance;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataPoint {
    public final Date date;
    public final double value;

    public DataPoint(Date d, double v) {
        this.date = d;
        this.value = v;
    }

    public final Date getDate() {
        return date;
    }

    public final double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{Date: " + PriceDataPoint.DATE_FORMAT.format(date) + ", Value: " + value + "}";
    }

    static void checkDates(DataPoint dp1, DataPoint dp2) {
        if(!dp1.getDate().equals(dp2.getDate())) {
            throw new IllegalArgumentException("Different dates: " + dp1 + ", " +  dp2);
        }
    }

    static DataPoint add(DataPoint dp1, DataPoint dp2) {
        checkDates(dp1, dp2);
        return new DataPoint(dp1.getDate(), dp1.value + dp2.value);
    }

    static DataPoint subtract(DataPoint dp1, DataPoint dp2) {
        checkDates(dp1, dp2);
        return new DataPoint(dp1.getDate(), dp1.value - dp2.value);
    }

    static List<DataPoint> getStandardDeviation(List<DataPoint> inputList) {
        return getStandardDeviation(inputList, 20);
    }

    static List<DataPoint> getStandardDeviation(List<DataPoint> inputList, int windowSize) {
        inputList.sort(Comparator.comparing(DataPoint::getDate));
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.setWindowSize(windowSize);
        List<DataPoint> ret = new ArrayList<>();
        for(DataPoint p: inputList) {
            stats.addValue(p.value);
            ret.add(new DataPoint(p.date, stats.getStandardDeviation()));
        }
        return ret;
    }

    static List<DataPoint> getMean(List<DataPoint> inputList) {
        return getMean(inputList, 20);
    }

    static List<DataPoint> getMean(List<DataPoint> inputList, int windowSize) {
        inputList.sort(Comparator.comparing(DataPoint::getDate));
        DescriptiveStatistics stats = new DescriptiveStatistics();
        stats.setWindowSize(windowSize);
        List<DataPoint> ret = new ArrayList<>();
        for(DataPoint p: inputList) {
            stats.addValue(p.value);
            ret.add(new DataPoint(p.date, stats.getMean()));
        }
        return ret;
    }

    static Map<Date, DataPoint> convertListToMap(List<DataPoint> dpList) {
        return dpList.stream().collect(Collectors.toMap(DataPoint::getDate, Function.identity()));
    }

    static List<DataPoint> addLists(List<DataPoint> pointsA, List<DataPoint> pointsB) {
        Map<Date, DataPoint> aDates = convertListToMap(pointsA);
        Map<Date, DataPoint> bDates = convertListToMap(pointsB);

        List<DataPoint> ret = new ArrayList<>();

        for(Date d: aDates.keySet()) {
            if(bDates.keySet().contains(d)) {
                ret.add(add(aDates.get(d), bDates.get(d)));
            }
        }
        ret.sort(Comparator.comparing(DataPoint::getDate));
        return ret;
    }

    static List<DataPoint> subtractLists(List<DataPoint> pointsA, List<DataPoint> pointsB) {
        Map<Date, DataPoint> aDates = convertListToMap(pointsA);
        Map<Date, DataPoint> bDates = convertListToMap(pointsB);

        List<DataPoint> ret = new ArrayList<>();

        for(Date d: aDates.keySet()) {
            if(bDates.keySet().contains(d)) {
                ret.add(subtract(aDates.get(d), bDates.get(d)));
            }
        }
        ret.sort(Comparator.comparing(DataPoint::getDate));
        return ret;
    }
    @SafeVarargs
    static Map<Date, List<Double>> mergeDataPointCollection(List<DataPoint> ... dataPointLists) {
        List<Map<Date, DataPoint>> maps = Arrays.stream(dataPointLists)
                .map(DataPoint::convertListToMap)
                .collect(Collectors.toList());
        Map<Date, List<Double>> ret = new TreeMap<>();
        if(maps.size() > 0) {
            for(Date d: maps.get(0).keySet()) {
                boolean allHave = true;
                for(Map<Date, DataPoint> innerMap : maps) {
                    if(!innerMap.keySet().contains(d)) {
                        allHave = false;
                        break;
                    }
                }
                if(allHave) {
                    List<Double> values = new ArrayList<>();
                    for(Map<Date, DataPoint> innerMap: maps) {
                        values.add(innerMap.get(d).value);
                    }
                    ret.put(d, values);
                }
            }
        }
        return ret;
    }
}
