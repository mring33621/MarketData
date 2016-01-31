
package com.mattring.marketdata.ddl;

import com.mattring.marketdata.DbAware;
import java.util.stream.IntStream;
import org.sql2o.Connection;

/**
 *
 * @author Matthew
 */
public class DropTablesMain  extends DbAware {
    
    public static void main(String[] args) {
        final String ddlTemplate = 
                "DROP TABLE POINTS_%s;";
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
