package com.mattring.marketdata.misc;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeDate {

    public static RowMapper<ExchangeDate> asRowMapper() {
        return new RowMapper<ExchangeDate>() {
            @Override
            public ExchangeDate mapRow(ResultSet rs, int i) throws SQLException {
                ExchangeDate ed = new ExchangeDate();
                ed.exchange = rs.getString("exch");
                ed.date = rs.getLong("theDate");
                return ed;
            }
        };
    }

    public String exchange;
    public Long date;

    @Override
    public String toString() {
        return "ExchangeDate{" +
                "exchange='" + exchange + '\'' +
                ", date=" + date +
                '}';
    }
}
