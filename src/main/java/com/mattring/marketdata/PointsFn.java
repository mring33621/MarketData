
package com.mattring.marketdata;

import java.util.List;

/**
 *
 * @author mring
 */
public interface PointsFn {

    List<Point> getAllPointsForSym(String sym);

    List<Point> getAllPointsForSym(String sym, int startDate);
    
}
