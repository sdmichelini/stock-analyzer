package com.sdmichelini.finance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AlphaVantageDataPointLoader implements IDataPointLoader {
    private final URL url;

    public AlphaVantageDataPointLoader(String symbol, String apiKey) throws MalformedURLException {
        this.url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" +symbol+ "&apikey=" + apiKey + "&datatype=csv");
    }

    @Override
    public List<DataPoint> loadDataPoints() {
        Logger logger = LogManager.getLogger(this.getClass().getName() + ":loadDataPoints");
        logger.info("Loading data from: " + url);

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader((url.openConnection()).getInputStream()))
        ) {
            return PriceCsvReader.readFromBufferedReader(reader);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }
}
