package com.sdmichelini.finance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);
    private static final String OUTPUT_PREFIX = (System.getenv("ANALYZER_OUTPUT") == null) ? "output_" : System.getenv("ANALYZER_OUTPUT") + "\\output_";

    public static void main(String args[]) {
        String[] symbols = {"MSFT", "AAPL"};

        for(String symbol: symbols) {
            try {
                List<DataPoint> dataPoints = new AlphaVantageDataPointLoader(symbol, System.getenv("ALPHA_VANTAGE_API_KEY")).loadDataPoints();
                writeBollingerBands(OUTPUT_PREFIX + symbol.toLowerCase() + ".csv", dataPoints);
            } catch (MalformedURLException e) {
                logger.error(e);
            }
        }
    }

    public static void writeBollingerBands(String filename, List<DataPoint> dataPoints) {
        List<DataPoint> stdDev = DataPoint.getStandardDeviation(dataPoints);
        stdDev = stdDev.stream().map(val -> new DataPoint(val.date, 2.0 * val.value)).collect(Collectors.toList());
        List<DataPoint> mean = DataPoint.getMean(dataPoints);
        List<DataPoint> upperBand = DataPoint.addLists(stdDev, mean);
        List<DataPoint> lowerBand = DataPoint.subtractLists(mean, stdDev);

        CsvWriter writer = new CsvWriter(filename);
        writer.writeToFile(DataPoint.mergeDataPointCollection(dataPoints, upperBand, lowerBand));
    }
}
