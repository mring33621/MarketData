
package com.mattring.marketdata;

import java.io.Serializable;

/**
 *
 * @author Matthew
 */
public class Point implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String sym;
    private int date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long vol;

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVol() {
        return vol;
    }

    public void setVol(long vol) {
        this.vol = vol;
    }
    
    public double[] getPriceArray() {
        return new double[] {
            open, high, low, close
        };
    }

    @Override
    public String toString() {
        return "Point{" + "sym=" + sym + ", date=" + date + ", open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", vol=" + vol + '}';
    }
    
    
}
