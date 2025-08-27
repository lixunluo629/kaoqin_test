package org.springframework.context;

import java.io.Closeable;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ProtocolResolver;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/context/ConfigurableApplicationContext.class */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {
    public static final String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
    public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
    public static final String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";
    public static final String ENVIRONMENT_BEAN_NAME = "environment";
    public static final String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";
    public static final String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";

    void setId(String str);

    void setParent(ApplicationContext applicationContext);

    void setEnvironment(ConfigurableEnvironment configurableEnvironment);

    @Override // org.springframework.core.env.EnvironmentCapable
    ConfigurableEnvironment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor);

    void addApplicationListener(ApplicationListener<?> applicationListener);

    void addProtocolResolver(ProtocolResolver protocolResolver);

    void refresh() throws IllegalStateException, BeansException;

    void registerShutdownHook();

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();

    boolean isActive();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;
}
