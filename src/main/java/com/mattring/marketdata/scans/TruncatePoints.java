package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Matthew
 */
public class TruncatePoints extends DbAware {

    long truncate() {

        final AtomicLong delCountTables = new AtomicLong();

        new AllTablesFn().listAll().stream().forEach(tbl -> {
            final String truncSql
                    = "truncate table " + tbl;
            JDBC_TEMPLATE.update(truncSql);
            delCountTables.incrementAndGet();
        });

        return delCountTables.get();
    }

    public static void main(String[] args) {
        long n = new TruncatePoints().truncate();
        System.out.println(n + " tables affected");
    }

}
