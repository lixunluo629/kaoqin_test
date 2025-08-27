package org.springframework.boot.autoconfigure.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

@ConfigurationProperties(prefix = "spring.cache")
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties.class */
public class CacheProperties {
    private CacheType type;
    private List<String> cacheNames = new ArrayList();
    private final Caffeine caffeine = new Caffeine();
    private final Couchbase couchbase = new Couchbase();
    private final EhCache ehcache = new EhCache();
    private final Hazelcast hazelcast = new Hazelcast();
    private final Infinispan infinispan = new Infinispan();
    private final JCache jcache = new JCache();
    private final Guava guava = new Guava();

    public CacheType getType() {
        return this.type;
    }

    public void setType(CacheType mode) {
        this.type = mode;
    }

    public List<String> getCacheNames() {
        return this.cacheNames;
    }

    public void setCacheNames(List<String> cacheNames) {
        this.cacheNames = cacheNames;
    }

    public Caffeine getCaffeine() {
        return this.caffeine;
    }

    public Couchbase getCouchbase() {
        return this.couchbase;
    }

    public EhCache getEhcache() {
        return this.ehcache;
    }

    @Deprecated
    public Hazelcast getHazelcast() {
        return this.hazelcast;
    }

    public Infinispan getInfinispan() {
        return this.infinispan;
    }

    public JCache getJcache() {
        return this.jcache;
    }

    public Guava getGuava() {
        return this.guava;
    }

    public Resource resolveConfigLocation(Resource config) {
        if (config != null) {
            Assert.isTrue(config.exists(), "Cache configuration does not exist '" + config.getDescription() + "'");
            return config;
        }
        return null;
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$Caffeine.class */
    public static class Caffeine {
        private String spec;

        public String getSpec() {
            return this.spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$Couchbase.class */
    public static class Couchbase {
        private int expiration;

        public int getExpiration() {
            return this.expiration;
        }

        public int getExpirationSeconds() {
            return (int) TimeUnit.MILLISECONDS.toSeconds(this.expiration);
        }

        public void setExpiration(int expiration) {
            this.expiration = expiration;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$EhCache.class */
    public static class EhCache {
        private Resource config;

        public Resource getConfig() {
            return this.config;
        }

        public void setConfig(Resource config) {
            this.config = config;
        }
    }

    @Deprecated
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$Hazelcast.class */
    public static class Hazelcast {
        private Resource config;

        @DeprecatedConfigurationProperty(replacement = "spring.hazelcast.config", reason = "Use general hazelcast auto-configuration instead.")
        @Deprecated
        public Resource getConfig() {
            return this.config;
        }

        public void setConfig(Resource config) {
            this.config = config;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$Infinispan.class */
    public static class Infinispan {
        private Resource config;

        public Resource getConfig() {
            return this.config;
        }

        public void setConfig(Resource config) {
            this.config = config;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$JCache.class */
    public static class JCache {
        private Resource config;
        private String provider;

        public String getProvider() {
            return this.provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public Resource getConfig() {
            return this.config;
        }

        public void setConfig(Resource config) {
            this.config = config;
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CacheProperties$Guava.class */
    public static class Guava {
        private String spec;

        @DeprecatedConfigurationProperty(reason = "Caffeine will supersede the Guava support in Spring Boot 2.0", replacement = "spring.cache.caffeine.spec")
        @Deprecated
        public String getSpec() {
            return this.spec;
        }

        @Deprecated
        public void setSpec(String spec) {
            this.spec = spec;
        }
    }
}
