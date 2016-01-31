
package com.mattring.marketdata;

import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

/**
 *
 * @author Matthew
 */
public class PointsToArrays {
    
    public PointsArrays transform(List<Point> points) {
        
        if ( ! (points instanceof RandomAccess) ) {
            throw new IllegalArgumentException("points must be a RandomAccess");
        }
        
        final int len = points.size();
        final double[] opens = new double[len];
        final double[] highs = new double[len];
        final double[] lows = new double[len];
        final double[] closes = new double[len];
        final double[] vols = new double[len];
        for (int i = 0; i < len; i++) {
            Point p = points.get(i);
            opens[i] = p.getOpen();
            highs[i] = p.getHigh();
            lows[i] = p.getLow();
            closes[i] = p.getClose();
            vols[i] = p.getVol();
        }
        
        final List<double[]> arrays = Arrays.asList(opens,highs,lows,closes,vols);
        final String sym = points.get(0).getSym();
        return new PointsArrays(sym, arrays);
    }
}
