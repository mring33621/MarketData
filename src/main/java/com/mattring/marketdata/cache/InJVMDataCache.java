package com.mattring.marketdata.cache;

import com.mattring.marketdata.DbAware;
import com.mattring.marketdata.Point;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

public class InJVMDataCache extends DbAware {

    private final ConcurrentMap<String, List<Point>> dataCache;

    public InJVMDataCache(int expectedNumberOfSyms) {
        dataCache = new ConcurrentHashMap<>(expectedNumberOfSyms);
    }

    private final List<Point> lookupPoints(String sym) {
        return null;
    }

    public Stream<Point> getData(String sym) {
//        final List<Point> points = dataCache.computeIfAbsent()
        return null;
    }
}
