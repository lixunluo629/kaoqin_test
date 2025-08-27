package org.springframework.data.redis.core.index;

import java.util.Collection;
import org.springframework.data.util.TypeInformation;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/IndexDefinition.class */
public interface IndexDefinition {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/IndexDefinition$Condition.class */
    public interface Condition<T> {
        boolean matches(T t, IndexingContext indexingContext);
    }

    String getKeyspace();

    Collection<Condition<?>> getConditions();

    IndexValueTransformer valueTransformer();

    String getIndexName();

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/index/IndexDefinition$IndexingContext.class */
    public static class IndexingContext {
        private final String keyspace;
        private final String path;
        private final TypeInformation<?> typeInformation;

        public IndexingContext(String keyspace, String path, TypeInformation<?> typeInformation) {
            this.keyspace = keyspace;
            this.path = path;
            this.typeInformation = typeInformation;
        }

        public String getKeyspace() {
            return this.keyspace;
        }

        public String getPath() {
            return this.path;
        }

        public TypeInformation<?> getTypeInformation() {
            return this.typeInformation;
        }
    }
}
