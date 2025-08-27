package org.apache.poi.util;

import java.util.HashMap;
import java.util.Map;

/* loaded from: poi-3.17.jar:org/apache/poi/util/BitFieldFactory.class */
public class BitFieldFactory {
    private static Map<Integer, BitField> instances = new HashMap();

    public static BitField getInstance(int mask) {
        BitField f = instances.get(Integer.valueOf(mask));
        if (f == null) {
            f = new BitField(mask);
            instances.put(Integer.valueOf(mask), f);
        }
        return f;
    }
}
