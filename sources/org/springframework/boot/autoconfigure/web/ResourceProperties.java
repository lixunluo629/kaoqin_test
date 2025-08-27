package org.springframework.boot.autoconfigure.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

@ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ResourceProperties.class */
public class ResourceProperties implements ResourceLoaderAware, InitializingBean {
    private static final String[] SERVLET_RESOURCE_LOCATIONS = {"/"};
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};
    private static final String[] RESOURCE_LOCATIONS = new String[CLASSPATH_RESOURCE_LOCATIONS.length + SERVLET_RESOURCE_LOCATIONS.length];
    private Integer cachePeriod;
    private ResourceLoader resourceLoader;
    private String[] staticLocations = RESOURCE_LOCATIONS;
    private boolean addMappings = true;
    private final Chain chain = new Chain();

    static {
        System.arraycopy(SERVLET_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, 0, SERVLET_RESOURCE_LOCATIONS.length);
        System.arraycopy(CLASSPATH_RESOURCE_LOCATIONS, 0, RESOURCE_LOCATIONS, SERVLET_RESOURCE_LOCATIONS.length, CLASSPATH_RESOURCE_LOCATIONS.length);
    }

    @Override // org.springframework.context.ResourceLoaderAware
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.staticLocations = appendSlashIfNecessary(this.staticLocations);
    }

    public String[] getStaticLocations() {
        return this.staticLocations;
    }

    public void setStaticLocations(String[] staticLocations) {
        this.staticLocations = appendSlashIfNecessary(staticLocations);
    }

    private String[] appendSlashIfNecessary(String[] staticLocations) {
        String[] normalized = new String[staticLocations.length];
        for (int i = 0; i < staticLocations.length; i++) {
            String location = staticLocations[i];
            if (location != null) {
                normalized[i] = location.endsWith("/") ? location : location + "/";
            }
        }
        return normalized;
    }

    public Resource getWelcomePage() {
        for (String location : getStaticWelcomePageLocations()) {
            Resource resource = this.resourceLoader.getResource(location);
            if (!resource.exists()) {
                continue;
            } else {
                resource.getURL();
                return resource;
            }
        }
        return null;
    }

    private String[] getStaticWelcomePageLocations() {
        String[] result = new String[this.staticLocations.length];
        for (int i = 0; i < result.length; i++) {
            String location = this.staticLocations[i];
            if (!location.endsWith("/")) {
                location = location + "/";
            }
            result[i] = location + "index.html";
        }
        return result;
    }

    List<Resource> getFaviconLocations() {
        List<Resource> locations = new ArrayList<>(this.staticLocations.length + 1);
        if (this.resourceLoader != null) {
            for (String location : this.staticLocations) {
                locations.add(this.resourceLoader.getResource(location));
            }
        }
        locations.add(new ClassPathResource("/"));
        return Collections.unmodifiableList(locations);
    }

    public Integer getCachePeriod() {
        return this.cachePeriod;
    }

    public void setCachePeriod(Integer cachePeriod) {
        this.cachePeriod = cachePeriod;
    }

    public boolean isAddMappings() {
        return this.addMappings;
    }

    public void setAddMappings(boolean addMappings) {
        this.addMappings = addMappings;
    }

    public Chain getChain() {
        return this.chain;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ResourceProperties$Chain.class */
    public static class Chain {
        private Boolean enabled;
        private boolean cache = true;
        private boolean htmlApplicationCache = false;
        private boolean gzipped = false;

        @NestedConfigurationProperty
        private final Strategy strategy = new Strategy();

        public Boolean getEnabled() {
            return getEnabled(getStrategy().getFixed().isEnabled(), getStrategy().getContent().isEnabled(), this.enabled);
        }

        public void setEnabled(boolean enabled) {
            this.enabled = Boolean.valueOf(enabled);
        }

        public boolean isCache() {
            return this.cache;
        }

        public void setCache(boolean cache) {
            this.cache = cache;
        }

        public Strategy getStrategy() {
            return this.strategy;
        }

        public boolean isHtmlApplicationCache() {
            return this.htmlApplicationCache;
        }

        public void setHtmlApplicationCache(boolean htmlApplicationCache) {
            this.htmlApplicationCache = htmlApplicationCache;
        }

        public boolean isGzipped() {
            return this.gzipped;
        }

        public void setGzipped(boolean gzipped) {
            this.gzipped = gzipped;
        }

        static Boolean getEnabled(boolean fixedEnabled, boolean contentEnabled, Boolean chainEnabled) {
            return (fixedEnabled || contentEnabled) ? Boolean.TRUE : chainEnabled;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ResourceProperties$Strategy.class */
    public static class Strategy {

        @NestedConfigurationProperty
        private final Fixed fixed = new Fixed();

        @NestedConfigurationProperty
        private final Content content = new Content();

        public Fixed getFixed() {
            return this.fixed;
        }

        public Content getContent() {
            return this.content;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ResourceProperties$Content.class */
    public static class Content {
        private boolean enabled;
        private String[] paths = {"/**"};

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getPaths() {
            return this.paths;
        }

        public void setPaths(String[] paths) {
            this.paths = paths;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/web/ResourceProperties$Fixed.class */
    public static class Fixed {
        private boolean enabled;
        private String[] paths = {"/**"};
        private String version;

        public boolean isEnabled() {
            return this.enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getPaths() {
            return this.paths;
        }

        public void setPaths(String[] paths) {
            this.paths = paths;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
