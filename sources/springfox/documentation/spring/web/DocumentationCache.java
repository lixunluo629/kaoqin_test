package springfox.documentation.spring.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import springfox.documentation.service.Documentation;

/* loaded from: springfox-spring-web-2.2.2.jar:springfox/documentation/spring/web/DocumentationCache.class */
public class DocumentationCache {
    private Map<String, Documentation> documentationLookup = Maps.newLinkedHashMap();
    private SimpleCacheManager cacheManager = new SimpleCacheManager();

    public DocumentationCache() {
        ConcurrentMapCache operationsCache = new ConcurrentMapCache("operations");
        ConcurrentMapCache modelsCache = new ConcurrentMapCache("models");
        ConcurrentMapCache modelPropertiesCache = new ConcurrentMapCache("modelProperties");
        ConcurrentMapCache modelDependenciesCache = new ConcurrentMapCache("modelDependencies");
        this.cacheManager.setCaches(Lists.newArrayList(operationsCache, modelDependenciesCache, modelsCache, modelPropertiesCache));
        this.cacheManager.afterPropertiesSet();
    }

    public void addDocumentation(Documentation documentation) {
        this.documentationLookup.put(documentation.getGroupName(), documentation);
    }

    public Documentation documentationByGroup(String groupName) {
        return this.documentationLookup.get(groupName);
    }

    public Map<String, Documentation> all() {
        return Collections.unmodifiableMap(this.documentationLookup);
    }

    public Cache getCache(String cacheName) {
        return this.cacheManager.getCache(cacheName);
    }
}
