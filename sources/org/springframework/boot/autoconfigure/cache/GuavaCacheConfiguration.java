package org.springframework.boot.autoconfigure.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Configuration
@ConditionalOnClass({CacheBuilder.class, GuavaCacheManager.class})
@Deprecated
@ConditionalOnMissingBean({CacheManager.class})
@Conditional({CacheCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/GuavaCacheConfiguration.class */
class GuavaCacheConfiguration {
    private final CacheProperties cacheProperties;
    private final CacheManagerCustomizers customizers;
    private final CacheBuilder<Object, Object> cacheBuilder;
    private final CacheBuilderSpec cacheBuilderSpec;
    private final CacheLoader<Object, Object> cacheLoader;

    GuavaCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, ObjectProvider<CacheBuilder<Object, Object>> cacheBuilder, ObjectProvider<CacheBuilderSpec> cacheBuilderSpec, ObjectProvider<CacheLoader<Object, Object>> cacheLoader) {
        this.cacheProperties = cacheProperties;
        this.customizers = customizers;
        this.cacheBuilder = cacheBuilder.getIfAvailable();
        this.cacheBuilderSpec = cacheBuilderSpec.getIfAvailable();
        this.cacheLoader = cacheLoader.getIfAvailable();
    }

    @Bean
    public GuavaCacheManager cacheManager() {
        GuavaCacheManager cacheManager = createCacheManager();
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        if (!CollectionUtils.isEmpty(cacheNames)) {
            cacheManager.setCacheNames(cacheNames);
        }
        return (GuavaCacheManager) this.customizers.customize(cacheManager);
    }

    private GuavaCacheManager createCacheManager() {
        GuavaCacheManager cacheManager = new GuavaCacheManager();
        setCacheBuilder(cacheManager);
        if (this.cacheLoader != null) {
            cacheManager.setCacheLoader(this.cacheLoader);
        }
        return cacheManager;
    }

    private void setCacheBuilder(GuavaCacheManager cacheManager) {
        String specification = this.cacheProperties.getGuava().getSpec();
        if (StringUtils.hasText(specification)) {
            cacheManager.setCacheSpecification(specification);
        } else if (this.cacheBuilderSpec != null) {
            cacheManager.setCacheBuilderSpec(this.cacheBuilderSpec);
        } else if (this.cacheBuilder != null) {
            cacheManager.setCacheBuilder(this.cacheBuilder);
        }
    }
}
