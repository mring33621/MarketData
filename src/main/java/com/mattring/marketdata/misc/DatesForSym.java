package com.mattring.marketdata.misc;

import com.mattring.marketdata.PointsFn;
import com.mattring.marketdata.PointsFnFromDb;
import com.mattring.marketdata.PointsToDateSet;

import java.util.*;

public class DatesForSym {

    public static List<Integer> desc(String sym, PointsFn pointsFn) {
        List<Integer> dates = new ArrayList<>(new PointsToDateSet().convert(pointsFn.getAllPointsForSym(sym)));
        Collections.sort(dates, Comparator.reverseOrder());
        return dates;
    }

    public static void main(String[] args) {
        final PointsFn pointsFn = new PointsFnFromDb();
        Arrays.stream(args).forEach(sym -> System.out.println(desc(sym, pointsFn)));
    }
}
