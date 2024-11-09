package com.quantcast.cookies.config;

import com.quantcast.cookies.parser.CookieLogCsvParser;
import com.quantcast.cookies.parser.CookieLogParser;
import com.quantcast.cookies.parser.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class ApplicationConfiguration {
    @Bean
    DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    }

    @Bean
    @Autowired
    CookieLogParser cookieLogParser(DateTimeFormatter dateTimeFormatter) {
        return new CookieLogCsvParser(new CsvParser(), dateTimeFormatter);
    }
}
