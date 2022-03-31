package com.company.task03.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class truckDataReader {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FILE_PATH = "resources/data/trucksData.txt";

    public List<String> readDataFromTxt(String fileName) {
        Path path;
        List<String> trucksData = null;

        if (fileName != null) {
            path = Paths.get(fileName);
            try (Stream<String> stringStream = Files.lines(path)) {
                trucksData = stringStream.toList();
            } catch (IOException e) {
                LOGGER.error("file does not read {} ", fileName, e);
//                e.printStackTrace();
            }
        }
        return trucksData;
    }
}
