package ch.qos.logback.classic.turbo;

import java.util.LinkedHashMap;
import java.util.Map;

/* JADX WARN: Classes with same name are omitted:
  logback-classic-1.2.3.jar:ch/qos/logback/classic/turbo/LRUMessageCache.class
 */
/* loaded from: logback-classic-1.2.3.jar.bak:ch/qos/logback/classic/turbo/LRUMessageCache.class */
class LRUMessageCache extends LinkedHashMap<String, Integer> {
    private static final long serialVersionUID = 1;
    final int cacheSize;

    LRUMessageCache(int cacheSize) {
        super((int) (cacheSize * 1.3333334f), 0.75f, true);
        if (cacheSize < 1) {
            throw new IllegalArgumentException("Cache size cannot be smaller than 1");
        }
        this.cacheSize = cacheSize;
    }

    int getMessageCountAndThenIncrement(String msg) {
        Integer i;
        if (msg == null) {
            return 0;
        }
        synchronized (this) {
            Integer i2 = (Integer) super.get(msg);
            if (i2 == null) {
                i = 0;
            } else {
                i = Integer.valueOf(i2.intValue() + 1);
            }
            super.put(msg, i);
        }
        return i.intValue();
    }

    @Override // java.util.LinkedHashMap
    protected boolean removeEldestEntry(Map.Entry<String, Integer> entry) {
        return size() > this.cacheSize;
    }

    @Override // java.util.LinkedHashMap, java.util.HashMap, java.util.AbstractMap, java.util.Map
    public synchronized void clear() {
        super.clear();
    }
}
