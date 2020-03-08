package com.mattring.marketdata;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FullPointRowMapper implements RowMapper<Point> {

    @Override
    public Point mapRow(ResultSet rs, int i) throws SQLException {
        // Assumed columns & order:
        //     select sym, exch, date, open, high, low, close, vol from...
        // TODO: maybe switch to using column names?
        Point p = new Point();
        p.setSym(rs.getString(1));
        p.setExch(rs.getString(2));
        p.setDate(rs.getInt(3));
        p.setOpen(rs.getDouble(4));
        p.setHigh(rs.getDouble(5));
        p.setLow(rs.getDouble(6));
        p.setClose(rs.getDouble(7));
        p.setVol(rs.getLong(8));
        return p;
    }

}
