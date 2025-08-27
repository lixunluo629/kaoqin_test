package org.springframework.beans.factory.config;

import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.core.CollectionFactory;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlPropertiesFactoryBean.class */
public class YamlPropertiesFactoryBean extends YamlProcessor implements FactoryBean<Properties>, InitializingBean {
    private boolean singleton = true;
    private Properties properties;

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (isSingleton()) {
            this.properties = createProperties();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Properties getObject() {
        return this.properties != null ? this.properties : createProperties();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return Properties.class;
    }

    protected Properties createProperties() {
        final Properties result = CollectionFactory.createStringAdaptingProperties();
        process(new YamlProcessor.MatchCallback() { // from class: org.springframework.beans.factory.config.YamlPropertiesFactoryBean.1
            @Override // org.springframework.beans.factory.config.YamlProcessor.MatchCallback
            public void process(Properties properties, Map<String, Object> map) {
                result.putAll(properties);
            }
        });
        return result;
    }
}
