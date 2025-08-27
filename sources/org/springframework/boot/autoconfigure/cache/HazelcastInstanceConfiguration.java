package org.springframework.boot.autoconfigure.cache;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import java.io.Closeable;
import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastConfigResourceCondition;
import org.springframework.boot.autoconfigure.hazelcast.HazelcastInstanceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/HazelcastInstanceConfiguration.class */
abstract class HazelcastInstanceConfiguration {
    HazelcastInstanceConfiguration() {
    }

    @Configuration
    @ConditionalOnSingleCandidate(HazelcastInstance.class)
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/HazelcastInstanceConfiguration$Existing.class */
    static class Existing {
        private final CacheProperties cacheProperties;
        private final CacheManagerCustomizers customizers;

        Existing(CacheProperties cacheProperties, CacheManagerCustomizers customizers) {
            this.cacheProperties = cacheProperties;
            this.customizers = customizers;
        }

        @Bean
        public HazelcastCacheManager cacheManager(HazelcastInstance existingHazelcastInstance) throws IOException {
            Resource config = this.cacheProperties.getHazelcast().getConfig();
            Resource location = this.cacheProperties.resolveConfigLocation(config);
            if (location != null) {
                HazelcastInstance cacheHazelcastInstance = new HazelcastInstanceFactory(location).getHazelcastInstance();
                return new CloseableHazelcastCacheManager(cacheHazelcastInstance);
            }
            HazelcastCacheManager cacheManager = new HazelcastCacheManager(existingHazelcastInstance);
            return this.customizers.customize(cacheManager);
        }
    }

    @ConditionalOnMissingBean({HazelcastInstance.class})
    @Configuration
    @Conditional({ConfigAvailableCondition.class})
    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/HazelcastInstanceConfiguration$Specific.class */
    static class Specific {
        private final CacheProperties cacheProperties;
        private final CacheManagerCustomizers customizers;

        Specific(CacheProperties cacheProperties, CacheManagerCustomizers customizers) {
            this.cacheProperties = cacheProperties;
            this.customizers = customizers;
        }

        @Bean
        public HazelcastInstance hazelcastInstance() throws IOException {
            Resource config = this.cacheProperties.getHazelcast().getConfig();
            Resource location = this.cacheProperties.resolveConfigLocation(config);
            if (location != null) {
                return new HazelcastInstanceFactory(location).getHazelcastInstance();
            }
            return Hazelcast.newHazelcastInstance();
        }

        @Bean
        public HazelcastCacheManager cacheManager() throws IOException {
            HazelcastCacheManager cacheManager = new HazelcastCacheManager(hazelcastInstance());
            return this.customizers.customize(cacheManager);
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/HazelcastInstanceConfiguration$ConfigAvailableCondition.class */
    static class ConfigAvailableCondition extends HazelcastConfigResourceCondition {
        ConfigAvailableCondition() {
            super("spring.cache.hazelcast", "config");
        }
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/HazelcastInstanceConfiguration$CloseableHazelcastCacheManager.class */
    private static class CloseableHazelcastCacheManager extends HazelcastCacheManager implements Closeable {
        private final HazelcastInstance hazelcastInstance;

        CloseableHazelcastCacheManager(HazelcastInstance hazelcastInstance) {
            super(hazelcastInstance);
            this.hazelcastInstance = hazelcastInstance;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.hazelcastInstance.shutdown();
        }
    }
}
