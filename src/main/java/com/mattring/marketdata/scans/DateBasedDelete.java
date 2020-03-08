package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Matthew
 */
public class DateBasedDelete extends DbAware {

    long delete(int startDateInclusive) {

        final AtomicLong delCountTables = new AtomicLong();

        new AllTablesFn().listAll().stream().forEach(tbl -> {
            final String delSql
                    = "delete from " + tbl
                    + " where date >= ?";
            JDBC_TEMPLATE.update(delSql, startDateInclusive);
            delCountTables.incrementAndGet();
        });

        return delCountTables.get();
    }

    public static void main(String[] args) {
        long n = new DateBasedDelete().delete(20150101);
        System.out.println(n + " tables affected");
    }

}
