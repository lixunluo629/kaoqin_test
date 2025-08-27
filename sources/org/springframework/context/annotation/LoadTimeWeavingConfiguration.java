package org.springframework.context.annotation;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.weaving.AspectJWeavingEnabler;
import org.springframework.context.weaving.DefaultContextLoadTimeWeaver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.instrument.classloading.LoadTimeWeaver;

@Configuration
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/annotation/LoadTimeWeavingConfiguration.class */
public class LoadTimeWeavingConfiguration implements ImportAware, BeanClassLoaderAware {
    private AnnotationAttributes enableLTW;
    private LoadTimeWeavingConfigurer ltwConfigurer;
    private ClassLoader beanClassLoader;

    @Override // org.springframework.context.annotation.ImportAware
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableLTW = AnnotationConfigUtils.attributesFor(importMetadata, (Class<?>) EnableLoadTimeWeaving.class);
        if (this.enableLTW == null) {
            throw new IllegalArgumentException("@EnableLoadTimeWeaving is not present on importing class " + importMetadata.getClassName());
        }
    }

    @Autowired(required = false)
    public void setLoadTimeWeavingConfigurer(LoadTimeWeavingConfigurer ltwConfigurer) {
        this.ltwConfigurer = ltwConfigurer;
    }

    @Override // org.springframework.beans.factory.BeanClassLoaderAware
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Bean(name = {ConfigurableApplicationContext.LOAD_TIME_WEAVER_BEAN_NAME})
    @Role(2)
    public LoadTimeWeaver loadTimeWeaver() {
        LoadTimeWeaver loadTimeWeaver = null;
        if (this.ltwConfigurer != null) {
            loadTimeWeaver = this.ltwConfigurer.getLoadTimeWeaver();
        }
        if (loadTimeWeaver == null) {
            loadTimeWeaver = new DefaultContextLoadTimeWeaver(this.beanClassLoader);
        }
        EnableLoadTimeWeaving.AspectJWeaving aspectJWeaving = (EnableLoadTimeWeaving.AspectJWeaving) this.enableLTW.getEnum("aspectjWeaving");
        switch (aspectJWeaving) {
            case AUTODETECT:
                if (this.beanClassLoader.getResource("META-INF/aop.xml") != null) {
                    AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver, this.beanClassLoader);
                    break;
                }
                break;
            case ENABLED:
                AspectJWeavingEnabler.enableAspectJWeaving(loadTimeWeaver, this.beanClassLoader);
                break;
        }
        return loadTimeWeaver;
    }
}
