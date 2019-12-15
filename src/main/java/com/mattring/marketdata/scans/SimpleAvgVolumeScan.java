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
public class SimpleAvgVolumeScan extends DbAware {

    List<String> scan(int startDate, long avgVol) {

        List<String> candidateSyms = Collections.emptyList();

        try (Connection conn = db.open()) {

            candidateSyms = new AllTablesFn().listAll().stream().map(tbl -> {
                final String qry
                        = "select sym from " + tbl
                        + " where date >= :startDate group by sym having AVG(vol) > :avgVol";
                List<String> syms
                        = conn.createQuery(qry)
                        .addParameter("startDate", startDate)
                        .addParameter("avgVol", avgVol)
                        .executeAndFetch(String.class);
                return syms;
            }).flatMap(s -> s.stream()).collect(Collectors.toList());

        }

        return candidateSyms;
    }

    public static void main(String[] args) {
        long n = new SimpleAvgVolumeScan().scan(20170101, 1_000_000L)
                .stream()
                .peek(System.out::println)
                .count();
        System.out.println(n + " items");
    }
}
