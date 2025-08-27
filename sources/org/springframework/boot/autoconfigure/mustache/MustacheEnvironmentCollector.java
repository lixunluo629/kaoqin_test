package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheEnvironmentCollector.class */
public class MustacheEnvironmentCollector extends DefaultCollector implements EnvironmentAware {
    private ConfigurableEnvironment environment;
    private Map<String, Object> target;
    private RelaxedPropertyResolver propertyResolver;
    private final Mustache.VariableFetcher propertyFetcher = new PropertyVariableFetcher();

    @Override // org.springframework.context.EnvironmentAware
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
        this.target = new HashMap();
        new RelaxedDataBinder(this.target).bind(new PropertySourcesPropertyValues(this.environment.getPropertySources()));
        this.propertyResolver = new RelaxedPropertyResolver(environment);
    }

    public Mustache.VariableFetcher createFetcher(Object ctx, String name) {
        Mustache.VariableFetcher fetcher = super.createFetcher(ctx, name);
        if (fetcher != null) {
            return fetcher;
        }
        if (this.propertyResolver.containsProperty(name)) {
            return this.propertyFetcher;
        }
        return null;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheEnvironmentCollector$PropertyVariableFetcher.class */
    private class PropertyVariableFetcher implements Mustache.VariableFetcher {
        private PropertyVariableFetcher() {
        }

        public Object get(Object ctx, String name) throws Exception {
            return MustacheEnvironmentCollector.this.propertyResolver.getProperty(name);
        }
    }
}
