package com.sdmichelini.finance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CsvWriter {
    private final String filepath;
    public CsvWriter(String filepath) {
        this.filepath = filepath;
    }

    public void writeToFile(Map<Date, List<Double>> input) {
        Logger logger = LogManager.getLogger(getClass().getName() + ":writeToFile");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for(Map.Entry<Date, List<Double>> entries: input.entrySet()) {

                writer.write(PriceDataPoint.DATE_FORMAT.format(entries.getKey()) + ", ");
                for(Double val : entries.getValue()) {
                    writer.write(val + ", ");
                }
                writer.write("\n");
            }
            logger.info("Wrote to file: " + filepath);
        } catch(IOException e) {
            logger.error(e);
        }
    }
}
