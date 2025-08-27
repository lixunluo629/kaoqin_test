package org.apache.ibatis.binding;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/binding/MapperRegistry.class */
public class MapperRegistry {
    private final Configuration config;
    private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap();

    public MapperRegistry(Configuration config) {
        this.config = config;
    }

    public <T> T getMapper(Class<T> cls, SqlSession sqlSession) {
        MapperProxyFactory<?> mapperProxyFactory = this.knownMappers.get(cls);
        if (mapperProxyFactory == null) {
            throw new BindingException("Type " + cls + " is not known to the MapperRegistry.");
        }
        try {
            return (T) mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }
    }

    public <T> boolean hasMapper(Class<T> type) {
        return this.knownMappers.containsKey(type);
    }

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()) {
            if (hasMapper(type)) {
                throw new BindingException("Type " + type + " is already known to the MapperRegistry.");
            }
            boolean loadCompleted = false;
            try {
                this.knownMappers.put(type, new MapperProxyFactory<>(type));
                MapperAnnotationBuilder parser = new MapperAnnotationBuilder(this.config, type);
                parser.parse();
                loadCompleted = true;
                if (1 == 0) {
                    this.knownMappers.remove(type);
                }
            } catch (Throwable th) {
                if (!loadCompleted) {
                    this.knownMappers.remove(type);
                }
                throw th;
            }
        }
    }

    public Collection<Class<?>> getMappers() {
        return Collections.unmodifiableCollection(this.knownMappers.keySet());
    }

    public void addMappers(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> mapperSet = resolverUtil.getClasses();
        Iterator<Class<? extends Class<?>>> it = mapperSet.iterator();
        while (it.hasNext()) {
            addMapper(it.next());
        }
    }

    public void addMappers(String packageName) {
        addMappers(packageName, Object.class);
    }
}
