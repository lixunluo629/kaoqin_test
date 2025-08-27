package org.springframework.data.util;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/AnnotatedTypeScanner.class */
public class AnnotatedTypeScanner implements ResourceLoaderAware, EnvironmentAware {
    private final Iterable<Class<? extends Annotation>> annotationTypess;
    private final boolean considerInterfaces;
    private ResourceLoader resourceLoader;
    private Environment environment;

    public AnnotatedTypeScanner(Class<? extends Annotation>... annotationTypes) {
        this(true, annotationTypes);
    }

    public AnnotatedTypeScanner(boolean considerInterfaces, Class<? extends Annotation>... annotationTypes) {
        this.annotationTypess = Arrays.asList(annotationTypes);
        this.considerInterfaces = considerInterfaces;
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Set<Class<?>> findTypes(String... basePackages) {
        return findTypes(Arrays.asList(basePackages));
    }

    public Set<Class<?>> findTypes(Iterable<String> basePackages) {
        ClassPathScanningCandidateComponentProvider provider = new InterfaceAwareScanner(this.considerInterfaces);
        if (this.resourceLoader != null) {
            provider.setResourceLoader(this.resourceLoader);
        }
        if (this.environment != null) {
            provider.setEnvironment(this.environment);
        }
        for (Class<? extends Annotation> annotationType : this.annotationTypess) {
            provider.addIncludeFilter(new AnnotationTypeFilter(annotationType, true, this.considerInterfaces));
        }
        Set<Class<?>> types = new HashSet<>();
        for (String basePackage : basePackages) {
            for (BeanDefinition definition : provider.findCandidateComponents(basePackage)) {
                try {
                    types.add(ClassUtils.forName(definition.getBeanClassName(), this.resourceLoader == null ? null : this.resourceLoader.getClassLoader()));
                } catch (ClassNotFoundException o_O) {
                    throw new IllegalStateException(o_O);
                }
            }
        }
        return types;
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/util/AnnotatedTypeScanner$InterfaceAwareScanner.class */
    private static class InterfaceAwareScanner extends ClassPathScanningCandidateComponentProvider {
        private final boolean considerInterfaces;

        public InterfaceAwareScanner(boolean considerInterfaces) {
            super(false);
            this.considerInterfaces = considerInterfaces;
        }

        @Override // org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return super.isCandidateComponent(beanDefinition) || (this.considerInterfaces && beanDefinition.getMetadata().isInterface());
        }
    }
}
