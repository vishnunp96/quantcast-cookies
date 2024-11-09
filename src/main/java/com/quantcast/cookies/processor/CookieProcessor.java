package com.quantcast.cookies.processor;

import com.quantcast.cookies.CookiesAppRunner;
import com.quantcast.cookies.counter.CookieCounter;
import com.quantcast.cookies.model.CookieLog;
import com.quantcast.cookies.parser.CookieLogCsvParser;
import com.quantcast.cookies.parser.CookieLogParser;
import com.quantcast.cookies.parser.CsvParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CookieProcessor {

    private static final Logger logger = LogManager.getLogger(CookieProcessor.class);

    private final CookieLogParser parser = new CookieLogCsvParser(new CsvParser(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    public void process(String filePath, LocalDate counterDate) throws IOException, IllegalArgumentException {
        logger.info("Reading file at {}", filePath);
        String csvData = getFileContent(filePath);
        CookieCounter counter = new CookieCounter(counterDate);

        logger.info("Parsing data from file {}", filePath);
        List<CookieLog> cookieLogs = parser.getCookieLogs(csvData);
        for (CookieLog log: cookieLogs) {
            counter.process(log);
        }

        logger.info("Writing output");
        writeOutput(counter.getMostActiveCookies());
    }

    private static String getFileContent(String filePath) throws IOException{
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static void writeOutput(List<String> output) {
        for (String outputLine: output) {
            System.out.println(outputLine);
        }
    }
}
