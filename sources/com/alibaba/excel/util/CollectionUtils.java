package com.alibaba.excel.util;

import java.util.Collection;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/CollectionUtils.class */
public class CollectionUtils {
    private CollectionUtils() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
