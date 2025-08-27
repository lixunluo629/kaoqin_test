package org.springframework.data.querydsl.binding;

import com.querydsl.core.types.EntityPath;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindingsFactory.class */
public class QuerydslBindingsFactory implements ApplicationContextAware {
    private static final String INVALID_DOMAIN_TYPE = "Unable to find Querydsl root type for detected domain type %s! User @%s's root attribute to define the domain type manually!";
    private final EntityPathResolver entityPathResolver;
    private final Map<TypeInformation<?>, EntityPath<?>> entityPaths;
    private AutowireCapableBeanFactory beanFactory;
    private Repositories repositories;

    public QuerydslBindingsFactory(EntityPathResolver entityPathResolver) {
        Assert.notNull(entityPathResolver, "EntityPathResolver must not be null!");
        this.entityPathResolver = entityPathResolver;
        this.entityPaths = new ConcurrentReferenceHashMap();
    }

    @Override // org.springframework.context.ApplicationContextAware
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
        this.repositories = new Repositories(applicationContext);
    }

    public EntityPathResolver getEntityPathResolver() {
        return this.entityPathResolver;
    }

    public QuerydslBindings createBindingsFor(Class<? extends QuerydslBinderCustomizer<?>> customizer, TypeInformation<?> domainType) {
        Assert.notNull(domainType, "Domain type must not be null!");
        EntityPath<?> path = verifyEntityPathPresent(domainType);
        QuerydslBindings bindings = new QuerydslBindings();
        findCustomizerForDomainType(customizer, domainType.getType()).customize(bindings, path);
        return bindings;
    }

    private EntityPath<?> verifyEntityPathPresent(TypeInformation<?> candidate) {
        EntityPath<?> path = this.entityPaths.get(candidate);
        if (path != null) {
            return path;
        }
        Class<?> type = candidate.getType();
        try {
            EntityPath<?> path2 = this.entityPathResolver.createPath(type);
            this.entityPaths.put(candidate, path2);
            return path2;
        } catch (IllegalArgumentException o_O) {
            throw new IllegalStateException(String.format(INVALID_DOMAIN_TYPE, candidate.getType(), QuerydslPredicate.class.getSimpleName()), o_O);
        }
    }

    private QuerydslBinderCustomizer<EntityPath<?>> findCustomizerForDomainType(Class<? extends QuerydslBinderCustomizer> customizer, Class<?> domainType) {
        if (customizer != null && !QuerydslBinderCustomizer.class.equals(customizer)) {
            return createQuerydslBinderCustomizer(customizer);
        }
        if (this.repositories != null && this.repositories.hasRepositoryFor(domainType)) {
            Object repository = this.repositories.getRepositoryFor(domainType);
            if (repository instanceof QuerydslBinderCustomizer) {
                return (QuerydslBinderCustomizer) repository;
            }
        }
        return NoOpCustomizer.INSTANCE;
    }

    private QuerydslBinderCustomizer<EntityPath<?>> createQuerydslBinderCustomizer(Class<? extends QuerydslBinderCustomizer> type) {
        if (this.beanFactory == null) {
            return (QuerydslBinderCustomizer) BeanUtils.instantiateClass(type);
        }
        try {
            return (QuerydslBinderCustomizer) this.beanFactory.getBean(type);
        } catch (NoSuchBeanDefinitionException e) {
            return (QuerydslBinderCustomizer) this.beanFactory.createBean(type);
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/querydsl/binding/QuerydslBindingsFactory$NoOpCustomizer.class */
    private enum NoOpCustomizer implements QuerydslBinderCustomizer<EntityPath<?>> {
        INSTANCE;

        @Override // org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
        public void customize(QuerydslBindings bindings, EntityPath<?> root) {
        }
    }
}
