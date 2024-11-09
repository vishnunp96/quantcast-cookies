package com.quantcast.cookies.parser;

import com.quantcast.cookies.model.CookieLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CookieLogCsvParserTest {

    CookieLogCsvParser cookieLogCsvParser = new CookieLogCsvParser(new CsvParser(),
            DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    @Test
    public void whenGoodCsvIsPassedThenListOfCookiesIsReturned() {
        String csvData = "ids,cookie,timestamp\n" +
                "id1,cookie1,2018-12-09T02:45:01+00:00\n" +
                "id2,cookie1,2019-12-09T02:45:01+00:00\n";
        LocalDateTime expectedDateTime = LocalDateTime.parse("2018-12-09T02:45:01");

        List<CookieLog> logs = cookieLogCsvParser.getCookieLogs(csvData);

        Assertions.assertEquals(2, logs.size());
        Assertions.assertEquals("cookie1", logs.getFirst().id());
        Assertions.assertEquals(expectedDateTime, logs.getFirst().dateTime());
        Assertions.assertEquals(expectedDateTime.plusYears(1), logs.get(1).dateTime());
    }

    @Test
    public void whenBadCsvIsPassedThenErrorIsReturned() {
        String csvData = "ids,cookie,ts\n" +
                "id1,cookie1,2018-12-09T02:45:01+00:00\n" +
                "id2,cookie1,2019-12-09T02:45:01+00:00\n";
        LocalDateTime expectedDateTime = LocalDateTime.parse("2018-12-09T02:45:01");

        try {
            cookieLogCsvParser.getCookieLogs(csvData);
            Assertions.fail("Should have failed due to lack of timestamp");
        } catch (IllegalArgumentException ignored){
        }
    }
}
