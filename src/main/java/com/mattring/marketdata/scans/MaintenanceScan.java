
package com.mattring.marketdata.scans;

import com.mattring.marketdata.DbAware;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Matthew
 */
public class MaintenanceScan extends DbAware {

    public static void main(String[] args) {

        final String qryForHolidays =
                "select distinct(date) from POINTS_Q where sym='QQQ' and vol <= 0 order by date asc";
        final String deleteHolidaysTemplate =
                "delete from POINTS_%s where date in (%s)";
        final String qryForDupDates =
                "select date from POINTS_Q where sym='QQQ' group by date having count(date) > 1";

        final List<Integer> holidays = JDBC_TEMPLATE.query(qryForHolidays, (rs, i) -> rs.getInt(1));
        // TODO: scan all exchanges for holidays
        if (!holidays.isEmpty()) {
            final String holidayIn = holidays.toString().replaceFirst("\\[", "").replaceFirst("\\]", ""); // HACK: munging the toString() into SQL in clause format
            System.out.println(holidayIn);
            IntStream.range(65, 91).forEachOrdered(n -> {
                final String c = "" + ((char) n);
                final String del = String.format(deleteHolidaysTemplate, c, holidayIn);
                System.out.println(del);
                JDBC_TEMPLATE.execute(del);
            });
        }

        final List<Integer> dupDates = JDBC_TEMPLATE.query(qryForDupDates, (rs, i) -> rs.getInt(1));
        // TODO: scan all exchanges for duplicate dates
        if (!dupDates.isEmpty()) {
            System.err.println("DUPLICATE DATES:");
            dupDates.forEach(System.err::println);
            final String dupDatesIn = dupDates.toString().replaceFirst("\\[", "").replaceFirst("\\]", ""); // HACK: munging the toString() into SQL in clause format
            System.out.println(dupDatesIn);
            IntStream.range(65, 91).forEachOrdered(n -> {
                final String c = "" + ((char) n);
                final String del = String.format(deleteHolidaysTemplate, c, dupDatesIn);
                System.out.println(del);
                JDBC_TEMPLATE.execute(del);
            });
        }

        // TODO: scan all exchanges for missing dates
    }
}
