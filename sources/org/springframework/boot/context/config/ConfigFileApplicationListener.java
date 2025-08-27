package org.springframework.boot.context.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.springframework.beans.BeansException;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.env.EnumerableCompositePropertySource;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;

/* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener.class */
public class ConfigFileApplicationListener implements EnvironmentPostProcessor, SmartApplicationListener, Ordered {
    private static final String DEFAULT_PROPERTIES = "defaultProperties";
    private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/";
    private static final String DEFAULT_NAMES = "application";
    public static final String ACTIVE_PROFILES_PROPERTY = "spring.profiles.active";
    public static final String INCLUDE_PROFILES_PROPERTY = "spring.profiles.include";
    public static final String CONFIG_NAME_PROPERTY = "spring.config.name";
    public static final String CONFIG_LOCATION_PROPERTY = "spring.config.location";
    public static final int DEFAULT_ORDER = -2147483638;
    public static final String APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME = "applicationConfigurationProperties";
    private String searchLocations;
    private String names;
    private final DeferredLog logger = new DeferredLog();
    private int order = DEFAULT_ORDER;
    private final ConversionService conversionService = new DefaultConversionService();

    @Override // org.springframework.context.event.SmartApplicationListener
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationEnvironmentPreparedEvent.class.isAssignableFrom(eventType) || ApplicationPreparedEvent.class.isAssignableFrom(eventType);
    }

    @Override // org.springframework.context.event.SmartApplicationListener
    public boolean supportsSourceType(Class<?> aClass) {
        return true;
    }

    @Override // org.springframework.context.ApplicationListener
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) event);
        }
        if (event instanceof ApplicationPreparedEvent) {
            onApplicationPreparedEvent(event);
        }
    }

    private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {
        List<EnvironmentPostProcessor> postProcessors = loadPostProcessors();
        postProcessors.add(this);
        AnnotationAwareOrderComparator.sort(postProcessors);
        for (EnvironmentPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessEnvironment(event.getEnvironment(), event.getSpringApplication());
        }
    }

    List<EnvironmentPostProcessor> loadPostProcessors() {
        return SpringFactoriesLoader.loadFactories(EnvironmentPostProcessor.class, getClass().getClassLoader());
    }

    @Override // org.springframework.boot.env.EnvironmentPostProcessor
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) throws BeansException {
        addPropertySources(environment, application.getResourceLoader());
        configureIgnoreBeanInfo(environment);
        bindToSpringApplication(environment, application);
    }

    private void configureIgnoreBeanInfo(ConfigurableEnvironment environment) {
        if (System.getProperty(CachedIntrospectionResults.IGNORE_BEANINFO_PROPERTY_NAME) == null) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.beaninfo.");
            Boolean ignore = (Boolean) resolver.getProperty("ignore", Boolean.class, Boolean.TRUE);
            System.setProperty(CachedIntrospectionResults.IGNORE_BEANINFO_PROPERTY_NAME, ignore.toString());
        }
    }

    private void onApplicationPreparedEvent(ApplicationEvent event) {
        this.logger.replayTo(ConfigFileApplicationListener.class);
        addPostProcessors(((ApplicationPreparedEvent) event).getApplicationContext());
    }

    protected void addPropertySources(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
        RandomValuePropertySource.addToEnvironment(environment);
        new Loader(environment, resourceLoader).load();
    }

    protected void bindToSpringApplication(ConfigurableEnvironment environment, SpringApplication application) throws BeansException {
        PropertiesConfigurationFactory<SpringApplication> binder = new PropertiesConfigurationFactory<>(application);
        binder.setTargetName("spring.main");
        binder.setConversionService(this.conversionService);
        binder.setPropertySources(environment.getPropertySources());
        try {
            binder.bindPropertiesToTarget();
        } catch (BindException ex) {
            throw new IllegalStateException("Cannot bind to SpringApplication", ex);
        }
    }

    protected void addPostProcessors(ConfigurableApplicationContext context) {
        context.addBeanFactoryPostProcessor(new PropertySourceOrderingPostProcessor(context));
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return this.order;
    }

    public void setSearchLocations(String locations) {
        Assert.hasLength(locations, "Locations must not be empty");
        this.searchLocations = locations;
    }

    public void setSearchNames(String names) {
        Assert.hasLength(names, "Names must not be empty");
        this.names = names;
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener$PropertySourceOrderingPostProcessor.class */
    private class PropertySourceOrderingPostProcessor implements BeanFactoryPostProcessor, Ordered {
        private ConfigurableApplicationContext context;

        PropertySourceOrderingPostProcessor(ConfigurableApplicationContext context) {
            this.context = context;
        }

        @Override // org.springframework.core.Ordered
        public int getOrder() {
            return Integer.MIN_VALUE;
        }

        @Override // org.springframework.beans.factory.config.BeanFactoryPostProcessor
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            reorderSources(this.context.getEnvironment());
        }

        private void reorderSources(ConfigurableEnvironment environment) {
            ConfigurationPropertySources.finishAndRelocate(environment.getPropertySources());
            PropertySource<?> defaultProperties = environment.getPropertySources().remove(ConfigFileApplicationListener.DEFAULT_PROPERTIES);
            if (defaultProperties != null) {
                environment.getPropertySources().addLast(defaultProperties);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener$Loader.class */
    private class Loader {
        private final Log logger;
        private final ConfigurableEnvironment environment;
        private final ResourceLoader resourceLoader;
        private PropertySourcesLoader propertiesLoader;
        private Queue<Profile> profiles;
        private List<Profile> processedProfiles;
        private boolean activatedProfiles;

        Loader(ConfigurableEnvironment environment, ResourceLoader resourceLoader) {
            this.logger = ConfigFileApplicationListener.this.logger;
            this.environment = environment;
            this.resourceLoader = resourceLoader != null ? resourceLoader : new DefaultResourceLoader();
        }

        public void load() {
            this.propertiesLoader = new PropertySourcesLoader();
            this.activatedProfiles = false;
            this.profiles = Collections.asLifoQueue(new LinkedList());
            this.processedProfiles = new LinkedList();
            Set<Profile> initialActiveProfiles = initializeActiveProfiles();
            this.profiles.addAll(getUnprocessedActiveProfiles(initialActiveProfiles));
            if (this.profiles.isEmpty()) {
                for (String defaultProfileName : this.environment.getDefaultProfiles()) {
                    Profile defaultProfile = new Profile(defaultProfileName, true);
                    if (!this.profiles.contains(defaultProfile)) {
                        this.profiles.add(defaultProfile);
                    }
                }
            }
            this.profiles.add(null);
            while (!this.profiles.isEmpty()) {
                Profile profile = this.profiles.poll();
                for (String location : getSearchLocations()) {
                    if (!location.endsWith("/")) {
                        load(location, null, profile);
                    } else {
                        for (String name : getSearchNames()) {
                            load(location, name, profile);
                        }
                    }
                }
                this.processedProfiles.add(profile);
            }
            addConfigurationProperties(this.propertiesLoader.getPropertySources());
        }

        private Set<Profile> initializeActiveProfiles() {
            if (!this.environment.containsProperty("spring.profiles.active") && !this.environment.containsProperty(ConfigFileApplicationListener.INCLUDE_PROFILES_PROPERTY)) {
                return Collections.emptySet();
            }
            SpringProfiles springProfiles = bindSpringProfiles(this.environment.getPropertySources());
            Set<Profile> activeProfiles = new LinkedHashSet<>(springProfiles.getActiveProfiles());
            activeProfiles.addAll(springProfiles.getIncludeProfiles());
            maybeActivateProfiles(activeProfiles);
            return activeProfiles;
        }

        private List<Profile> getUnprocessedActiveProfiles(Set<Profile> initialActiveProfiles) {
            List<Profile> unprocessedActiveProfiles = new ArrayList<>();
            for (String profileName : this.environment.getActiveProfiles()) {
                Profile profile = new Profile(profileName);
                if (!initialActiveProfiles.contains(profile)) {
                    unprocessedActiveProfiles.add(profile);
                }
            }
            Collections.reverse(unprocessedActiveProfiles);
            return unprocessedActiveProfiles;
        }

        private void load(String location, String name, Profile profile) {
            String group = "profile=" + (profile != null ? profile : "");
            if (!StringUtils.hasText(name)) {
                loadIntoGroup(group, location, profile);
                return;
            }
            for (String ext : this.propertiesLoader.getAllFileExtensions()) {
                if (profile != null) {
                    loadIntoGroup(group, location + name + "-" + profile + "." + ext, null);
                    for (Profile processedProfile : this.processedProfiles) {
                        if (processedProfile != null) {
                            loadIntoGroup(group, location + name + "-" + processedProfile + "." + ext, profile);
                        }
                    }
                    loadIntoGroup(group, location + name + "-" + profile + "." + ext, profile);
                }
                loadIntoGroup(group, location + name + "." + ext, profile);
            }
        }

        private PropertySource<?> loadIntoGroup(String identifier, String location, Profile profile) {
            try {
                return doLoadIntoGroup(identifier, location, profile);
            } catch (Exception ex) {
                throw new IllegalStateException("Failed to load property source from location '" + location + "'", ex);
            }
        }

        private PropertySource<?> doLoadIntoGroup(String identifier, String location, Profile profile) throws IOException {
            Resource resource = this.resourceLoader.getResource(location);
            PropertySource<?> propertySource = null;
            StringBuilder msg = new StringBuilder();
            if (resource != null && resource.exists()) {
                String name = "applicationConfig: [" + location + "]";
                String group = "applicationConfig: [" + identifier + "]";
                propertySource = this.propertiesLoader.load(resource, group, name, profile != null ? profile.getName() : null);
                if (propertySource != null) {
                    msg.append("Loaded ");
                    handleProfileProperties(propertySource);
                } else {
                    msg.append("Skipped (empty) ");
                }
            } else {
                msg.append("Skipped ");
            }
            msg.append("config file ");
            msg.append(getResourceDescription(location, resource));
            if (profile != null) {
                msg.append(" for profile ").append(profile);
            }
            if (resource == null || !resource.exists()) {
                msg.append(" resource not found");
                this.logger.trace(msg);
            } else {
                this.logger.debug(msg);
            }
            return propertySource;
        }

        private String getResourceDescription(String location, Resource resource) {
            String resourceDescription = "'" + location + "'";
            if (resource != null) {
                try {
                    resourceDescription = String.format("'%s' (%s)", resource.getURI().toASCIIString(), location);
                } catch (IOException e) {
                }
            }
            return resourceDescription;
        }

        private void handleProfileProperties(PropertySource<?> propertySource) {
            SpringProfiles springProfiles = bindSpringProfiles(propertySource);
            maybeActivateProfiles(springProfiles.getActiveProfiles());
            addProfiles(springProfiles.getIncludeProfiles());
        }

        private SpringProfiles bindSpringProfiles(PropertySource<?> propertySource) {
            MutablePropertySources propertySources = new MutablePropertySources();
            propertySources.addFirst(propertySource);
            return bindSpringProfiles(propertySources);
        }

        private SpringProfiles bindSpringProfiles(PropertySources propertySources) {
            SpringProfiles springProfiles = new SpringProfiles();
            RelaxedDataBinder dataBinder = new RelaxedDataBinder(springProfiles, "spring.profiles");
            dataBinder.bind(new PropertySourcesPropertyValues(propertySources, false));
            springProfiles.setActive(resolvePlaceholders(springProfiles.getActive()));
            springProfiles.setInclude(resolvePlaceholders(springProfiles.getInclude()));
            return springProfiles;
        }

        private List<String> resolvePlaceholders(List<String> values) {
            List<String> resolved = new ArrayList<>();
            for (String value : values) {
                resolved.add(this.environment.resolvePlaceholders(value));
            }
            return resolved;
        }

        private void maybeActivateProfiles(Set<Profile> profiles) {
            if (this.activatedProfiles) {
                if (!profiles.isEmpty()) {
                    this.logger.debug("Profiles already activated, '" + profiles + "' will not be applied");
                }
            } else if (!profiles.isEmpty()) {
                addProfiles(profiles);
                this.logger.debug("Activated profiles " + StringUtils.collectionToCommaDelimitedString(profiles));
                this.activatedProfiles = true;
                removeUnprocessedDefaultProfiles();
            }
        }

        private void removeUnprocessedDefaultProfiles() {
            Iterator<Profile> iterator = this.profiles.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isDefaultProfile()) {
                    iterator.remove();
                }
            }
        }

        private void addProfiles(Set<Profile> profiles) {
            for (Profile profile : profiles) {
                this.profiles.add(profile);
                if (!environmentHasActiveProfile(profile.getName())) {
                    prependProfile(this.environment, profile);
                }
            }
        }

        private boolean environmentHasActiveProfile(String profile) {
            for (String activeProfile : this.environment.getActiveProfiles()) {
                if (activeProfile.equals(profile)) {
                    return true;
                }
            }
            return false;
        }

        private void prependProfile(ConfigurableEnvironment environment, Profile profile) {
            Set<String> profiles = new LinkedHashSet<>();
            environment.getActiveProfiles();
            profiles.add(profile.getName());
            profiles.addAll(Arrays.asList(environment.getActiveProfiles()));
            environment.setActiveProfiles((String[]) profiles.toArray(new String[profiles.size()]));
        }

        private Set<String> getSearchLocations() {
            Set<String> locations = new LinkedHashSet<>();
            if (this.environment.containsProperty(ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY)) {
                for (String path : asResolvedSet(this.environment.getProperty(ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY), null)) {
                    if (!path.contains(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX)) {
                        path = StringUtils.cleanPath(path);
                        if (!ResourceUtils.isUrl(path)) {
                            path = ResourceUtils.FILE_URL_PREFIX + path;
                        }
                    }
                    locations.add(path);
                }
            }
            locations.addAll(asResolvedSet(ConfigFileApplicationListener.this.searchLocations, ConfigFileApplicationListener.DEFAULT_SEARCH_LOCATIONS));
            return locations;
        }

        private Set<String> getSearchNames() {
            if (this.environment.containsProperty(ConfigFileApplicationListener.CONFIG_NAME_PROPERTY)) {
                return asResolvedSet(this.environment.getProperty(ConfigFileApplicationListener.CONFIG_NAME_PROPERTY), null);
            }
            return asResolvedSet(ConfigFileApplicationListener.this.names, "application");
        }

        private Set<String> asResolvedSet(String value, String fallback) {
            List<String> list = Arrays.asList(StringUtils.trimArrayElements(StringUtils.commaDelimitedListToStringArray(value != null ? this.environment.resolvePlaceholders(value) : fallback)));
            Collections.reverse(list);
            return new LinkedHashSet(list);
        }

        private void addConfigurationProperties(MutablePropertySources sources) {
            List<PropertySource<?>> reorderedSources = new ArrayList<>();
            Iterator<PropertySource<?>> it = sources.iterator();
            while (it.hasNext()) {
                PropertySource<?> item = it.next();
                reorderedSources.add(item);
            }
            addConfigurationProperties(new ConfigurationPropertySources(reorderedSources));
        }

        private void addConfigurationProperties(ConfigurationPropertySources configurationSources) {
            MutablePropertySources existingSources = this.environment.getPropertySources();
            if (existingSources.contains(ConfigFileApplicationListener.DEFAULT_PROPERTIES)) {
                existingSources.addBefore(ConfigFileApplicationListener.DEFAULT_PROPERTIES, configurationSources);
            } else {
                existingSources.addLast(configurationSources);
            }
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener$Profile.class */
    private static class Profile {
        private final String name;
        private final boolean defaultProfile;

        Profile(String name) {
            this(name, false);
        }

        Profile(String name, boolean defaultProfile) {
            Assert.notNull(name, "Name must not be null");
            this.name = name;
            this.defaultProfile = defaultProfile;
        }

        public String getName() {
            return this.name;
        }

        public boolean isDefaultProfile() {
            return this.defaultProfile;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            return ((Profile) obj).name.equals(this.name);
        }

        public int hashCode() {
            return this.name.hashCode();
        }

        public String toString() {
            return this.name;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener$ConfigurationPropertySources.class */
    static class ConfigurationPropertySources extends EnumerablePropertySource<Collection<PropertySource<?>>> {
        private final Collection<PropertySource<?>> sources;
        private final String[] names;

        ConfigurationPropertySources(Collection<PropertySource<?>> sources) {
            super(ConfigFileApplicationListener.APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME, sources);
            this.sources = sources;
            List<String> names = new ArrayList<>();
            for (PropertySource<?> source : sources) {
                if (source instanceof EnumerablePropertySource) {
                    names.addAll(Arrays.asList(((EnumerablePropertySource) source).getPropertyNames()));
                }
            }
            this.names = (String[]) names.toArray(new String[names.size()]);
        }

        @Override // org.springframework.core.env.PropertySource
        public Object getProperty(String name) {
            for (PropertySource<?> propertySource : this.sources) {
                Object value = propertySource.getProperty(name);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }

        public static void finishAndRelocate(MutablePropertySources propertySources) {
            String name = ConfigFileApplicationListener.APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME;
            ConfigurationPropertySources removed = (ConfigurationPropertySources) propertySources.get(name);
            if (removed != null) {
                for (PropertySource<?> propertySource : removed.sources) {
                    if (propertySource instanceof EnumerableCompositePropertySource) {
                        EnumerableCompositePropertySource composite = (EnumerableCompositePropertySource) propertySource;
                        for (PropertySource<?> nested : composite.getSource()) {
                            propertySources.addAfter(name, nested);
                            name = nested.getName();
                        }
                    } else {
                        propertySources.addAfter(name, propertySource);
                    }
                }
                propertySources.remove(ConfigFileApplicationListener.APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME);
            }
        }

        @Override // org.springframework.core.env.EnumerablePropertySource
        public String[] getPropertyNames() {
            return this.names;
        }
    }

    /* loaded from: spring-boot-1.5.22.RELEASE.jar:org/springframework/boot/context/config/ConfigFileApplicationListener$SpringProfiles.class */
    static final class SpringProfiles {
        private List<String> active = new ArrayList();
        private List<String> include = new ArrayList();

        SpringProfiles() {
        }

        public List<String> getActive() {
            return this.active;
        }

        public void setActive(List<String> active) {
            this.active = active;
        }

        public List<String> getInclude() {
            return this.include;
        }

        public void setInclude(List<String> include) {
            this.include = include;
        }

        Set<Profile> getActiveProfiles() {
            return asProfileSet(this.active);
        }

        Set<Profile> getIncludeProfiles() {
            return asProfileSet(this.include);
        }

        private Set<Profile> asProfileSet(List<String> profileNames) {
            List<Profile> profiles = new ArrayList<>();
            for (String profileName : profileNames) {
                profiles.add(new Profile(profileName));
            }
            Collections.reverse(profiles);
            return new LinkedHashSet(profiles);
        }
    }
}
