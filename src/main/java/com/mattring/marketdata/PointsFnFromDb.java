package com.mattring.marketdata;

import java.util.Collections;
import java.util.List;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class PointsFnFromDb extends DbAware implements PointsFn {

    @Override
    public List<Point> getAllPointsForSym(String sym) {
        return getAllPointsForSym(sym, 0);
    }

    @Override
    public List<Point> getAllPointsForSym(String sym, int startDate) {
        final String tbl = tblFn.apply(sym);
        final String qry
                = "select sym, date, open, high, low, close, vol from "
                + tbl + " where sym=:sym and date >= :startDate order by date asc";
        List<Point> points = Collections.emptyList();
        try (Connection conn = db.open()) {
            points
                    = conn
                    .createQuery(qry)
                    .addParameter("sym", sym)
                    .addParameter("startDate", startDate)
                    .executeAndFetch(Point.class);
        }
        return points;
    }

    public static void main(String[] args) {
        System.out.println(new PointsFnFromDb().getAllPointsForSym("TQQQ", 0).size());
        System.out.println(new PointsFnFromDb().getAllPointsForSym("SQQQ", 0).size());
        System.out.println(new PointsFnFromDb().getAllPointsForSym("FAS", 0).size());
    }
}
