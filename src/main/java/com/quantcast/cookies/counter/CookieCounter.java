package com.quantcast.cookies.counter;

import com.quantcast.cookies.model.CookieLog;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Counter keeps track of cookie frequency for a specific day
 */
public class CookieCounter {

    private final LocalDate counterDate;
    protected final Map<String, Integer> counts;

    public CookieCounter(LocalDate counterDate) {
        this.counterDate = counterDate;
        this.counts = new HashMap<>();
    }

    /**
     * increments count associated with cookie if log matches counter date
     * @param log - CookieLog object
     */
    public void process(CookieLog log) {
        if (counterDate.isEqual(log.dateTime().toLocalDate())) {
            counts.put(log.id(), counts.getOrDefault(log.id(), 0) + 1);
        }
    }

    /**
     * looks for most active cookies processed so far
     * @return - list of string of cookie ids
     */
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

    /**
     * searches through cookies processed to find maximum activity
     * @return - maximum activity value
     */
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
