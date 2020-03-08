
package com.mattring.marketdata.ddl;

import com.mattring.marketdata.DbAware;

import java.util.stream.IntStream;

/**
 * @author Matthew
 */
public class DropTablesMain extends DbAware {

    public static void main(String[] args) {

        final String ddlTemplate =
                "DROP TABLE POINTS_%s;";

        IntStream.range(65, 91).forEachOrdered(n -> {
            final String c = "" + ((char) n);
            final String ddl = String.format(ddlTemplate, c);
            System.out.println(ddl);
            JDBC_TEMPLATE.execute(ddl);
        });
    }

}