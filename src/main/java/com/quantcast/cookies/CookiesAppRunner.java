package com.quantcast.cookies;

import com.quantcast.cookies.processor.CookieProcessor;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Entrypoint to the application
 * - takes in commandline args
 *      - file(-f) - log file to be ingested
 *      - date(-d) - date for which logs have to be counted
 *          - (yyyy-MM-dd) format -> 2018-12-09
 * - Output
 *      - StdOUT - Most active cookies for the specific date
 */

@Component
public class CookiesAppRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(CookiesAppRunner.class);

    @Autowired
    private final CookieProcessor processor;

    public CookiesAppRunner(CookieProcessor processor) {
        this.processor = processor;
    }


    /**
     * opens input file stream and calls processor with streams and date.
     *
     * @param args - command line arguments
     * @throws IOException - when file input has an issue
     * @throws IllegalArgumentException - when input data formatting
     * @throws RuntimeException - when commandLine arguments have an issue
     */
    @Override
    public void run(ApplicationArguments args) throws RuntimeException, IOException, IllegalArgumentException{
        CommandLine cmdArgs = getArguments(args.getSourceArgs());
        LocalDate countDate = LocalDate.parse(cmdArgs.getOptionValue("d"));
        String filePath = cmdArgs.getOptionValue("f");
        logger.info("Running application for date {} on log file {}", countDate, filePath);

        try(InputStream inputStream = new FileInputStream(filePath)) {
            processor.process(inputStream, System.out, countDate);
        } catch (IOException e) {
            logger.error("A problem occurred with file input or output.", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid data to be parsed", e);
            throw e;
        }
    }

    /**
     * Configures the command line parser and returns it.
     *
     * @param args - command line arguments
     * @return CommandLine object containing "f" and "d" arguments specified
     *  - "f" - file - log file to be parsed
     *  - "d" - date - date for which logs have to be counted
     * @throws RuntimeException if required arguments are not present, or in wrong format.
     */
    private CommandLine getArguments(String[] args) throws RuntimeException{
        Options options = new Options();

        Option date = new Option("d", "date", true, "date to check logs." +
                " Format-> yyyy-mm-dd");
        date.setRequired(true);
        options.addOption(date);

        Option file = new Option("f", "file", true, "log file path");
        file.setRequired(true);
        options.addOption(file);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            LocalDate logDate = LocalDate.parse(cmd.getOptionValue("d"));
        } catch (ParseException | DateTimeParseException e) {
            logger.error("Command line arguments encountered a problem.", e);
            helpFormatter.printHelp("cookie-log-app", options);
            throw new RuntimeException(e);
        }

        return cmd;
    }
}
