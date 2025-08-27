package org.apache.ibatis.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/DefaultReflectorFactory.class */
public class DefaultReflectorFactory implements ReflectorFactory {
    private boolean classCacheEnabled = true;
    private final ConcurrentMap<Class<?>, Reflector> reflectorMap = new ConcurrentHashMap();

    @Override // org.apache.ibatis.reflection.ReflectorFactory
    public boolean isClassCacheEnabled() {
        return this.classCacheEnabled;
    }

    @Override // org.apache.ibatis.reflection.ReflectorFactory
    public void setClassCacheEnabled(boolean classCacheEnabled) {
        this.classCacheEnabled = classCacheEnabled;
    }

    @Override // org.apache.ibatis.reflection.ReflectorFactory
    public Reflector findForClass(Class<?> type) {
        if (this.classCacheEnabled) {
            Reflector cached = this.reflectorMap.get(type);
            if (cached == null) {
                cached = new Reflector(type);
                this.reflectorMap.put(type, cached);
            }
            return cached;
        }
        return new Reflector(type);
    }
}
