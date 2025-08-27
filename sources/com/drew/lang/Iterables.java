package com.drew.lang;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* loaded from: metadata-extractor-2.10.1.jar:com/drew/lang/Iterables.class */
public class Iterables {
    public static <E> List<E> toList(Iterable<E> iterable) {
        ArrayList<E> list = new ArrayList<>();
        for (E item : iterable) {
            list.add(item);
        }
        return list;
    }

    public static <E> Set<E> toSet(Iterable<E> iterable) {
        HashSet<E> set = new HashSet<>();
        for (E item : iterable) {
            set.add(item);
        }
        return set;
    }
}
