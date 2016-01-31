
package com.mattring.marketdata;

import java.util.function.Function;

/**
 * assumes incoming format like: 
 * SYM,DATE,OPEN,HIGH,LOW,CLOSE,VOL
 * NFLX,20150102,49.1514,50.3314,48.7314,49.8486,1924900
 * @author mring
 */
public class StringArrayToPointFn implements Function<String[], Point> {

    @Override
    public Point apply(String[] sa) {
        Point p = new Point();
        p.setSym(sa[0].trim());
        p.setDate(Integer.parseInt(sa[1].trim()));
        p.setOpen(Double.parseDouble(sa[2].trim()));
        p.setHigh(Double.parseDouble(sa[3].trim()));
        p.setLow(Double.parseDouble(sa[4].trim()));
        p.setClose(Double.parseDouble(sa[5].trim()));
        p.setVol(Long.parseLong(sa[6].trim()));
        return p;
    }
    
}
