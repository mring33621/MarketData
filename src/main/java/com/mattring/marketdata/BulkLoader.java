package com.mattring.marketdata;

import com.google.common.base.Strings;
import com.mattring.marketdata.scans.MaintenanceScan;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Matthew
 */
public class BulkLoader extends DbAware {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        final String insertTemplate
                = "INSERT INTO %s (EXCH, SYM, DATE, OPEN, HIGH, LOW, CLOSE, VOL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        final Path oldFilesDir = new File("C:\\Data\\marketdata\\archive").toPath();
        final AtomicInteger fileCount = new AtomicInteger();

        Files.list(new File("C:\\Data\\marketdata\\csv").toPath())
                .filter(p -> p.getFileName()
                        .toString().endsWith(".csv"))
                .forEach((Path p) -> {
                    final String dateStr = p.getFileName().toString().split("_")[1].split(".csv")[0];
                    final int date = Integer.parseInt(dateStr);
                    final String[] exch = new String[1];
                    exch[0] = p.getFileName().toString().split("_")[0];
                    if (Strings.isNullOrEmpty(exch[0])) {
                        exch[0] = "UNK";
                    }
                    System.out.println(date + " " + exch[0]);
                    try (Stream<String> filteredLines = Files.lines(p).filter(s -> !s.startsWith("Symbol"))) {
                        filteredLines.forEach(l -> {
                            final String[] parts = l.split(",");
                            final Point point = new Point();
                            final String sym = parts[0];
                            point.setExch(exch[0]);
                            point.setSym(sym);
                            point.setDate(date);
                            point.setOpen(Double.parseDouble(parts[2]));
                            point.setHigh(Double.parseDouble(parts[3]));
                            point.setLow(Double.parseDouble(parts[4]));
                            point.setClose(Double.parseDouble(parts[5]));
                            point.setVol(Long.parseLong(parts[6]));
                            final String table = tblFn.apply(sym);
                            final String insertSql = String.format(insertTemplate, table);
                            JDBC_TEMPLATE.update(
                                    insertSql,
                                    point.getExch(),
                                    point.getSym(),
                                    point.getDate(),
                                    point.getOpen(),
                                    point.getHigh(),
                                    point.getLow(),
                                    point.getClose(),
                                    point.getVol()
                            );
                        });
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
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
