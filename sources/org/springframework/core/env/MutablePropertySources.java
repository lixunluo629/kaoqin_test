package org.springframework.core.env;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/MutablePropertySources.class */
public class MutablePropertySources implements PropertySources {
    private final Log logger;
    private final List<PropertySource<?>> propertySourceList;

    public MutablePropertySources() {
        this.propertySourceList = new CopyOnWriteArrayList();
        this.logger = LogFactory.getLog(getClass());
    }

    public MutablePropertySources(PropertySources propertySources) {
        this();
        for (PropertySource<?> propertySource : propertySources) {
            addLast(propertySource);
        }
    }

    MutablePropertySources(Log logger) {
        this.propertySourceList = new CopyOnWriteArrayList();
        this.logger = logger;
    }

    @Override // org.springframework.core.env.PropertySources
    public boolean contains(String name) {
        return this.propertySourceList.contains(PropertySource.named(name));
    }

    @Override // org.springframework.core.env.PropertySources
    public PropertySource<?> get(String name) {
        int index = this.propertySourceList.indexOf(PropertySource.named(name));
        if (index != -1) {
            return this.propertySourceList.get(index);
        }
        return null;
    }

    @Override // java.lang.Iterable
    public Iterator<PropertySource<?>> iterator() {
        return this.propertySourceList.iterator();
    }

    public void addFirst(PropertySource<?> propertySource) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with highest search precedence");
        }
        removeIfPresent(propertySource);
        this.propertySourceList.add(0, propertySource);
    }

    public void addLast(PropertySource<?> propertySource) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with lowest search precedence");
        }
        removeIfPresent(propertySource);
        this.propertySourceList.add(propertySource);
    }

    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with search precedence immediately higher than '" + relativePropertySourceName + "'");
        }
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = assertPresentAndGetIndex(relativePropertySourceName);
        addAtIndex(index, propertySource);
    }

    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Adding PropertySource '" + propertySource.getName() + "' with search precedence immediately lower than '" + relativePropertySourceName + "'");
        }
        assertLegalRelativeAddition(relativePropertySourceName, propertySource);
        removeIfPresent(propertySource);
        int index = assertPresentAndGetIndex(relativePropertySourceName);
        addAtIndex(index + 1, propertySource);
    }

    public int precedenceOf(PropertySource<?> propertySource) {
        return this.propertySourceList.indexOf(propertySource);
    }

    public PropertySource<?> remove(String name) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Removing PropertySource '" + name + "'");
        }
        int index = this.propertySourceList.indexOf(PropertySource.named(name));
        if (index != -1) {
            return this.propertySourceList.remove(index);
        }
        return null;
    }

    public void replace(String name, PropertySource<?> propertySource) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Replacing PropertySource '" + name + "' with '" + propertySource.getName() + "'");
        }
        int index = assertPresentAndGetIndex(name);
        this.propertySourceList.set(index, propertySource);
    }

    public int size() {
        return this.propertySourceList.size();
    }

    public String toString() {
        return this.propertySourceList.toString();
    }

    protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
        String newPropertySourceName = propertySource.getName();
        if (relativePropertySourceName.equals(newPropertySourceName)) {
            throw new IllegalArgumentException("PropertySource named '" + newPropertySourceName + "' cannot be added relative to itself");
        }
    }

    protected void removeIfPresent(PropertySource<?> propertySource) {
        this.propertySourceList.remove(propertySource);
    }

    private void addAtIndex(int index, PropertySource<?> propertySource) {
        removeIfPresent(propertySource);
        this.propertySourceList.add(index, propertySource);
    }

    private int assertPresentAndGetIndex(String name) {
        int index = this.propertySourceList.indexOf(PropertySource.named(name));
        if (index == -1) {
            throw new IllegalArgumentException("PropertySource named '" + name + "' does not exist");
        }
        return index;
    }
}
