package com.quantcast.cookies.parser;

import com.quantcast.cookies.model.CookieLog;

import java.util.List;


/**
 * From given data, should get back a list of cookie logs.
 */
public interface CookieLogParser {
    List<CookieLog> getCookieLogs(String data);
}
