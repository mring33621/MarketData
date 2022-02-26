package com.mattring.marketdata.scans;

import com.mattring.marketdata.AllTablesFn;
import com.mattring.marketdata.DbAware;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Matthew
 */
public class SameDayHighLowSpreadScan extends DbAware {

    public List<String> scan(int startDate, long avgVol, double avgHLRatio, double minAvgClose) {

        List<String> candidateSyms = Collections.emptyList();

        candidateSyms = new AllTablesFn().listAll().stream().map(tbl -> {
            final String qry
                    = "select sym from " + tbl
                    + " where date >= ? group by sym "
                    + "having AVG(vol) > ? "
                    + "and AVG(close) > ? "
                    + "and AVG(COALESCE(high / NULLIF(low,0), 0)) > ?";
            List<String> syms =
                    JDBC_TEMPLATE.query(
                            qry,
                            new Object[]{startDate, avgVol, minAvgClose, avgHLRatio},
                            (rs, i) -> rs.getString(1));
            return syms;
        }).flatMap(s -> s.stream()).collect(Collectors.toList());

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
