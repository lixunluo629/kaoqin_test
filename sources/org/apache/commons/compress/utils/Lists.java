package org.apache.commons.compress.utils;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: commons-compress-1.19.jar:org/apache/commons/compress/utils/Lists.class */
public class Lists {
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    public static <E> ArrayList<E> newArrayList(Iterator<? extends E> iterator) {
        ArrayList<E> list = newArrayList();
        Iterators.addAll(list, iterator);
        return list;
    }

    private Lists() {
    }
}
