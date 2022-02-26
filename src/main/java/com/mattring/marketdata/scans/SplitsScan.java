package com.mattring.marketdata.scans;

import com.mattring.marketdata.Pair;
import com.mattring.marketdata.Point;
import com.mattring.marketdata.PointsFnFromDb;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Matthew
 */
public class SplitsScan {

    static final PointsFnFromDb pfn = new PointsFnFromDb();

    public Pair<Set<String>, List<String>> scan(List<String> symsToScan, int startDate) {

        final Set<String> splitSyms = new LinkedHashSet<>();
        final List<String> splitSymInfo = new ArrayList<>();

        symsToScan.stream().forEachOrdered(s -> {
            List<Point> points = pfn.getAllPointsForSym(s, startDate);
            IntStream.range(1, points.size()).forEachOrdered(i -> {
                Point p0 = points.get(i - 1);
                Point p1 = points.get(i);
                double closeOpenRatio = p0.getClose() / p1.getOpen();
                if (closeOpenRatio < 0.76d || closeOpenRatio > 1.49d) {
                    splitSyms.add(s);
                    splitSymInfo.add(s + ": " + p1.getDate() + ": " + closeOpenRatio);
                }
            });
        });

        final Pair<Set<String>, List<String>> pair = new Pair<>(splitSyms, splitSymInfo);
        return pair;
    }

    public static void main(String[] args) {
        SplitsScan sc = new SplitsScan();
        List<String> syms = new SameDayHighLowSpreadScan().scan(20150101, 1_000_000L, 1.03d, 7.00d);
        Pair<Set<String>, List<String>> pair = sc.scan(syms, 0);
        System.out.println("=============================================");
        long n = pair.a.stream().peek(System.out::println).count();
        System.out.println(n + " unique split syms");
        System.out.println("---------------------------------------------");
        n = pair.b.stream().peek(System.out::println).count();
        System.out.println(n + " split info items");
        System.out.println("=============================================");
    }
}
