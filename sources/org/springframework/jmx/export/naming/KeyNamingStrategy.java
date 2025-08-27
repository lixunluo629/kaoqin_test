package org.springframework.jmx.export.naming;

import java.io.IOException;
import java.util.Properties;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/jmx/export/naming/KeyNamingStrategy.class */
public class KeyNamingStrategy implements ObjectNamingStrategy, InitializingBean {
    protected final Log logger = LogFactory.getLog(getClass());
    private Properties mappings;
    private Resource[] mappingLocations;
    private Properties mergedMappings;

    public void setMappings(Properties mappings) {
        this.mappings = mappings;
    }

    public void setMappingLocation(Resource location) {
        this.mappingLocations = new Resource[]{location};
    }

    public void setMappingLocations(Resource... mappingLocations) {
        this.mappingLocations = mappingLocations;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() throws IOException {
        this.mergedMappings = new Properties();
        CollectionUtils.mergePropertiesIntoMap(this.mappings, this.mergedMappings);
        if (this.mappingLocations != null) {
            for (Resource location : this.mappingLocations) {
                if (this.logger.isInfoEnabled()) {
                    this.logger.info("Loading JMX object name mappings file from " + location);
                }
                PropertiesLoaderUtils.fillProperties(this.mergedMappings, location);
            }
        }
    }

    @Override // org.springframework.jmx.export.naming.ObjectNamingStrategy
    public ObjectName getObjectName(Object managedBean, String beanKey) throws MalformedObjectNameException {
        String objectName = null;
        if (this.mergedMappings != null) {
            objectName = this.mergedMappings.getProperty(beanKey);
        }
        if (objectName == null) {
            objectName = beanKey;
        }
        return ObjectNameManager.getInstance(objectName);
    }
}
