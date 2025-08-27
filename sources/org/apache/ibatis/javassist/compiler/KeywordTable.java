package org.apache.ibatis.javassist.compiler;

import java.util.HashMap;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/compiler/KeywordTable.class */
public final class KeywordTable extends HashMap {
    public int lookup(String name) {
        Object found = get(name);
        if (found == null) {
            return -1;
        }
        return ((Integer) found).intValue();
    }

    public void append(String name, int t) {
        put(name, Integer.valueOf(t));
    }
}
