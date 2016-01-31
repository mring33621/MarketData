/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mattring.marketdata.scans;

import com.mattring.marketdata.DbAware;
import com.mattring.marketdata.Point;
import com.mattring.marketdata.PointsToDateSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class DateMisalignmentScan extends DbAware {

    public Map<String, Set<Integer>> scanDb(String sym1, String sym2, int startDate, int endDate) {

        final String tbl1 = tblFn.apply(sym1);
        final String tbl2 = tblFn.apply(sym2);

        final String qry
                = "select a.* from %s a "
                + "where a.sym=:sym "
                + "and a.date between :startDate and :endDate "
                + "order by a.date asc";

        List<Point> dates1 = null;
        List<Point> dates2 = null;

        try (Connection conn = db.open()) {

            dates1
                    = conn.createQuery(String.format(qry, tbl1))
                    .addParameter("sym", sym1)
                    .addParameter("startDate", startDate)
                    .addParameter("endDate", endDate)
                    .executeAndFetch(Point.class);
            dates2
                    = conn.createQuery(String.format(qry, tbl2))
                    .addParameter("sym", sym2)
                    .addParameter("startDate", startDate)
                    .addParameter("endDate", endDate)
                    .executeAndFetch(Point.class);

        }

        return checkDateAlignment(dates1, dates2, sym1, sym2);
    }

    public Map<String, Set<Integer>> checkDateAlignment(List<Point> dates1, List<Point> dates2, String sym1, String sym2) {
        final PointsToDateSet pointsToDateSet = new PointsToDateSet();
        final Set<Integer> dateSet1a = pointsToDateSet.convert(dates1);
        final Set<Integer> dateSet1b = new LinkedHashSet<>(dateSet1a);
        final Set<Integer> dateSet2a = pointsToDateSet.convert(dates2);
        final Set<Integer> dateSet2b = new LinkedHashSet<>(dateSet2a);

        dateSet1a.removeAll(dateSet2b);
        dateSet2a.removeAll(dateSet1b);

        final Map<String, Set<Integer>> retVal = new LinkedHashMap<>();
        retVal.put(sym1, dateSet1a);
        retVal.put(sym2, dateSet2a);
        return retVal;
    }

    public static void main(String[] args) {
        System.out.println(new DateMisalignmentScan().scanDb("YNDX", "GOOG", 20120101, 20150911));
    }

}
