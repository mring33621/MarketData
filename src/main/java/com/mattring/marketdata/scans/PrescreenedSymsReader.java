package com.mattring.marketdata.scans;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Matthew
 */
public class PrescreenedSymsReader {

    public List<String> readSyms(String filename) throws IOException {
        return Files.lines(
                Paths.get("c:/Data", filename))
                .collect(Collectors.toList());
    }
}
