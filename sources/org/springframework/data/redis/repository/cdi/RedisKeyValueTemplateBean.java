package org.springframework.data.redis.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/cdi/RedisKeyValueTemplateBean.class */
public class RedisKeyValueTemplateBean extends CdiBean<KeyValueOperations> {
    private final Bean<RedisKeyValueAdapter> keyValueAdapter;

    /* renamed from: create, reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Object m7808create(CreationalContext creationalContext) {
        return create((CreationalContext<KeyValueOperations>) creationalContext);
    }

    public RedisKeyValueTemplateBean(Bean<RedisKeyValueAdapter> keyValueAdapter, Set<Annotation> qualifiers, BeanManager beanManager) {
        super(qualifiers, KeyValueOperations.class, beanManager);
        Assert.notNull(keyValueAdapter, "KeyValueAdapter bean must not be null!");
        this.keyValueAdapter = keyValueAdapter;
    }

    public KeyValueOperations create(CreationalContext<KeyValueOperations> creationalContext) {
        RedisKeyValueAdapter keyValueAdapter = (RedisKeyValueAdapter) getDependencyInstance(this.keyValueAdapter, RedisKeyValueAdapter.class);
        RedisMappingContext redisMappingContext = new RedisMappingContext();
        redisMappingContext.afterPropertiesSet();
        RedisKeyValueTemplate redisKeyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, redisMappingContext);
        return redisKeyValueTemplate;
    }

    @Override // org.springframework.data.redis.repository.cdi.CdiBean
    public void destroy(KeyValueOperations instance, CreationalContext<KeyValueOperations> creationalContext) {
        if (instance.getMappingContext() instanceof DisposableBean) {
            try {
                ((DisposableBean) instance.getMappingContext()).destroy();
                instance.destroy();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        super.destroy((RedisKeyValueTemplateBean) instance, (CreationalContext<RedisKeyValueTemplateBean>) creationalContext);
    }
}
