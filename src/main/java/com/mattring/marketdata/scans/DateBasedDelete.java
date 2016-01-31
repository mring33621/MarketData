package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;
import java.util.concurrent.atomic.AtomicLong;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class DateBasedDelete extends DbAware {

    long delete(int startDateInclusive) {
        
        final AtomicLong delCountTables = new AtomicLong();

        try (Connection conn = db.open()) {

            new AllTablesFn().listAll().stream().forEach(tbl -> {
                final String qry
                        = "delete from " + tbl
                        + " where date >= :startDate";
                conn.createQuery(qry)
                        .addParameter("startDate", startDateInclusive)
                        .executeUpdate();
                delCountTables.incrementAndGet();
            });

        }

        return delCountTables.get();
    }

    public static void main(String[] args) {
        long n = new DateBasedDelete().delete(20150101);
        System.out.println(n + " tables affected");
    }

}
