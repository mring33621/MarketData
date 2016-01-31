
package com.mattring.marketdata.ddl;

import com.mattring.marketdata.DbAware;
import java.util.stream.IntStream;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class CreateTablesMain extends DbAware {
    
    public static void main(String[] args) {
        final String ddlTemplate = 
                "CREATE TABLE IF NOT EXISTS POINTS_%s ("
                + "SYM VARCHAR(7), "
                + "DATE INT, "
                + "OPEN DOUBLE, "
                + "HIGH DOUBLE, "
                + "LOW DOUBLE, "
                + "CLOSE DOUBLE, "
                + "VOL BIGINT);";
        try (Connection conn = DbAware.db.open()) {
            IntStream.range(65, 91).forEachOrdered(n -> {
                final String c = "" + ((char) n);
                final String ddl = String.format(ddlTemplate, c);
                System.out.println(ddl);
                conn.createQuery(ddl).executeUpdate();
            });
            conn.commit();
        }
    }
}
