package com.quantcast.cookies;

import com.quantcast.cookies.model.CookieLog;
import com.quantcast.cookies.parser.CookieLogParser;
import com.quantcast.cookies.processor.CookieProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CookieProcessorTest {

    @Mock
    CookieLogParser cookieLogParser;

    @InjectMocks
    CookieProcessor cookieProcessor;

    ByteArrayOutputStream outputStream;

    String filePath = "src/test/resources/cookie_log_test.csv";
    LocalDate testDate = LocalDate.parse("2018-12-09");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        outputStream = new ByteArrayOutputStream();
    }

    List<CookieLog> sampleCookieLogs() {
        List<CookieLog> sample = new ArrayList<>();
        sample.add(new CookieLog("id1", testDate.atStartOfDay()));
        sample.add(new CookieLog("id2", testDate.atStartOfDay().plusHours(2)));
        sample.add(new CookieLog("id1", testDate.atStartOfDay().plusHours(3)));
        sample.add(new CookieLog("id3", testDate.atStartOfDay().plusHours(4)));
        return sample;
    }

    @Test
    public void whenGoodCsvIsPassedThenCorrectOutputIsReceived() throws IOException {
        when(cookieLogParser.getCookieLogs(anyString())).thenReturn(sampleCookieLogs());

        cookieProcessor.process(filePath, outputStream, testDate);

        Assertions.assertEquals("id1\n", outputStream.toString());
    }

    @Test
    public void whenParsingCsvIsAProblemIllegalArgumentErrorIsReceived() throws IOException {
        when(cookieLogParser.getCookieLogs(anyString())).thenThrow(new IllegalArgumentException());

        try {
            cookieProcessor.process(filePath, outputStream, testDate);
            Assertions.fail("The error thrown by cookieLogParser should bubble up");
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    public void whenFileInputFacesAProblemIOExceptionIsThrown() {
        try {
            cookieProcessor.process("foo"+filePath, outputStream, testDate);
            Assertions.fail("The error thrown by file process should bubble up");
        } catch (IOException ignored) {
        }
    }
}
