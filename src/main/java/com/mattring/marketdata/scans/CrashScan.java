package com.mattring.marketdata.scans;

import com.mattring.marketdata.Point;
import com.mattring.marketdata.PointsFnFromDb;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import org.uncommons.maths.statistics.DataSet;

/**
 *
 * @author Matthew
 */
public class CrashScan {

    static final PointsFnFromDb pfn = new PointsFnFromDb();

    public List<String> scan(List<String> symsToScan, int startDate, double crashThreshold) {

        final List<String> selected = new ArrayList<>();

        symsToScan.stream().forEachOrdered(s -> {
            final List<Point> points = pfn.getAllPointsForSym(s, startDate);
            IntStream.range(1, points.size()).forEachOrdered(i -> {
                final Point p0 = points.get(i - 1);
                final Point p1 = points.get(i);
                final double open = p0.getOpen();
                final double crashPoint = Math.min(p0.getClose(), p1.getOpen());
                final double loss = crashPoint / open;
                if (loss < crashThreshold) {
                    selected.add(p0.getSym() + " " + p0.getDate());
                }
            });
            
        });

        return selected;
    }

    public static void main(String[] args) {
        final CrashScan cs = new CrashScan();
        final List<String> selected = cs.scan(
                Arrays.asList("TQQQ", "FAS"), 
                20120101, 
                0.95d);
        final long n = selected.stream().peek(System.out::println).count();
    }
}
