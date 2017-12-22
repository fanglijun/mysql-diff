package com.example.mysqldiff.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapUtils {

    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyGetter) {
        Map<K, V> result = new LinkedHashMap<>();
        for (V item : list) {
            result.put(keyGetter.apply(item), item);
        }
        return result;
    }
}
