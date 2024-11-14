package com.quantcast.cookies.reader;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class FileStreamInput implements StreamReader{

    @Override
    public InputStream getInputStream(String inputStreamSpec) throws IOException{
        return new FileInputStream(inputStreamSpec);
    }
}
