/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mattring.marketdata.scans;

import com.mattring.marketdata.DbAware;
import com.mattring.marketdata.FullPointRowMapper;
import com.mattring.marketdata.Point;
import com.mattring.marketdata.PointsToDateSet;

import java.util.*;

/**
 * @author Matthew
 */
public class DateMisalignmentScan extends DbAware {

    public Map<String, Set<Integer>> scanDb(String sym1, String sym2, int startDate, int endDate) {

        final String tbl1 = tblFn.apply(sym1);
        final String tbl2 = tblFn.apply(sym2);

        final String qry
                = "select sym, exch, date, open, high, low, close, vol from %s "
                + "where sym=? "
                + "and date between ? and ? "
                + "order by date asc";

        List<Point> dates1 = JDBC_TEMPLATE.query(
                String.format(qry, tbl1),
                new Object[]{sym1, startDate, endDate},
                new FullPointRowMapper()
        );

        List<Point> dates2 = JDBC_TEMPLATE.query(
                String.format(qry, tbl2),
                new Object[]{sym2, startDate, endDate},
                new FullPointRowMapper()
        );

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
