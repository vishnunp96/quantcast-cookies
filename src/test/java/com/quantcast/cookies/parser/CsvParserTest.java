package com.quantcast.cookies.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class CsvParserTest {

    CsvParser csvParser = new CsvParser();
    @Test
    public void whenGoodCsvIsPassedListOfMapsIsReturned() {
        String csvData = "ids,timestamp\nid1,2012\nid2,2024";

        List<Map<String, String>> result = csvParser.getValues(csvData);

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.getFirst().containsKey("ids"));
        Assertions.assertTrue(result.getFirst().containsKey("timestamp"));
        Assertions.assertEquals("id2", result.get(1).get("ids"));
        Assertions.assertEquals("2024", result.get(1).get("timestamp"));
    }

    @Test
    public void whenBadCsvIsPassedErrorIsThrown() {
        String csvData = "cookie,timestamp\nid1,2012,id2";
        try {
            csvParser.getValues(csvData);
            Assertions.fail("The number of columns in second row do not match the header, should fail.");
        } catch (IllegalArgumentException ignored) {
        }
    }
}
