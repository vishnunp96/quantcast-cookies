package com.quantcast.cookies.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvParser {

    private final String delimiter = ",";

    public List<Map<String, String>> getValues(String csvData) throws IllegalArgumentException {
        String[] csvLines = csvData.split("\n");
        if (csvLines.length < 1) {
            throw new IllegalArgumentException("Empty csv data");
        }

        String[] headers = csvLines[0].split(delimiter);

        List<Map<String, String>> csvValues = new ArrayList<>();
        for (int i = 1; i < csvLines.length; i++) {
            csvValues.add(getMappedValuesFromRow(headers, csvLines[i]));
        }

        return csvValues;
    }

    private Map<String, String> getMappedValuesFromRow(String[] headers, String row)
            throws IllegalArgumentException {
        String[] rowValues = row.split(delimiter);
        if (rowValues.length != headers.length){
            throw new IllegalArgumentException("CSV row size does not match header size");
        }

        Map<String, String> mappedValues = new HashMap<>();
        for (int col = 0; col < rowValues.length; col++) {
            mappedValues.put(headers[col], rowValues[col]);
        }
        return mappedValues;
    }
}
