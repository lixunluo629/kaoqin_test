package org.springframework.data.redis.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.redis.repository.query.RedisQueryCreator;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/cdi/RedisRepositoryBean.class */
public class RedisRepositoryBean<T> extends CdiRepositoryBean<T> {
    private final Bean<KeyValueOperations> keyValueTemplate;

    public RedisRepositoryBean(Bean<KeyValueOperations> keyValueTemplate, Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager, CustomRepositoryImplementationDetector detector) {
        super(qualifiers, repositoryType, beanManager, detector);
        Assert.notNull(keyValueTemplate, "Bean holding keyvalue template must not be null!");
        this.keyValueTemplate = keyValueTemplate;
    }

    @Override // org.springframework.data.repository.cdi.CdiRepositoryBean
    protected T create(CreationalContext<T> creationalContext, Class<T> cls, Object obj) {
        return (T) new RedisRepositoryFactory((KeyValueOperations) getDependencyInstance(this.keyValueTemplate, KeyValueOperations.class), RedisQueryCreator.class).getRepository(cls, obj);
    }
}
