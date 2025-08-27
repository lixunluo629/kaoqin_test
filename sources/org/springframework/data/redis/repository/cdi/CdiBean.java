package org.springframework.data.redis.repository.cdi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/cdi/CdiBean.class */
public abstract class CdiBean<T> implements Bean<T>, PassivationCapable {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) CdiBean.class);
    protected final BeanManager beanManager;
    private final Set<Annotation> qualifiers;
    private final Set<Type> types;
    private final Class<T> beanClass;
    private final String passivationId;

    public CdiBean(Set<Annotation> qualifiers, Class<T> beanClass, BeanManager beanManager) {
        this(qualifiers, Collections.emptySet(), beanClass, beanManager);
    }

    public CdiBean(Set<Annotation> qualifiers, Set<Type> types, Class<T> beanClass, BeanManager beanManager) {
        Assert.notNull(qualifiers, "Qualifier annotations must not be null!");
        Assert.notNull(beanManager, "BeanManager must not be null!");
        Assert.notNull(types, "Types must not be null!");
        Assert.notNull(beanClass, "Bean class mast not be null!");
        this.qualifiers = qualifiers;
        this.types = types;
        this.beanClass = beanClass;
        this.beanManager = beanManager;
        this.passivationId = createPassivationId(qualifiers, beanClass);
    }

    private final String createPassivationId(Set<Annotation> qualifiers, Class<?> repositoryType) {
        List<String> qualifierNames = new ArrayList<>(qualifiers.size());
        for (Annotation qualifier : qualifiers) {
            qualifierNames.add(qualifier.annotationType().getName());
        }
        Collections.sort(qualifierNames);
        StringBuilder builder = new StringBuilder(StringUtils.collectionToDelimitedString(qualifierNames, ":"));
        builder.append(":").append(repositoryType.getName());
        return builder.toString();
    }

    public Set<Type> getTypes() {
        Set<Type> types = new HashSet<>();
        types.add(this.beanClass);
        types.addAll(Arrays.asList(this.beanClass.getInterfaces()));
        types.addAll(this.types);
        return types;
    }

    protected <S> S getDependencyInstance(Bean<S> bean, Type type) {
        return (S) this.beanManager.getReference(bean, type, this.beanManager.createCreationalContext(bean));
    }

    public final void initialize() {
        create(this.beanManager.createCreationalContext(this));
    }

    public void destroy(T instance, CreationalContext<T> creationalContext) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Destroying bean instance %s for repository type '%s'.", instance.toString(), this.beanClass.getName()));
        }
        creationalContext.release();
    }

    public Set<Annotation> getQualifiers() {
        return this.qualifiers;
    }

    public String getName() {
        return getQualifiers().contains(Default.class) ? this.beanClass.getName() : this.beanClass.getName() + "-" + getQualifiers().toString();
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        Set<Class<? extends Annotation>> stereotypes = new HashSet<>();
        for (Annotation annotation : this.beanClass.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(Stereotype.class)) {
                stereotypes.add(annotationType);
            }
        }
        return stereotypes;
    }

    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    public boolean isAlternative() {
        return this.beanClass.isAnnotationPresent(Alternative.class);
    }

    public boolean isNullable() {
        return false;
    }

    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    public String getId() {
        return this.passivationId;
    }

    public String toString() {
        return String.format("CdiBean: type='%s', qualifiers=%s", this.beanClass.getName(), this.qualifiers.toString());
    }
}
