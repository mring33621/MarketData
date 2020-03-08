
package com.mattring.marketdata.ddl;

        import com.mattring.marketdata.DbAware;

        import java.util.stream.IntStream;

/**
 * @author Matthew
 */
public class CreateTablesMain extends DbAware {

    public static void main(String[] args) {

        final String pointsDdlTemplate =
                "CREATE TABLE IF NOT EXISTS POINTS_%s ("
                        + "EXCH VARCHAR(10), "
                        + "SYM VARCHAR(10), "
                        + "DATE INT, "
                        + "OPEN DOUBLE, "
                        + "HIGH DOUBLE, "
                        + "LOW DOUBLE, "
                        + "CLOSE DOUBLE, "
                        + "VOL BIGINT);";


        IntStream.range(65, 91).forEachOrdered(n -> {
            final String c = "" + ((char) n);
            final String ddl = String.format(pointsDdlTemplate, c);
            System.out.println(ddl);
            JDBC_TEMPLATE.execute(ddl);
        });

    }

}