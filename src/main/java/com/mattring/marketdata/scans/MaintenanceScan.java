
package com.mattring.marketdata.scans;
import com.mattring.marketdata.DbAware;
import java.util.List;
import java.util.stream.IntStream;
import org.sql2o.Connection;

/**
 *
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
        
        try (Connection conn = DbAware.db.open()) {
            
            final List<Integer> holidays = 
                conn.createQuery(qryForHolidays).executeAndFetch(Integer.class);
            
            if (!holidays.isEmpty()) {
                final String holidayIn = holidays.toString().replaceFirst("\\[", "").replaceFirst("\\]", "");
                System.out.println(holidayIn);

                IntStream.range(65, 91).forEachOrdered(n -> {
                    final String c = "" + ((char) n);
                    final String del = String.format(deleteHolidaysTemplate, c, holidayIn);
                    System.out.println(del);
                    conn.createQuery(del).executeUpdate();
                });
                conn.commit();
                
            }
        }
        
        try (Connection conn = DbAware.db.open()) {
            
            final List<Integer> dupDates = 
                conn.createQuery(qryForDupDates).executeAndFetch(Integer.class);
            
            if (!dupDates.isEmpty()) {
                
                System.err.println("DUPLICATE DATES:");
                dupDates.forEach(System.err::println);
                
            }
        }
    }
}
