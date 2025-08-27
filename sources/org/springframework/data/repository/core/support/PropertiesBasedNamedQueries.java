package org.springframework.data.repository.core.support;

import java.util.Properties;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/PropertiesBasedNamedQueries.class */
public class PropertiesBasedNamedQueries implements NamedQueries {
    public static final NamedQueries EMPTY = new PropertiesBasedNamedQueries(new Properties());
    private final Properties properties;

    public PropertiesBasedNamedQueries(Properties properties) {
        Assert.notNull(properties, "Properties must not be null!");
        this.properties = properties;
    }

    @Override // org.springframework.data.repository.core.NamedQueries
    public boolean hasQuery(String queryName) {
        return this.properties.containsKey(queryName);
    }

    @Override // org.springframework.data.repository.core.NamedQueries
    public String getQuery(String queryName) {
        return this.properties.getProperty(queryName);
    }
}
