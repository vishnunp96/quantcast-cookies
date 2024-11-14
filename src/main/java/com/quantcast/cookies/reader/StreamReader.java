package com.quantcast.cookies.reader;

import java.io.IOException;
import java.io.InputStream;

public interface StreamReader {
    public InputStream getInputStream(String inputStreamSpec) throws IOException;
}
