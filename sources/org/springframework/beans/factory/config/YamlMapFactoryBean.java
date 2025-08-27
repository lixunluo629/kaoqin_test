package org.springframework.beans.factory.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.YamlProcessor;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/factory/config/YamlMapFactoryBean.class */
public class YamlMapFactoryBean extends YamlProcessor implements FactoryBean<Map<String, Object>>, InitializingBean {
    private boolean singleton = true;
    private Map<String, Object> map;

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
            this.map = createMap();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    public Map<String, Object> getObject() {
        return this.map != null ? this.map : createMap();
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<?> getObjectType() {
        return Map.class;
    }

    protected Map<String, Object> createMap() {
        final Map<String, Object> result = new LinkedHashMap<>();
        process(new YamlProcessor.MatchCallback() { // from class: org.springframework.beans.factory.config.YamlMapFactoryBean.1
            @Override // org.springframework.beans.factory.config.YamlProcessor.MatchCallback
            public void process(Properties properties, Map<String, Object> map) {
                YamlMapFactoryBean.this.merge(result, map);
            }
        });
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void merge(Map<String, Object> output, Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object existing = output.get(key);
            if ((value instanceof Map) && (existing instanceof Map)) {
                Map<String, Object> result = new LinkedHashMap<>((Map<? extends String, ? extends Object>) existing);
                merge(result, (Map) value);
                output.put(key, result);
            } else {
                output.put(key, value);
            }
        }
    }
}
