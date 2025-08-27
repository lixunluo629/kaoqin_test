package org.hyperic.sigar.util;

import org.hyperic.sigar.util.ReferenceMap;

/* loaded from: sigar-1.6.4.jar:org/hyperic/sigar/util/WeakReferenceMap.class */
public class WeakReferenceMap extends ReferenceMap {
    @Override // org.hyperic.sigar.util.ReferenceMap, java.util.AbstractMap, java.util.Map
    public Object put(Object key, Object value) {
        poll();
        return this.map.put(key, new ReferenceMap.WeakValue(key, value, this.queue));
    }
}
