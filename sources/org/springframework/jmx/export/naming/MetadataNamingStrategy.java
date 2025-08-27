package org.springframework.jmx.export.naming;

import java.util.Hashtable;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jmx.export.metadata.InvalidMetadataException;
import org.springframework.jmx.export.metadata.JmxAttributeSource;
import org.springframework.jmx.export.metadata.ManagedResource;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/naming/MetadataNamingStrategy.class */
public class MetadataNamingStrategy implements ObjectNamingStrategy, InitializingBean {
    private JmxAttributeSource attributeSource;
    private String defaultDomain;

    public MetadataNamingStrategy() {
    }

    public MetadataNamingStrategy(JmxAttributeSource attributeSource) {
        Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
        this.attributeSource = attributeSource;
    }

    public void setAttributeSource(JmxAttributeSource attributeSource) {
        Assert.notNull(attributeSource, "JmxAttributeSource must not be null");
        this.attributeSource = attributeSource;
    }

    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        if (this.attributeSource == null) {
            throw new IllegalArgumentException("Property 'attributeSource' is required");
        }
    }

    @Override // org.springframework.jmx.export.naming.ObjectNamingStrategy
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException, InvalidMetadataException {
        Class<?> managedClass = AopUtils.getTargetClass(managedBean);
        ManagedResource mr = this.attributeSource.getManagedResource(managedClass);
        if (mr != null && StringUtils.hasText(mr.getObjectName())) {
            return ObjectNameManager.getInstance(mr.getObjectName());
        }
        try {
            return ObjectNameManager.getInstance(beanKey);
        } catch (MalformedObjectNameException e) {
            String domain = this.defaultDomain;
            if (domain == null) {
                domain = ClassUtils.getPackageName(managedClass);
            }
            Hashtable<String, String> properties = new Hashtable<>();
            properties.put("type", ClassUtils.getShortName(managedClass));
            properties.put("name", beanKey);
            return ObjectNameManager.getInstance(domain, properties);
        }
    }
}
