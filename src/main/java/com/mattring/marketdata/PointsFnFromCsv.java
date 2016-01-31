
package com.mattring.marketdata;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author mring
 */
public class PointsFnFromCsv implements PointsFn {
    
    final File dataDir;

    public PointsFnFromCsv(String dataDir) {
        this.dataDir = new File(dataDir);
    }

    @Override
    public List<Point> getAllPointsForSym(String sym) {
        try {
            final String dataFilename = sym + ".csv";
            final File dataFile = new File(dataDir, dataFilename);
            final StringArrayToPointFn sapfn = new StringArrayToPointFn();
            final List<Point> points = 
                Files.readAllLines(dataFile.toPath())
                    .stream()
                    .skip(1)
                    .map(s -> s.split(","))
                    .map(sapfn::apply)
                    .collect(Collectors.toList());
            return points;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Point> getAllPointsForSym(String sym, int startDate) {
        return getAllPointsForSym(sym)
                .stream()
                .filter(p -> p.getDate() >= startDate)
                .collect(Collectors.toList());
    }
    
}
