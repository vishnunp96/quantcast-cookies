package com.quantcast.cookies.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to read data from Csv files
 */
public class CsvParser {

    private final String delimiter = ",";

    /**
     * Parses a csv into a data structure
     * @param csvData - entire csvData, rows separated by \n, columns separated by commas
     * @return - list of maps, one map for each row, map keys taken from header
     * @throws IllegalArgumentException - when csv is empty or if rows cannot be mapped.
     */
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

    /**
     * returns the row of csv as a data structure, with the help of the header
     *
     * @param headers - csv headers
     * @param row - row of data
     * @return - map for row, keys taken from csv header
     * @throws IllegalArgumentException - when number of columns do not match between
     *  row and header.
     */
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
