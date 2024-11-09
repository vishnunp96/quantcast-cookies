package com.quantcast.cookies;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class CookiesAppRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(CookiesAppRunner.class);
    @Override
    public void run(ApplicationArguments args) throws Exception {
        CommandLine cmdArgs = getArguments(args.getSourceArgs());
        LocalDate logDate = LocalDate.parse(cmdArgs.getOptionValue("d"));
        String filePath = cmdArgs.getOptionValue("f");
        logger.info("Running application for date {} on log file {}", logDate, filePath);
    }

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
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
            LocalDate logDate = LocalDate.parse(cmd.getOptionValue("d"));
        } catch (ParseException | DateTimeParseException e) {
            logger.error(e);
            helpFormatter.printHelp("cookie-log-app", options);
            throw new RuntimeException(e);
        }

        return cmd;
    }
}
