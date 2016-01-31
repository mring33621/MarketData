package com.mattring.marketdata;

import com.mattring.marketdata.scans.MaintenanceScan;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class BulkLoader extends DbAware {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        final String insertTemplate
                = "INSERT INTO %s (SYM, DATE, OPEN, HIGH, LOW, CLOSE, VOL) "
                + "VALUES (:sym, :date, :open, :high, :low, :close, :vol)";

        final Path oldFilesDir = new File("C:\\Data\\old").toPath();
        final AtomicInteger fileCount = new AtomicInteger();

        Files.list(new File("C:\\Data\\marketdata\\csv").toPath())
                .filter(p -> p.getFileName()
                        .toString().endsWith(".csv"))
                .forEach((Path p) -> {
                    try (Connection con = db.open()) {
                        final String dateStr = p.getFileName().toString().split("_")[1].split(".csv")[0];
                        final int date = Integer.parseInt(dateStr);
                        System.out.println(date);
                        try (Stream<String> filteredLines = Files.lines(p).filter(s -> !s.startsWith("Symbol"))) {
                            filteredLines.forEach(l -> {
                                final String[] parts = l.split(",");
                                final Point point = new Point();
                                final String sym = parts[0];
                                point.setSym(sym);
                                point.setDate(date);
                                point.setOpen(Double.parseDouble(parts[2]));
                                point.setHigh(Double.parseDouble(parts[3]));
                                point.setLow(Double.parseDouble(parts[4]));
                                point.setClose(Double.parseDouble(parts[5]));
                                point.setVol(Long.parseLong(parts[6]));
                                final String table = tblFn.apply(sym);
                                final String insertSql = String.format(insertTemplate, table);
                                con.createQuery(insertSql).bind(point).executeUpdate();
                            });
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        con.commit(false);
                    }
                    try {
                        System.out.println(fileCount.addAndGet(1));
//                        final Path oldFile = oldFilesDir.resolve(p.getFileName());
//                        Files.move(p, oldFile, StandardCopyOption.REPLACE_EXISTING);
                        Files.delete(p);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
        
        MaintenanceScan.main(new String[0]);
    }

}
