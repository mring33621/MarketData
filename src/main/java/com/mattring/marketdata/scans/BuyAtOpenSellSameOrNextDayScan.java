package com.mattring.marketdata.scans;

import com.mattring.marketdata.Point;
import com.mattring.marketdata.PointsFnFromDb;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.uncommons.maths.statistics.DataSet;

/**
 *
 * @author Matthew
 */
public class BuyAtOpenSellSameOrNextDayScan {

    static final PointsFnFromDb pfn = new PointsFnFromDb();

    public List<String> scan(List<String> symsToScan, int startDate, double minAvgGain) {

        final List<String> selected = new ArrayList<>();

        symsToScan.stream().forEachOrdered(s -> {
            final List<Point> points = pfn.getAllPointsForSym(s, startDate);
            final DataSet stats = new DataSet(points.size());
            IntStream.range(1, points.size()).forEachOrdered(i -> {
                final Point p0 = points.get(i - 1);
                final Point p1 = points.get(i);
                final double open = p0.getOpen();
                final double bestHigh = Math.max(p0.getHigh(), p1.getHigh());
                final double gain = bestHigh / open;
                stats.addValue(gain);
            });
            if (stats.getArithmeticMean() >= minAvgGain) {
                selected.add(s);
            }
        });

        return selected;
    }

    public static void main(String[] args) {
        final List<String> syms = new SameDayHighLowSpreadScan().scan(20150101, 1_000_000L, 1.013d, 7.00d);
        System.out.println(syms.size() + " volatile syms");
        final BuyAtOpenSellSameOrNextDayScan bos = new BuyAtOpenSellSameOrNextDayScan();
        final List<String> selected = bos.scan(syms, 20150101, 1.025d);
        final long n = selected.stream().peek(System.out::println).count();
        System.out.println(n + " selected syms");
    }
}
