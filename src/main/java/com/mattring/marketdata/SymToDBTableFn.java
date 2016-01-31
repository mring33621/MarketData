
package com.mattring.marketdata;

import java.util.function.Function;

/**
 *
 * @author Matthew
 */
public class SymToDBTableFn implements Function<String, String> {
    @Override
    public String apply(String sym) {
        return "POINTS_" + sym.charAt(0);
    }
}
