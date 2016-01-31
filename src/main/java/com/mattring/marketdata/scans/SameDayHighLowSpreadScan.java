package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class SameDayHighLowSpreadScan extends DbAware {

    List<String> scan(int startDate, long avgVol, double avgHLRatio, double minAvgClose) {

        List<String> candidateSyms = Collections.emptyList();

        try (Connection conn = db.open()) {

            candidateSyms = new AllTablesFn().listAll().stream().map(tbl -> {
                final String qry
                        = "select sym from " + tbl
                        + " where date >= :startDate group by sym "
                        + "having AVG(vol) > :avgVol "
                        + "and AVG(close) > :minAvgClose "
                        + "and AVG(COALESCE(high / NULLIF(low,0), 0)) > :avgHLRatio";
                List<String> syms
                        = conn.createQuery(qry)
                        .addParameter("startDate", startDate)
                        .addParameter("avgVol", avgVol)
                        .addParameter("minAvgClose", minAvgClose)
                        .addParameter("avgHLRatio", avgHLRatio)
                        .executeAndFetch(String.class);
                return syms;
            }).flatMap(s -> s.stream()).collect(Collectors.toList());

        }

        return candidateSyms;
    }

    public static void main(String[] args) {
        long n = new SameDayHighLowSpreadScan().scan(20151001, 1_000_000L, 1.03d, 7.00d)
                .stream()
                .peek(System.out::println)
                .count();
        System.out.println(n + " items");
    }
}
