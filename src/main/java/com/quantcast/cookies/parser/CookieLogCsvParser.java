package com.quantcast.cookies.parser;

import com.quantcast.cookies.model.CookieLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CookieLogCsvParser implements CookieLogParser {

    private final CsvParser csvParser;
    private final DateTimeFormatter dateTimeFormatter;

    public CookieLogCsvParser(CsvParser csvParser, DateTimeFormatter dateTimeFormatter) {
        this.csvParser = csvParser;
        this.dateTimeFormatter = dateTimeFormatter;
    }
    @Override
    public List<CookieLog> getCookieLogs(String csvData) throws IllegalArgumentException{
        List<Map<String, String>> csvValues = csvParser.getValues(csvData);
        List<CookieLog> cookies = new ArrayList<>();
        for (Map<String, String> row: csvValues) {
            if (!row.containsKey("cookie") || !row.containsKey("timestamp")) {
                throw new IllegalArgumentException("CSV data does not contain expected headers - cookie, timestamp");
            }
            String cookieId = row.get("cookie");
            LocalDateTime timestamp = LocalDateTime.parse(row.get("timestamp"), dateTimeFormatter);
            cookies.add(new CookieLog(cookieId, timestamp));
        }

        return cookies;
    }
}
