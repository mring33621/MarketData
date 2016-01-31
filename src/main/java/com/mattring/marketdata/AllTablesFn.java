
package com.mattring.marketdata;

import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author Matthew
 */
public class AllTablesFn {
    
    static final IntFunction<String> tblFn = i -> "POINTS_" + (char)i;
    
    public List<String> listAll() {
        return IntStream.rangeClosed('A', 'Z').mapToObj(tblFn).collect(Collectors.toList());
    }
}
