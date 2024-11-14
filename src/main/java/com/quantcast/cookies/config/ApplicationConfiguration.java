package com.quantcast.cookies.config;

import com.quantcast.cookies.parser.CookieLogCsvParser;
import com.quantcast.cookies.parser.CookieLogParser;
import com.quantcast.cookies.parser.CsvParser;
import com.quantcast.cookies.reader.DirectoryStreamInput;
import com.quantcast.cookies.reader.FileStreamInput;
import com.quantcast.cookies.reader.StreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfiguration {


    /**
     * config - format for date time in the log file
     * @return - suitable formatter
     */
    @Bean
    DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    }

    /**
     * config - parser to use for input stream content
     * @param dateTimeFormatter - Autowired date time format
     * @return - parser for input csv file content
     */
    @Bean
    @Autowired
    CookieLogParser cookieLogParser(DateTimeFormatter dateTimeFormatter) {
        return new CookieLogCsvParser(new CsvParser(), dateTimeFormatter);
    }

    @Bean
    StreamReader directoryReader() {
        return new DirectoryStreamInput();
    }

    @Bean
    StreamReader fileReader() {
        return new FileStreamInput();
    }
}
