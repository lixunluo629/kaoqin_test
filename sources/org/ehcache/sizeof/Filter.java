package org.ehcache.sizeof;

import java.lang.reflect.Field;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/Filter.class */
public interface Filter {
    void ignoreInstancesOf(Class cls, boolean z);

    void ignoreField(Field field);
}
