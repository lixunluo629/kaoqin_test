package org.springframework.boot.bind;

import java.util.Iterator;
import java.util.Map;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/bind/RelaxedPropertyResolver.class */
public class RelaxedPropertyResolver implements PropertyResolver {
    private final PropertyResolver resolver;
    private final String prefix;

    public RelaxedPropertyResolver(PropertyResolver resolver) {
        this(resolver, null);
    }

    public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
        Assert.notNull(resolver, "PropertyResolver must not be null");
        this.resolver = resolver;
        this.prefix = prefix != null ? prefix : "";
    }

    @Override // org.springframework.core.env.PropertyResolver
    public String getRequiredProperty(String key) throws IllegalStateException {
        return (String) getRequiredProperty(key, String.class);
    }

    @Override // org.springframework.core.env.PropertyResolver
    public <T> T getRequiredProperty(String str, Class<T> cls) throws IllegalStateException {
        T t = (T) getProperty(str, cls);
        Assert.state(t != null, String.format("required key [%s] not found", str));
        return t;
    }

    @Override // org.springframework.core.env.PropertyResolver
    public String getProperty(String key) {
        return (String) getProperty(key, String.class, null);
    }

    @Override // org.springframework.core.env.PropertyResolver
    public String getProperty(String key, String defaultValue) {
        return (String) getProperty(key, String.class, defaultValue);
    }

    @Override // org.springframework.core.env.PropertyResolver
    public <T> T getProperty(String str, Class<T> cls) {
        return (T) getProperty(str, cls, null);
    }

    @Override // org.springframework.core.env.PropertyResolver
    public <T> T getProperty(String str, Class<T> cls, T t) {
        RelaxedNames relaxedNames = new RelaxedNames(this.prefix);
        RelaxedNames relaxedNames2 = new RelaxedNames(str);
        Iterator<String> it = relaxedNames.iterator();
        while (it.hasNext()) {
            String next = it.next();
            Iterator<String> it2 = relaxedNames2.iterator();
            while (it2.hasNext()) {
                String next2 = it2.next();
                if (this.resolver.containsProperty(next + next2)) {
                    return (T) this.resolver.getProperty(next + next2, cls);
                }
            }
        }
        return t;
    }

    @Override // org.springframework.core.env.PropertyResolver
    @Deprecated
    public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        Iterator<String> it = prefixes.iterator();
        while (it.hasNext()) {
            String prefix = it.next();
            Iterator<String> it2 = keys.iterator();
            while (it2.hasNext()) {
                String relaxedKey = it2.next();
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return this.resolver.getPropertyAsClass(prefix + relaxedKey, targetType);
                }
            }
        }
        return null;
    }

    @Override // org.springframework.core.env.PropertyResolver
    public boolean containsProperty(String key) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        Iterator<String> it = prefixes.iterator();
        while (it.hasNext()) {
            String prefix = it.next();
            Iterator<String> it2 = keys.iterator();
            while (it2.hasNext()) {
                String relaxedKey = it2.next();
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // org.springframework.core.env.PropertyResolver
    public String resolvePlaceholders(String text) {
        throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
    }

    @Override // org.springframework.core.env.PropertyResolver
    public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Unable to resolve placeholders with relaxed properties");
    }

    public Map<String, Object> getSubProperties(String keyPrefix) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver, "SubProperties not available.");
        ConfigurableEnvironment env = (ConfigurableEnvironment) this.resolver;
        return PropertySourceUtils.getSubProperties(env.getPropertySources(), this.prefix, keyPrefix);
    }

    public static RelaxedPropertyResolver ignoringUnresolvableNestedPlaceholders(Environment environment, String prefix) {
        Assert.notNull(environment, "Environment must not be null");
        PropertyResolver resolver = environment;
        if (environment instanceof ConfigurableEnvironment) {
            resolver = new PropertySourcesPropertyResolver(((ConfigurableEnvironment) environment).getPropertySources());
            ((PropertySourcesPropertyResolver) resolver).setIgnoreUnresolvableNestedPlaceholders(true);
        }
        return new RelaxedPropertyResolver(resolver, prefix);
    }
}
