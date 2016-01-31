package com.mattring.marketdata;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author Matthew
 */
public class PointsToDateSet {

    public Set<Integer> convert(Collection<Point> points) {
        return points.stream().map(Point::getDate).collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
