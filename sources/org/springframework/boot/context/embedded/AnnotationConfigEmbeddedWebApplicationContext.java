package org.springframework.boot.context.embedded;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScopeMetadataResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/embedded/AnnotationConfigEmbeddedWebApplicationContext.class */
public class AnnotationConfigEmbeddedWebApplicationContext extends EmbeddedWebApplicationContext {
    private final AnnotatedBeanDefinitionReader reader;
    private final ClassPathBeanDefinitionScanner scanner;
    private Class<?>[] annotatedClasses;
    private String[] basePackages;

    public AnnotationConfigEmbeddedWebApplicationContext() {
        this.reader = new AnnotatedBeanDefinitionReader(this);
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }

    public AnnotationConfigEmbeddedWebApplicationContext(Class<?>... annotatedClasses) {
        this();
        register(annotatedClasses);
        refresh();
    }

    public AnnotationConfigEmbeddedWebApplicationContext(String... basePackages) {
        this();
        scan(basePackages);
        refresh();
    }

    @Override // org.springframework.context.support.AbstractApplicationContext, org.springframework.context.ConfigurableApplicationContext
    public void setEnvironment(ConfigurableEnvironment environment) {
        super.setEnvironment(environment);
        this.reader.setEnvironment(environment);
        this.scanner.setEnvironment(environment);
    }

    public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.reader.setBeanNameGenerator(beanNameGenerator);
        this.scanner.setBeanNameGenerator(beanNameGenerator);
        getBeanFactory().registerSingleton(AnnotationConfigUtils.CONFIGURATION_BEAN_NAME_GENERATOR, beanNameGenerator);
    }

    public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver) {
        this.reader.setScopeMetadataResolver(scopeMetadataResolver);
        this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
    }

    public final void register(Class<?>... annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
        Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
    }

    public final void scan(String... basePackages) {
        this.basePackages = basePackages;
        Assert.notEmpty(basePackages, "At least one base package must be specified");
    }

    @Override // org.springframework.context.support.AbstractApplicationContext
    protected void prepareRefresh() {
        this.scanner.clearCache();
        super.prepareRefresh();
    }

    @Override // org.springframework.boot.context.embedded.EmbeddedWebApplicationContext, org.springframework.web.context.support.GenericWebApplicationContext, org.springframework.context.support.AbstractApplicationContext
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeanDefinitionStoreException {
        super.postProcessBeanFactory(beanFactory);
        if (this.basePackages != null && this.basePackages.length > 0) {
            this.scanner.scan(this.basePackages);
        }
        if (this.annotatedClasses != null && this.annotatedClasses.length > 0) {
            this.reader.register(this.annotatedClasses);
        }
    }
}
