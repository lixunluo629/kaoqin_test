package org.springframework.boot.autoconfigure.cache;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.spring.cache.CacheBuilder;
import com.couchbase.client.spring.cache.CouchbaseCacheManager;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Bucket.class, CouchbaseCacheManager.class})
@ConditionalOnSingleCandidate(Bucket.class)
@ConditionalOnMissingBean({CacheManager.class})
@Conditional({CacheCondition.class})
/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/cache/CouchbaseCacheConfiguration.class */
public class CouchbaseCacheConfiguration {
    private final CacheProperties cacheProperties;
    private final CacheManagerCustomizers customizers;
    private final Bucket bucket;

    public CouchbaseCacheConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizers, Bucket bucket) {
        this.cacheProperties = cacheProperties;
        this.customizers = customizers;
        this.bucket = bucket;
    }

    @Bean
    public CouchbaseCacheManager cacheManager() {
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        CouchbaseCacheManager cacheManager = new CouchbaseCacheManager(CacheBuilder.newInstance(this.bucket).withExpiration(this.cacheProperties.getCouchbase().getExpirationSeconds()), (String[]) cacheNames.toArray(new String[cacheNames.size()]));
        return this.customizers.customize(cacheManager);
    }
}
