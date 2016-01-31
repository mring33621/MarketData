
package com.mattring.marketdata;

import java.util.List;

/**
 *
 * @author Matthew
 */
public class PointsArrays {
    private final String sym;
    private final List<double[]> arrays;

    public PointsArrays(String sym, List<double[]> arrays) {
        this.sym = sym;
        this.arrays = arrays;
    }

    public String getSym() {
        return sym;
    }

    public List<double[]> getArrays() {
        return arrays;
    }
    
}
