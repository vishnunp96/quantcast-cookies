package com.quantcast.cookies.counter;

import com.quantcast.cookies.model.CookieLog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CookieCounterTest {

    CookieCounter cookieCounter;
    final LocalDate counterDate = LocalDate.parse("2018-12-09");
    @BeforeEach
    public void setup() {
        cookieCounter = new CookieCounter(counterDate);
    }

    @Test
    public void whenValidCookieIsProcessedItsRecorded() {
        String cookieId1 = "id1";
        LocalDateTime dateTime = counterDate.atStartOfDay();

        CookieLog cookieLog1 = new CookieLog(cookieId1, dateTime);

        cookieCounter.process(cookieLog1);
        cookieCounter.process(cookieLog1);

        Assertions.assertEquals(1, cookieCounter.counts.size());
        Assertions.assertEquals(2, cookieCounter.counts.get(cookieId1));
    }

    @Test
    public void whenInvalidCookieIsProcessedItsNotRecorded() {
        String cookieId1 = "id1";
        LocalDateTime dateTime = counterDate.atStartOfDay().minusDays(2);

        CookieLog cookieLog1 = new CookieLog(cookieId1, dateTime);

        cookieCounter.process(cookieLog1);

        Assertions.assertTrue(cookieCounter.counts.isEmpty());

    }

    @Test
    public void whenMultipleCookiesAreTiedReturnsAllOfThem() {
        String cookieId1 = "id1";
        String cookieId2 = "id2";
        LocalDateTime dateTime = counterDate.atStartOfDay();

        CookieLog cookieLog1 = new CookieLog(cookieId1, dateTime);
        CookieLog cookieLog2 = new CookieLog(cookieId2, dateTime);
        CookieLog cookieLog3 = new CookieLog(cookieId1 + "3", dateTime);

        cookieCounter.process(cookieLog1);
        cookieCounter.process(cookieLog2);
        cookieCounter.process(cookieLog1);
        cookieCounter.process(cookieLog3);
        cookieCounter.process(cookieLog2);

        List<String> mostActiveCookies = cookieCounter.getMostActiveCookies();
        Assertions.assertEquals(2, mostActiveCookies.size());
        Assertions.assertTrue(mostActiveCookies.contains(cookieId1));
        Assertions.assertTrue(mostActiveCookies.contains(cookieId2));
    }
}
