
package com.mattring.marketdata;

import java.util.function.Function;
import org.sql2o.Sql2o;

/**
 *
 * @author Matthew
 */
public class DbAware {
    
    protected final static Sql2o db = new Sql2o("jdbc:h2:file:C:/Data/marketdata/db/db", "sa", "");
    protected final static SymToDBTableFn tblFn = new SymToDBTableFn();
//    protected final static Function<String, String> tblFn = (s) -> { return "POINTS"; };
    
}
