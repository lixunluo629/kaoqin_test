package org.springframework.data.repository.cdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.config.CustomRepositoryImplementationDetector;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/cdi/CdiRepositoryExtensionSupport.class */
public abstract class CdiRepositoryExtensionSupport implements Extension {
    private static final Logger LOGGER = LoggerFactory.getLogger((Class<?>) CdiRepositoryExtensionSupport.class);
    private final Map<Class<?>, Set<Annotation>> repositoryTypes = new HashMap();
    private final Set<CdiRepositoryBean<?>> eagerRepositories = new HashSet();
    private final CustomRepositoryImplementationDetector customImplementationDetector;

    protected CdiRepositoryExtensionSupport() {
        Environment environment = new StandardEnvironment();
        ResourceLoader resourceLoader = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        this.customImplementationDetector = new CustomRepositoryImplementationDetector(metadataReaderFactory, environment, resourceLoader);
    }

    protected <X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> processAnnotatedType) {
        AnnotatedType<X> annotatedType = processAnnotatedType.getAnnotatedType();
        Class<?> javaClass = annotatedType.getJavaClass();
        if (isRepository(javaClass)) {
            Set<Annotation> qualifiers = getQualifiers(javaClass);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Discovered repository type '%s' with qualifiers %s.", javaClass.getName(), qualifiers));
            }
            this.repositoryTypes.put(javaClass, qualifiers);
        }
    }

    private boolean isRepository(Class<?> type) {
        boolean isInterface = type.isInterface();
        boolean extendsRepository = Repository.class.isAssignableFrom(type);
        boolean isAnnotated = type.isAnnotationPresent(RepositoryDefinition.class);
        boolean excludedByAnnotation = type.isAnnotationPresent(NoRepositoryBean.class);
        return isInterface && (extendsRepository || isAnnotated) && !excludedByAnnotation;
    }

    private Set<Annotation> getQualifiers(Class<?> type) {
        Set<Annotation> qualifiers = new HashSet<>();
        Annotation[] annotations = type.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(Qualifier.class)) {
                qualifiers.add(annotation);
            }
        }
        if (qualifiers.isEmpty()) {
            qualifiers.add(DefaultAnnotationLiteral.INSTANCE);
        }
        qualifiers.add(AnyAnnotationLiteral.INSTANCE);
        return qualifiers;
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager manager) {
        for (CdiRepositoryBean<?> bean : this.eagerRepositories) {
            LOGGER.debug("Eagerly instantiating CDI repository bean for {}.", bean.getBeanClass());
            bean.initialize();
        }
    }

    protected Iterable<Map.Entry<Class<?>, Set<Annotation>>> getRepositoryTypes() {
        return this.repositoryTypes.entrySet();
    }

    protected void registerBean(CdiRepositoryBean<?> bean) {
        Class<?> repositoryInterface = bean.getBeanClass();
        if (AnnotationUtils.findAnnotation(repositoryInterface, Eager.class) != null) {
            this.eagerRepositories.add(bean);
        }
    }

    protected CustomRepositoryImplementationDetector getCustomImplementationDetector() {
        return this.customImplementationDetector;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/cdi/CdiRepositoryExtensionSupport$DefaultAnnotationLiteral.class */
    static class DefaultAnnotationLiteral extends AnnotationLiteral<Default> implements Default {
        private static final long serialVersionUID = 511359421048623933L;
        private static final DefaultAnnotationLiteral INSTANCE = new DefaultAnnotationLiteral();

        DefaultAnnotationLiteral() {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/cdi/CdiRepositoryExtensionSupport$AnyAnnotationLiteral.class */
    static class AnyAnnotationLiteral extends AnnotationLiteral<Any> implements Any {
        private static final long serialVersionUID = 7261821376671361463L;
        private static final AnyAnnotationLiteral INSTANCE = new AnyAnnotationLiteral();

        AnyAnnotationLiteral() {
        }
    }
}
