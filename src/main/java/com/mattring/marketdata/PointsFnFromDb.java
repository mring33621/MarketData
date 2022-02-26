package com.mattring.marketdata;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
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
                = "select sym, exch, date, open, high, low, close, vol from "
                + tbl + " where sym=? and date >=? order by date asc";
        final List<Point> points = JDBC_TEMPLATE.query(qry, new Object[]{sym, startDate}, new FullPointRowMapper());
        return points;
    }

    @Override
    public List<Point> getAllPointsForSym(String sym, int numRows, int offset) {
        final String tbl = tblFn.apply(sym);
        final String qry
                = "select sym, exch, date, open, high, low, close, vol from "
                + tbl + " order by date asc"
                + " limit " + numRows + " offset " + offset;
        final List<Point> points = JDBC_TEMPLATE.query(qry, new FullPointRowMapper());
        return points;
    }

    public static void main(String[] args) {
        System.out.println(new PointsFnFromDb().getAllPointsForSym("TQQQ", 0).size());
        System.out.println(new PointsFnFromDb().getAllPointsForSym("SQQQ", 0).size());
        System.out.println(new PointsFnFromDb().getAllPointsForSym("FAS", 0).size());
    }
}
