package org.springframework.data.redis.core.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/CompositeIndexResolver.class */
public class CompositeIndexResolver implements IndexResolver {
    private final List<IndexResolver> resolvers;

    public CompositeIndexResolver(Collection<IndexResolver> resolvers) {
        Assert.notNull(resolvers, "Resolvers must not be null!");
        if (CollectionUtils.contains(resolvers.iterator(), (Object) null)) {
            throw new IllegalArgumentException("Resolvers must no contain null values");
        }
        this.resolvers = new ArrayList(resolvers);
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(TypeInformation<?> typeInformation, Object value) {
        if (this.resolvers.isEmpty()) {
            return Collections.emptySet();
        }
        Set<IndexedData> data = new LinkedHashSet<>();
        for (IndexResolver resolver : this.resolvers) {
            data.addAll(resolver.resolveIndexesFor(typeInformation, value));
        }
        return data;
    }

    @Override // org.springframework.data.redis.core.convert.IndexResolver
    public Set<IndexedData> resolveIndexesFor(String keyspace, String path, TypeInformation<?> typeInformation, Object value) {
        Set<IndexedData> data = new LinkedHashSet<>();
        for (IndexResolver resolver : this.resolvers) {
            data.addAll(resolver.resolveIndexesFor(keyspace, path, typeInformation, value));
        }
        return data;
    }
}
