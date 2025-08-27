package org.apache.ibatis.cache;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/cache/NullCacheKey.class */
public final class NullCacheKey extends CacheKey {
    private static final long serialVersionUID = 3704229911977019465L;

    @Override // org.apache.ibatis.cache.CacheKey
    public void update(Object object) {
        throw new CacheException("Not allowed to update a NullCacheKey instance.");
    }

    @Override // org.apache.ibatis.cache.CacheKey
    public void updateAll(Object[] objects) {
        throw new CacheException("Not allowed to update a NullCacheKey instance.");
    }
}
