package org.apache.ibatis.cache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.reflection.ArrayUtil;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/CacheKey.class */
public class CacheKey implements Cloneable, Serializable {
    private static final long serialVersionUID = 1146682552656046210L;
    public static final CacheKey NULL_CACHE_KEY = new NullCacheKey();
    private static final int DEFAULT_MULTIPLYER = 37;
    private static final int DEFAULT_HASHCODE = 17;
    private final int multiplier;
    private int hashcode;
    private long checksum;
    private int count;
    private List<Object> updateList;

    public CacheKey() {
        this.hashcode = 17;
        this.multiplier = 37;
        this.count = 0;
        this.updateList = new ArrayList();
    }

    public CacheKey(Object[] objects) {
        this();
        updateAll(objects);
    }

    public int getUpdateCount() {
        return this.updateList.size();
    }

    public void update(Object object) {
        int baseHashCode = object == null ? 1 : ArrayUtil.hashCode(object);
        this.count++;
        this.checksum += baseHashCode;
        this.hashcode = (this.multiplier * this.hashcode) + (baseHashCode * this.count);
        this.updateList.add(object);
    }

    public void updateAll(Object[] objects) {
        for (Object o : objects) {
            update(o);
        }
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CacheKey)) {
            return false;
        }
        CacheKey cacheKey = (CacheKey) object;
        if (this.hashcode != cacheKey.hashcode || this.checksum != cacheKey.checksum || this.count != cacheKey.count) {
            return false;
        }
        for (int i = 0; i < this.updateList.size(); i++) {
            Object thisObject = this.updateList.get(i);
            Object thatObject = cacheKey.updateList.get(i);
            if (!ArrayUtil.equals(thisObject, thatObject)) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return this.hashcode;
    }

    public String toString() {
        StringBuilder returnValue = new StringBuilder().append(this.hashcode).append(':').append(this.checksum);
        for (Object object : this.updateList) {
            returnValue.append(':').append(ArrayUtil.toString(object));
        }
        return returnValue.toString();
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public CacheKey m3147clone() throws CloneNotSupportedException {
        CacheKey clonedCacheKey = (CacheKey) super.clone();
        clonedCacheKey.updateList = new ArrayList(this.updateList);
        return clonedCacheKey;
    }
}
