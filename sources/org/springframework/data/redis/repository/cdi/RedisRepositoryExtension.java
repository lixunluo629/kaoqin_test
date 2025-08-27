package org.springframework.data.redis.repository.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.ProcessBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.repository.cdi.CdiRepositoryBean;
import org.springframework.data.repository.cdi.CdiRepositoryExtensionSupport;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/cdi/RedisRepositoryExtension.class */
public class RedisRepositoryExtension extends CdiRepositoryExtensionSupport {
    private static final Logger LOG = LoggerFactory.getLogger((Class<?>) RedisRepositoryExtension.class);
    private final Map<Set<Annotation>, Bean<RedisKeyValueAdapter>> redisKeyValueAdapters = new HashMap();
    private final Map<Set<Annotation>, Bean<KeyValueOperations>> redisKeyValueTemplates = new HashMap();
    private final Map<Set<Annotation>, Bean<RedisOperations<?, ?>>> redisOperations = new HashMap();

    public RedisRepositoryExtension() {
        LOG.info("Activating CDI extension for Spring Data Redis repositories.");
    }

    <X> void processBean(@Observes ProcessBean<X> processBean) {
        Bean<RedisOperations<?, ?>> bean = processBean.getBean();
        for (Type type : bean.getTypes()) {
            Type beanType = type;
            if (beanType instanceof ParameterizedType) {
                beanType = ((ParameterizedType) beanType).getRawType();
            }
            if ((beanType instanceof Class) && RedisKeyValueTemplate.class.isAssignableFrom((Class) beanType)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Discovered %s with qualifiers %s.", RedisKeyValueTemplate.class.getName(), bean.getQualifiers()));
                }
                this.redisKeyValueTemplates.put(new HashSet(bean.getQualifiers()), bean);
            }
            if ((beanType instanceof Class) && RedisKeyValueAdapter.class.isAssignableFrom((Class) beanType)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Discovered %s with qualifiers %s.", RedisKeyValueAdapter.class.getName(), bean.getQualifiers()));
                }
                this.redisKeyValueAdapters.put(new HashSet(bean.getQualifiers()), bean);
            }
            if ((beanType instanceof Class) && RedisOperations.class.isAssignableFrom((Class) beanType)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Discovered %s with qualifiers %s.", RedisOperations.class.getName(), bean.getQualifiers()));
                }
                this.redisOperations.put(new HashSet(bean.getQualifiers()), bean);
            }
        }
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) throws UnsatisfiedResolutionException {
        registerDependenciesIfNecessary(afterBeanDiscovery, beanManager);
        for (Map.Entry<Class<?>, Set<Annotation>> entry : getRepositoryTypes()) {
            Class<?> repositoryType = entry.getKey();
            Set<Annotation> qualifiers = entry.getValue();
            CdiRepositoryBean<?> repositoryBean = createRepositoryBean(repositoryType, qualifiers, beanManager);
            if (LOG.isInfoEnabled()) {
                LOG.info(String.format("Registering bean for %s with qualifiers %s.", repositoryType.getName(), qualifiers));
            }
            registerBean(repositoryBean);
            afterBeanDiscovery.addBean(repositoryBean);
        }
    }

    private void registerDependenciesIfNecessary(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) throws UnsatisfiedResolutionException {
        for (Map.Entry<Class<?>, Set<Annotation>> entry : getRepositoryTypes()) {
            Set<Annotation> qualifiers = entry.getValue();
            if (!this.redisKeyValueAdapters.containsKey(qualifiers)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(String.format("Registering bean for %s with qualifiers %s.", RedisKeyValueAdapter.class.getName(), qualifiers));
                }
                RedisKeyValueAdapterBean redisKeyValueAdapterBean = createRedisKeyValueAdapterBean(qualifiers, beanManager);
                this.redisKeyValueAdapters.put(qualifiers, redisKeyValueAdapterBean);
                afterBeanDiscovery.addBean(redisKeyValueAdapterBean);
            }
            if (!this.redisKeyValueTemplates.containsKey(qualifiers)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(String.format("Registering bean for %s with qualifiers %s.", RedisKeyValueTemplate.class.getName(), qualifiers));
                }
                RedisKeyValueTemplateBean redisKeyValueTemplateBean = createRedisKeyValueTemplateBean(qualifiers, beanManager);
                this.redisKeyValueTemplates.put(qualifiers, redisKeyValueTemplateBean);
                afterBeanDiscovery.addBean(redisKeyValueTemplateBean);
            }
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.enterprise.inject.UnsatisfiedResolutionException */
    private <T> CdiRepositoryBean<T> createRepositoryBean(Class<T> repositoryType, Set<Annotation> qualifiers, BeanManager beanManager) throws UnsatisfiedResolutionException {
        Bean<KeyValueOperations> redisKeyValueTemplate = this.redisKeyValueTemplates.get(qualifiers);
        if (redisKeyValueTemplate == null) {
            throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.", RedisKeyValueTemplate.class.getName(), qualifiers));
        }
        return new RedisRepositoryBean(redisKeyValueTemplate, qualifiers, repositoryType, beanManager, getCustomImplementationDetector());
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.enterprise.inject.UnsatisfiedResolutionException */
    private RedisKeyValueAdapterBean createRedisKeyValueAdapterBean(Set<Annotation> qualifiers, BeanManager beanManager) throws UnsatisfiedResolutionException {
        Bean<RedisOperations<?, ?>> redisOperationsBean = this.redisOperations.get(qualifiers);
        if (redisOperationsBean == null) {
            throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.", RedisOperations.class.getName(), qualifiers));
        }
        return new RedisKeyValueAdapterBean(redisOperationsBean, qualifiers, beanManager);
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.enterprise.inject.UnsatisfiedResolutionException */
    private RedisKeyValueTemplateBean createRedisKeyValueTemplateBean(Set<Annotation> qualifiers, BeanManager beanManager) throws UnsatisfiedResolutionException {
        Bean<RedisKeyValueAdapter> redisKeyValueAdapterBean = this.redisKeyValueAdapters.get(qualifiers);
        if (redisKeyValueAdapterBean == null) {
            throw new UnsatisfiedResolutionException(String.format("Unable to resolve a bean for '%s' with qualifiers %s.", RedisKeyValueAdapter.class.getName(), qualifiers));
        }
        return new RedisKeyValueTemplateBean(redisKeyValueAdapterBean, qualifiers, beanManager);
    }
}
