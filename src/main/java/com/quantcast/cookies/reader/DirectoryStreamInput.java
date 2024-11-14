package com.quantcast.cookies.reader;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DirectoryStreamInput implements StreamReader{
    @Override
    public InputStream getInputStream(String inputStreamSpec) throws IOException {
        String directory = inputStreamSpec.endsWith("/") ?
                inputStreamSpec.substring(0, inputStreamSpec.length() - 1) : inputStreamSpec;
        List<String> files = getFilesInDirectory(directory);

        Vector<InputStream> streams = new Vector<>();
        for(String file: files) {
            streams.add(new FileInputStream(directory + "/" + file));
        }
        return new SequenceInputStream(streams.elements());
    }

    private List<String> getFilesInDirectory(String directory) throws IOException {
        List<String> fileList = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(directory))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileList.add(path.getFileName()
                            .toString());
                }
            }
        }
        return fileList;
    }

}
