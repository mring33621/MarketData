
package com.mattring.marketdata;

/**
 *
 * @author Matthew
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {

    public Pair() {
    }

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }
    
    public T a;
    public U b;
    
}
