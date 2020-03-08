package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Matthew
 */
public class SimpleAvgVolumeScan extends DbAware {

    List<String> scan(int startDate, long avgVol) {

        List<String> candidateSyms = Collections.emptyList();

        candidateSyms = new AllTablesFn().listAll().stream().map(tbl -> {
            final String qry
                    = "select sym from " + tbl
                    + " where date >= ? group by sym having AVG(vol) > ?";
            List<String> syms =
                    JDBC_TEMPLATE.query(
                            qry,
                            new Object[]{startDate, avgVol},
                            (rs, i) -> rs.getString(1));
            return syms;
        }).flatMap(s -> s.stream()).collect(Collectors.toList());

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
