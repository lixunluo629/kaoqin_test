package org.springframework.core.env;

import org.springframework.core.convert.ConversionException;
import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/PropertySourcesPropertyResolver.class */
public class PropertySourcesPropertyResolver extends AbstractPropertyResolver {
    private final PropertySources propertySources;

    public PropertySourcesPropertyResolver(PropertySources propertySources) {
        this.propertySources = propertySources;
    }

    @Override // org.springframework.core.env.AbstractPropertyResolver, org.springframework.core.env.PropertyResolver
    public boolean containsProperty(String key) {
        if (this.propertySources != null) {
            for (PropertySource<?> propertySource : this.propertySources) {
                if (propertySource.containsProperty(key)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override // org.springframework.core.env.AbstractPropertyResolver, org.springframework.core.env.PropertyResolver
    public String getProperty(String key) {
        return (String) getProperty(key, String.class, true);
    }

    @Override // org.springframework.core.env.PropertyResolver
    public <T> T getProperty(String str, Class<T> cls) {
        return (T) getProperty(str, (Class) cls, true);
    }

    @Override // org.springframework.core.env.AbstractPropertyResolver
    protected String getPropertyAsRawString(String key) {
        return (String) getProperty(key, String.class, false);
    }

    protected <T> T getProperty(String str, Class<T> cls, boolean z) {
        if (this.propertySources != null) {
            for (PropertySource<?> propertySource : this.propertySources) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace("Searching for key '" + str + "' in PropertySource '" + propertySource.getName() + "'");
                }
                Object property = propertySource.getProperty(str);
                if (property != null) {
                    if (z && (property instanceof String)) {
                        property = resolveNestedPlaceholders((String) property);
                    }
                    logKeyFound(str, propertySource, property);
                    return (T) convertValueIfNecessary(property, cls);
                }
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Could not find key '" + str + "' in any property source");
            return null;
        }
        return null;
    }

    @Override // org.springframework.core.env.AbstractPropertyResolver, org.springframework.core.env.PropertyResolver
    @Deprecated
    public <T> Class<T> getPropertyAsClass(String str, Class<T> cls) throws LinkageError {
        Class<?> clsForName;
        if (this.propertySources != null) {
            for (PropertySource<?> propertySource : this.propertySources) {
                if (this.logger.isTraceEnabled()) {
                    this.logger.trace(String.format("Searching for key '%s' in [%s]", str, propertySource.getName()));
                }
                Object property = propertySource.getProperty(str);
                if (property != null) {
                    logKeyFound(str, propertySource, property);
                    if (property instanceof String) {
                        try {
                            clsForName = ClassUtils.forName((String) property, null);
                        } catch (Exception e) {
                            throw new ClassConversionException((String) property, cls, e);
                        }
                    } else if (property instanceof Class) {
                        clsForName = (Class) property;
                    } else {
                        clsForName = property.getClass();
                    }
                    if (!cls.isAssignableFrom(clsForName)) {
                        throw new ClassConversionException(clsForName, cls);
                    }
                    return (Class<T>) clsForName;
                }
            }
        }
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(String.format("Could not find key '%s' in any property source", str));
            return null;
        }
        return null;
    }

    protected void logKeyFound(String key, PropertySource<?> propertySource, Object value) {
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Found key '" + key + "' in PropertySource '" + propertySource.getName() + "' with value of type " + value.getClass().getSimpleName());
        }
    }

    @Deprecated
    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/env/PropertySourcesPropertyResolver$ClassConversionException.class */
    private static class ClassConversionException extends ConversionException {
        public ClassConversionException(Class<?> actual, Class<?> expected) {
            super(String.format("Actual type %s is not assignable to expected type %s", actual.getName(), expected.getName()));
        }

        public ClassConversionException(String actual, Class<?> expected, Exception ex) {
            super(String.format("Could not find/load class %s during attempt to convert to %s", actual, expected.getName()), ex);
        }
    }
}
