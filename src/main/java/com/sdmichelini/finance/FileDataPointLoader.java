package com.sdmichelini.finance;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileDataPointLoader implements IDataPointLoader {
    private final String name;
    public FileDataPointLoader(String filename) {
        name = filename;
    }

    @Override
    public List<DataPoint> loadDataPoints() {
        InputStream is = ClassLoader.getSystemResourceAsStream(name);
        Objects.requireNonNull(is);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return PriceCsvReader.readFromBufferedReader(reader);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
