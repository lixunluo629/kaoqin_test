package org.springframework.boot.builder;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ResourceLoader;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/builder/SpringApplicationBuilder.class */
public class SpringApplicationBuilder {
    private final SpringApplication application;
    private ConfigurableApplicationContext context;
    private SpringApplicationBuilder parent;
    private ConfigurableEnvironment environment;
    private boolean registerShutdownHookApplied;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final Set<Object> sources = new LinkedHashSet();
    private final Map<String, Object> defaultProperties = new LinkedHashMap();
    private Set<String> additionalProfiles = new LinkedHashSet();
    private boolean configuredAsChild = false;

    public SpringApplicationBuilder(Object... sources) {
        this.application = createSpringApplication(sources);
    }

    protected SpringApplication createSpringApplication(Object... sources) {
        return new SpringApplication(sources);
    }

    public ConfigurableApplicationContext context() {
        return this.context;
    }

    public SpringApplication application() {
        return this.application;
    }

    public ConfigurableApplicationContext run(String... args) {
        if (this.running.get()) {
            return this.context;
        }
        configureAsChildIfNecessary(args);
        if (this.running.compareAndSet(false, true)) {
            synchronized (this.running) {
                this.context = build().run(args);
            }
        }
        return this.context;
    }

    private void configureAsChildIfNecessary(String... args) {
        if (this.parent != null && !this.configuredAsChild) {
            this.configuredAsChild = true;
            if (!this.registerShutdownHookApplied) {
                this.application.setRegisterShutdownHook(false);
            }
            initializers(new ParentContextApplicationContextInitializer(this.parent.run(args)));
        }
    }

    public SpringApplication build() {
        return build(new String[0]);
    }

    public SpringApplication build(String... args) {
        configureAsChildIfNecessary(args);
        this.application.setSources(this.sources);
        return this.application;
    }

    public SpringApplicationBuilder child(Object... sources) {
        SpringApplicationBuilder child = new SpringApplicationBuilder(new Object[0]);
        child.sources(sources);
        child.properties(this.defaultProperties).environment(this.environment).additionalProfiles(this.additionalProfiles);
        child.parent = this;
        web(false);
        bannerMode(Banner.Mode.OFF);
        this.application.setSources(this.sources);
        return child;
    }

    public SpringApplicationBuilder parent(Object... sources) {
        if (this.parent == null) {
            this.parent = new SpringApplicationBuilder(sources).web(false).properties(this.defaultProperties).environment(this.environment);
        } else {
            this.parent.sources(sources);
        }
        return this.parent;
    }

    private SpringApplicationBuilder runAndExtractParent(String... args) {
        if (this.context == null) {
            run(args);
        }
        if (this.parent != null) {
            return this.parent;
        }
        throw new IllegalStateException("No parent defined yet (please use the other overloaded parent methods to set one)");
    }

    public SpringApplicationBuilder parent(ConfigurableApplicationContext parent) {
        this.parent = new SpringApplicationBuilder(new Object[0]);
        this.parent.context = parent;
        this.parent.running.set(true);
        return this;
    }

    public SpringApplicationBuilder sibling(Object... sources) {
        return runAndExtractParent(new String[0]).child(sources);
    }

    public SpringApplicationBuilder sibling(Object[] sources, String... args) {
        return runAndExtractParent(args).child(sources);
    }

    public SpringApplicationBuilder contextClass(Class<? extends ConfigurableApplicationContext> cls) {
        this.application.setApplicationContextClass(cls);
        return this;
    }

    public SpringApplicationBuilder sources(Object... sources) {
        this.sources.addAll(new LinkedHashSet(Arrays.asList(sources)));
        return this;
    }

    public SpringApplicationBuilder sources(Class<?>... sources) {
        this.sources.addAll(new LinkedHashSet(Arrays.asList(sources)));
        return this;
    }

    public SpringApplicationBuilder web(boolean webEnvironment) {
        this.application.setWebEnvironment(webEnvironment);
        return this;
    }

    public SpringApplicationBuilder logStartupInfo(boolean logStartupInfo) {
        this.application.setLogStartupInfo(logStartupInfo);
        return this;
    }

    public SpringApplicationBuilder banner(Banner banner) {
        this.application.setBanner(banner);
        return this;
    }

    public SpringApplicationBuilder bannerMode(Banner.Mode bannerMode) {
        this.application.setBannerMode(bannerMode);
        return this;
    }

    public SpringApplicationBuilder headless(boolean headless) {
        this.application.setHeadless(headless);
        return this;
    }

    public SpringApplicationBuilder registerShutdownHook(boolean registerShutdownHook) {
        this.registerShutdownHookApplied = true;
        this.application.setRegisterShutdownHook(registerShutdownHook);
        return this;
    }

    public SpringApplicationBuilder main(Class<?> mainApplicationClass) {
        this.application.setMainApplicationClass(mainApplicationClass);
        return this;
    }

    public SpringApplicationBuilder addCommandLineProperties(boolean addCommandLineProperties) {
        this.application.setAddCommandLineProperties(addCommandLineProperties);
        return this;
    }

    public SpringApplicationBuilder properties(String... defaultProperties) {
        return properties(getMapFromKeyValuePairs(defaultProperties));
    }

    private Map<String, Object> getMapFromKeyValuePairs(String[] properties) {
        Map<String, Object> map = new HashMap<>();
        for (String property : properties) {
            int index = lowestIndexOf(property, ":", SymbolConstants.EQUAL_SYMBOL);
            String key = property.substring(0, index > 0 ? index : property.length());
            String value = index > 0 ? property.substring(index + 1) : "";
            map.put(key, value);
        }
        return map;
    }

    private int lowestIndexOf(String property, String... candidates) {
        int index = -1;
        for (String candidate : candidates) {
            int candidateIndex = property.indexOf(candidate);
            if (candidateIndex > 0) {
                index = index != -1 ? Math.min(index, candidateIndex) : candidateIndex;
            }
        }
        return index;
    }

    public SpringApplicationBuilder properties(Properties defaultProperties) {
        return properties(getMapFromProperties(defaultProperties));
    }

    private Map<String, Object> getMapFromProperties(Properties properties) {
        HashMap<String, Object> map = new HashMap<>();
        Iterator it = Collections.list(properties.propertyNames()).iterator();
        while (it.hasNext()) {
            Object key = it.next();
            map.put((String) key, properties.get(key));
        }
        return map;
    }

    public SpringApplicationBuilder properties(Map<String, Object> defaults) {
        this.defaultProperties.putAll(defaults);
        this.application.setDefaultProperties(this.defaultProperties);
        if (this.parent != null) {
            this.parent.properties(this.defaultProperties);
            this.parent.environment(this.environment);
        }
        return this;
    }

    public SpringApplicationBuilder profiles(String... profiles) {
        this.additionalProfiles.addAll(Arrays.asList(profiles));
        this.application.setAdditionalProfiles((String[]) this.additionalProfiles.toArray(new String[this.additionalProfiles.size()]));
        return this;
    }

    private SpringApplicationBuilder additionalProfiles(Collection<String> additionalProfiles) {
        this.additionalProfiles = new LinkedHashSet(additionalProfiles);
        this.application.setAdditionalProfiles((String[]) this.additionalProfiles.toArray(new String[this.additionalProfiles.size()]));
        return this;
    }

    public SpringApplicationBuilder beanNameGenerator(BeanNameGenerator beanNameGenerator) {
        this.application.setBeanNameGenerator(beanNameGenerator);
        return this;
    }

    public SpringApplicationBuilder environment(ConfigurableEnvironment environment) {
        this.application.setEnvironment(environment);
        this.environment = environment;
        return this;
    }

    public SpringApplicationBuilder resourceLoader(ResourceLoader resourceLoader) {
        this.application.setResourceLoader(resourceLoader);
        return this;
    }

    public SpringApplicationBuilder initializers(ApplicationContextInitializer<?>... initializers) {
        this.application.addInitializers(initializers);
        return this;
    }

    public SpringApplicationBuilder listeners(ApplicationListener<?>... listeners) {
        this.application.addListeners(listeners);
        return this;
    }
}
