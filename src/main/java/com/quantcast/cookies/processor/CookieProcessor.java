package com.quantcast.cookies.processor;

import com.quantcast.cookies.counter.CookieCounter;
import com.quantcast.cookies.model.CookieLog;
import com.quantcast.cookies.parser.CookieLogParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
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

    /**
     * - Reads from input stream
     * - Parses data and processes using counter based on input date
     * - Writes to output stream
     *
     * @param inputStream - to read from
     * @param outputStream - to write to
     * @param counterDate - date for which logs have to be counted
     * @throws IOException - when input stream has an issue
     * @throws IllegalArgumentException - when parsing runs into an issue
     */
    public void process(InputStream inputStream, OutputStream outputStream, LocalDate counterDate)
            throws IOException, IllegalArgumentException {
        logger.info("Reading from input stream");
        String csvData = getStreamContent(inputStream);
        CookieCounter counter = new CookieCounter(counterDate);

        logger.info("Parsing data from stream");
        List<CookieLog> cookieLogs = parser.getCookieLogs(csvData);
        for (CookieLog log: cookieLogs) {
            counter.process(log);
        }

        logger.info("Writing output");
        writeOutput(outputStream, counter.getMostActiveCookies());
    }

    /**
     * Handles getting full content from stream
     * @param inputStream - stream to read from
     * @return - stream content, lines separated by \n
     * @throws IOException - when input stream causes an issue
     */
    private static String getStreamContent(InputStream inputStream)
            throws IOException{
        StringBuilder streamContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                streamContent.append(line).append("\n");
            }
        }
        return streamContent.toString();
    }

    /**
     * Handles writing to output stream in required format.
     * @param outputStream - stream to write output
     * @param output - list of strings to write to output
     * @throws IOException - when output stream causes an issue
     */
    private static void writeOutput(OutputStream outputStream, List<String> output)
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream))){
            for (String outputLine: output) {
                writer.write(outputLine + "\n");
            }
        }
    }
}
