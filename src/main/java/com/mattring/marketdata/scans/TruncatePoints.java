package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;
import java.util.concurrent.atomic.AtomicLong;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class TruncatePoints extends DbAware {

    long truncate() {
        
        final AtomicLong delCountTables = new AtomicLong();

        try (Connection conn = db.open()) {

            new AllTablesFn().listAll().stream().forEach(tbl -> {
                final String qry
                        = "truncate table " + tbl;
                conn.createQuery(qry).executeUpdate();
                delCountTables.incrementAndGet();
            });

        }

        return delCountTables.get();
    }

    public static void main(String[] args) {
        long n = new TruncatePoints().truncate();
        System.out.println(n + " tables affected");
    }

}
