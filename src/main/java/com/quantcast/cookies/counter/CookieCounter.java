package com.quantcast.cookies.counter;

import com.quantcast.cookies.model.CookieLog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookieCounter {

    private final LocalDate logDate;
    protected final Map<String, Integer> counts;

    public CookieCounter(LocalDate logDate) {
        this.logDate = logDate;
        this.counts = new HashMap<>();
    }

    public void process(CookieLog log) {
        if (logDate.isEqual(log.dateTime().toLocalDate())) {
            counts.put(log.id(), counts.getOrDefault(log.id(), 0) + 1);
        }
    }

    public List<String> getMostActiveCookies() {
        List<String> activeCookies = new ArrayList<>();
        int maxActivity = getMaximumActivity();

        for (Map.Entry<String, Integer> entry: counts.entrySet()) {
            if (entry.getValue() == maxActivity) {
                activeCookies.add(entry.getKey());
            }
        }

        return activeCookies;
    }

    private int getMaximumActivity() {
        int maxActivity = 0;
        for (Map.Entry<String, Integer> entry: counts.entrySet()) {
            if (entry.getValue() > maxActivity) {
                maxActivity = entry.getValue();
            }
        }
        return maxActivity;
    }
}
