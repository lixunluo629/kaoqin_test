package org.springframework.data.repository.cdi;

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
import javax.enterprise.inject.Stereotype;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.PassivationCapable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;
import org.springframework.data.repository.config.DefaultRepositoryConfiguration;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/cdi/CdiRepositoryBean.class */
public abstract class CdiRepositoryBean<T> implements Bean<T>, PassivationCapable {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) CdiRepositoryBean.class);
    private static final CdiRepositoryConfiguration DEFAULT_CONFIGURATION = DefaultCdiRepositoryConfiguration.INSTANCE;
    private final Set<Annotation> qualifiers;
    private final Class<T> repositoryType;
    private final CustomRepositoryImplementationDetector detector;
    private final BeanManager beanManager;
    private final String passivationId;
    private transient T repoInstance;

    public CdiRepositoryBean(Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager) {
        this(qualifiers, repositoryType, beanManager, null);
    }

    public CdiRepositoryBean(Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager, CustomRepositoryImplementationDetector detector) {
        Assert.notNull(qualifiers, "Qualifiers must not be null!");
        Assert.notNull(beanManager, "BeanManager must not be null!");
        Assert.notNull(repositoryType, "Repoitory type must not be null!");
        Assert.isTrue(repositoryType.isInterface(), "RepositoryType must be an interface!");
        this.qualifiers = qualifiers;
        this.repositoryType = repositoryType;
        this.beanManager = beanManager;
        this.detector = detector;
        this.passivationId = createPassivationId(qualifiers, repositoryType);
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
        Set<Class> interfaces = new HashSet<>();
        interfaces.add(this.repositoryType);
        interfaces.addAll(Arrays.asList(this.repositoryType.getInterfaces()));
        return new HashSet(interfaces);
    }

    protected <S> S getDependencyInstance(Bean<S> bean, Class<S> cls) {
        return (S) this.beanManager.getReference(bean, cls, this.beanManager.createCreationalContext(bean));
    }

    public final void initialize() {
        create(this.beanManager.createCreationalContext(this));
    }

    public final T create(CreationalContext<T> creationalContext) {
        if (this.repoInstance != null) {
            LOGGER.debug("Returning eagerly created CDI repository instance for {}.", this.repositoryType.getName());
            return this.repoInstance;
        }
        LOGGER.debug("Creating CDI repository bean instance for {}.", this.repositoryType.getName());
        this.repoInstance = create(creationalContext, this.repositoryType);
        return this.repoInstance;
    }

    public void destroy(T instance, CreationalContext<T> creationalContext) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Destroying bean instance %s for repository type '%s'.", instance.toString(), this.repositoryType.getName()));
        }
        creationalContext.release();
    }

    protected CdiRepositoryConfiguration lookupConfiguration(BeanManager beanManager, Set<Annotation> qualifiers) {
        Set<Bean<?>> beans = beanManager.getBeans(CdiRepositoryConfiguration.class, getQualifiersArray(qualifiers));
        if (beans.isEmpty()) {
            return DEFAULT_CONFIGURATION;
        }
        Bean<S> bean = (Bean) beans.iterator().next();
        return (CdiRepositoryConfiguration) getDependencyInstance(bean, bean.getBeanClass());
    }

    private Bean<?> getCustomImplementationBean(Class<?> repositoryType, BeanManager beanManager, Set<Annotation> qualifiers) throws UnsatisfiedResolutionException {
        if (this.detector == null) {
            return null;
        }
        CdiRepositoryConfiguration cdiRepositoryConfiguration = lookupConfiguration(beanManager, qualifiers);
        Class<?> customImplementationClass = getCustomImplementationClass(repositoryType, cdiRepositoryConfiguration);
        if (customImplementationClass == null) {
            return null;
        }
        Set<Bean<?>> beans = beanManager.getBeans(customImplementationClass, getQualifiersArray(qualifiers));
        if (beans.isEmpty()) {
            return null;
        }
        return beans.iterator().next();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: javax.enterprise.inject.UnsatisfiedResolutionException */
    private Class<?> getCustomImplementationClass(Class<?> repositoryType, CdiRepositoryConfiguration cdiRepositoryConfiguration) throws UnsatisfiedResolutionException {
        String className = getCustomImplementationClassName(repositoryType, cdiRepositoryConfiguration);
        AbstractBeanDefinition beanDefinition = this.detector.detectCustomImplementation(className, Collections.singleton(repositoryType.getPackage().getName()), Collections.emptySet());
        if (beanDefinition == null) {
            return null;
        }
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new UnsatisfiedResolutionException(String.format("Unable to resolve class for '%s'", beanDefinition.getBeanClassName()), e);
        }
    }

    private String getCustomImplementationClassName(Class<?> repositoryType, CdiRepositoryConfiguration cdiRepositoryConfiguration) {
        String configuredPostfix = cdiRepositoryConfiguration.getRepositoryImplementationPostfix();
        Assert.hasText(configuredPostfix, "Configured repository postfix must not be null or empty!");
        return ClassUtils.getShortName(repositoryType) + configuredPostfix;
    }

    private Annotation[] getQualifiersArray(Set<Annotation> qualifiers) {
        return (Annotation[]) qualifiers.toArray(new Annotation[qualifiers.size()]);
    }

    public Set<Annotation> getQualifiers() {
        return this.qualifiers;
    }

    public String getName() {
        return this.repositoryType.getName();
    }

    public Set<Class<? extends Annotation>> getStereotypes() {
        Set<Class<? extends Annotation>> stereotypes = new HashSet<>();
        for (Annotation annotation : this.repositoryType.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(Stereotype.class)) {
                stereotypes.add(annotationType);
            }
        }
        return stereotypes;
    }

    public Class<?> getBeanClass() {
        return this.repositoryType;
    }

    public boolean isAlternative() {
        return this.repositoryType.isAnnotationPresent(Alternative.class);
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

    @Deprecated
    protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) throws UnsatisfiedResolutionException {
        Bean<?> customImplementationBean = getCustomImplementationBean(repositoryType, this.beanManager, this.qualifiers);
        Object customImplementation = customImplementationBean == null ? null : this.beanManager.getReference(customImplementationBean, customImplementationBean.getBeanClass(), this.beanManager.createCreationalContext(customImplementationBean));
        return create(creationalContext, repositoryType, customImplementation);
    }

    protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType, Object customImplementation) {
        throw new UnsupportedOperationException("You have to implement create(CreationalContext<T>, Class<T>, Object) in order to use custom repository implementations");
    }

    public String toString() {
        return String.format("CdiRepositoryBean: type='%s', qualifiers=%s", this.repositoryType.getName(), this.qualifiers.toString());
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/cdi/CdiRepositoryBean$DefaultCdiRepositoryConfiguration.class */
    enum DefaultCdiRepositoryConfiguration implements CdiRepositoryConfiguration {
        INSTANCE;

        @Override // org.springframework.data.repository.cdi.CdiRepositoryConfiguration
        public String getRepositoryImplementationPostfix() {
            return DefaultRepositoryConfiguration.DEFAULT_REPOSITORY_IMPLEMENTATION_POSTFIX;
        }
    }
}
