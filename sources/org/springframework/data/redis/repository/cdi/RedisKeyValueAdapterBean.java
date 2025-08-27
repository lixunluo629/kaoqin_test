package org.springframework.data.redis.repository.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/cdi/RedisKeyValueAdapterBean.class */
public class RedisKeyValueAdapterBean extends CdiBean<RedisKeyValueAdapter> {
    private final Bean<RedisOperations<?, ?>> redisOperations;

    /* renamed from: create, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Object m7807create(CreationalContext creationalContext) {
        return create((CreationalContext<RedisKeyValueAdapter>) creationalContext);
    }

    public RedisKeyValueAdapterBean(Bean<RedisOperations<?, ?>> redisOperations, Set<Annotation> qualifiers, BeanManager beanManager) {
        super(qualifiers, RedisKeyValueAdapter.class, beanManager);
        Assert.notNull(redisOperations, "RedisOperations Bean must not be null!");
        this.redisOperations = redisOperations;
    }

    public RedisKeyValueAdapter create(CreationalContext<RedisKeyValueAdapter> creationalContext) {
        Type beanType = getBeanType();
        RedisOperations<?, ?> redisOperations = (RedisOperations) getDependencyInstance(this.redisOperations, beanType);
        RedisKeyValueAdapter redisKeyValueAdapter = new RedisKeyValueAdapter(redisOperations);
        return redisKeyValueAdapter;
    }

    private Type getBeanType() {
        for (Type type : this.redisOperations.getTypes()) {
            if ((type instanceof Class) && RedisOperations.class.isAssignableFrom((Class) type)) {
                return type;
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if ((parameterizedType.getRawType() instanceof Class) && RedisOperations.class.isAssignableFrom((Class) parameterizedType.getRawType())) {
                    return type;
                }
            }
        }
        throw new IllegalStateException("Cannot resolve bean type for class " + RedisOperations.class.getName());
    }

    @Override // org.springframework.data.redis.repository.cdi.CdiBean
    public void destroy(RedisKeyValueAdapter instance, CreationalContext<RedisKeyValueAdapter> creationalContext) {
        if (instance instanceof DisposableBean) {
            try {
                instance.destroy();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        super.destroy((RedisKeyValueAdapterBean) instance, (CreationalContext<RedisKeyValueAdapterBean>) creationalContext);
    }
}
