package org.springframework.boot.bind;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.PropertyValues;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/PropertySourcesPropertyValues.class */
public class PropertySourcesPropertyValues implements PropertyValues {
    private static final Pattern COLLECTION_PROPERTY = Pattern.compile("\\[(\\d+)\\](\\.\\S+)?");
    private final PropertySources propertySources;
    private final Collection<String> nonEnumerableFallbackNames;
    private final PropertyNamePatternsMatcher includes;
    private final Map<String, PropertyValue> propertyValues;
    private final ConcurrentHashMap<String, PropertySource<?>> collectionOwners;
    private final boolean resolvePlaceholders;

    public PropertySourcesPropertyValues(PropertySources propertySources) {
        this(propertySources, true);
    }

    public PropertySourcesPropertyValues(PropertySources propertySources, boolean resolvePlaceholders) {
        this(propertySources, (Collection) null, PropertyNamePatternsMatcher.ALL, resolvePlaceholders);
    }

    public PropertySourcesPropertyValues(PropertySources propertySources, Collection<String> includePatterns, Collection<String> nonEnumerableFallbackNames) {
        this(propertySources, nonEnumerableFallbackNames, new PatternPropertyNamePatternsMatcher(includePatterns), true);
    }

    PropertySourcesPropertyValues(PropertySources propertySources, Collection<String> nonEnumerableFallbackNames, PropertyNamePatternsMatcher includes, boolean resolvePlaceholders) {
        this.propertyValues = new LinkedHashMap();
        this.collectionOwners = new ConcurrentHashMap<>();
        Assert.notNull(propertySources, "PropertySources must not be null");
        Assert.notNull(includes, "Includes must not be null");
        this.propertySources = propertySources;
        this.nonEnumerableFallbackNames = nonEnumerableFallbackNames;
        this.includes = includes;
        this.resolvePlaceholders = resolvePlaceholders;
        PropertySourcesPropertyResolver resolver = new PropertySourcesPropertyResolver(propertySources);
        for (PropertySource<?> source : propertySources) {
            processPropertySource(source, resolver);
        }
    }

    private void processPropertySource(PropertySource<?> source, PropertySourcesPropertyResolver resolver) {
        if (source instanceof CompositePropertySource) {
            processCompositePropertySource((CompositePropertySource) source, resolver);
        } else if (source instanceof EnumerablePropertySource) {
            processEnumerablePropertySource((EnumerablePropertySource) source, resolver, this.includes);
        } else {
            processNonEnumerablePropertySource(source, resolver);
        }
    }

    private void processCompositePropertySource(CompositePropertySource source, PropertySourcesPropertyResolver resolver) {
        for (PropertySource<?> nested : source.getPropertySources()) {
            processPropertySource(nested, resolver);
        }
    }

    private void processEnumerablePropertySource(EnumerablePropertySource<?> source, PropertySourcesPropertyResolver resolver, PropertyNamePatternsMatcher includes) {
        if (source.getPropertyNames().length > 0) {
            for (String propertyName : source.getPropertyNames()) {
                if (includes.matches(propertyName)) {
                    Object value = getEnumerableProperty(source, resolver, propertyName);
                    putIfAbsent(propertyName, value, source);
                }
            }
        }
    }

    private Object getEnumerableProperty(EnumerablePropertySource<?> source, PropertySourcesPropertyResolver resolver, String propertyName) {
        try {
            if (this.resolvePlaceholders) {
                return resolver.getProperty(propertyName, Object.class);
            }
        } catch (RuntimeException e) {
        }
        return source.getProperty(propertyName);
    }

    private void processNonEnumerablePropertySource(PropertySource<?> source, PropertySourcesPropertyResolver resolver) {
        if (this.nonEnumerableFallbackNames == null) {
            return;
        }
        for (String propertyName : this.nonEnumerableFallbackNames) {
            if (source.containsProperty(propertyName)) {
                Object value = null;
                try {
                    value = resolver.getProperty(propertyName, (Class<Object>) Object.class);
                } catch (RuntimeException e) {
                }
                if (value == null) {
                    value = source.getProperty(propertyName.toUpperCase(Locale.ENGLISH));
                }
                putIfAbsent(propertyName, value, source);
            }
        }
    }

    @Override // org.springframework.beans.PropertyValues
    public PropertyValue[] getPropertyValues() {
        Collection<PropertyValue> values = this.propertyValues.values();
        return (PropertyValue[]) values.toArray(new PropertyValue[values.size()]);
    }

    @Override // org.springframework.beans.PropertyValues
    public PropertyValue getPropertyValue(String propertyName) {
        PropertyValue propertyValue = this.propertyValues.get(propertyName);
        if (propertyValue != null) {
            return propertyValue;
        }
        for (PropertySource<?> source : this.propertySources) {
            Object value = source.getProperty(propertyName);
            PropertyValue propertyValue2 = putIfAbsent(propertyName, value, source);
            if (propertyValue2 != null) {
                return propertyValue2;
            }
        }
        return null;
    }

    private PropertyValue putIfAbsent(String propertyName, Object value, PropertySource<?> source) {
        if (value != null && !this.propertyValues.containsKey(propertyName)) {
            PropertySource<?> collectionOwner = this.collectionOwners.putIfAbsent(COLLECTION_PROPERTY.matcher(propertyName).replaceAll("[]"), source);
            if (collectionOwner == null || collectionOwner == source) {
                PropertyValue propertyValue = new OriginCapablePropertyValue(propertyName, value, propertyName, source);
                this.propertyValues.put(propertyName, propertyValue);
                return propertyValue;
            }
            return null;
        }
        return null;
    }

    @Override // org.springframework.beans.PropertyValues
    public PropertyValues changesSince(PropertyValues old) {
        MutablePropertyValues changes = new MutablePropertyValues();
        for (PropertyValue newValue : getPropertyValues()) {
            PropertyValue oldValue = old.getPropertyValue(newValue.getName());
            if (oldValue == null || !oldValue.equals(newValue)) {
                changes.addPropertyValue(newValue);
            }
        }
        return changes;
    }

    @Override // org.springframework.beans.PropertyValues
    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    @Override // org.springframework.beans.PropertyValues
    public boolean isEmpty() {
        return this.propertyValues.isEmpty();
    }
}
