package com.quantcast.cookies.processor;

import com.quantcast.cookies.counter.CookieCounter;
import com.quantcast.cookies.model.CookieLog;
import com.quantcast.cookies.parser.CookieLogParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Component
public class CookieProcessor {

    private static final Logger logger = LogManager.getLogger(CookieProcessor.class);

    @Autowired
    private final CookieLogParser parser;

    public CookieProcessor(CookieLogParser parser) {
        this.parser = parser;
    }


    public void process(String filePath, OutputStream outputStream, LocalDate counterDate) throws IOException, IllegalArgumentException {
        logger.info("Reading file at {}", filePath);
        String csvData = getFileContent(filePath);
        CookieCounter counter = new CookieCounter(counterDate);

        logger.info("Parsing data from file {}", filePath);
        List<CookieLog> cookieLogs = parser.getCookieLogs(csvData);
        for (CookieLog log: cookieLogs) {
            counter.process(log);
        }

        logger.info("Writing output");
        writeOutput(outputStream, counter.getMostActiveCookies());
    }

    private static String getFileContent(String filePath) throws IOException{
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static void writeOutput(OutputStream outputStream, List<String> output) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))){
            for (String outputLine: output) {
                writer.write(outputLine + "\n");
            }
        }
    }
}
