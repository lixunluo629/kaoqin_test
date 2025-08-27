package org.springframework.data.redis.core.convert;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.index.GeoIndexDefinition;
import org.springframework.data.redis.core.index.IndexDefinition;
import org.springframework.data.redis.core.index.SimpleIndexDefinition;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/IndexedDataFactoryProvider.class */
class IndexedDataFactoryProvider {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/IndexedDataFactoryProvider$IndexedDataFactory.class */
    interface IndexedDataFactory {
        IndexedData createIndexedDataFor(Object obj);
    }

    IndexedDataFactoryProvider() {
    }

    IndexedDataFactory getIndexedDataFactory(IndexDefinition definition) {
        if (definition instanceof SimpleIndexDefinition) {
            return new SimpleIndexedPropertyValueFactory((SimpleIndexDefinition) definition);
        }
        if (definition instanceof GeoIndexDefinition) {
            return new GeoIndexedPropertyValueFactory((GeoIndexDefinition) definition);
        }
        return null;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/IndexedDataFactoryProvider$SimpleIndexedPropertyValueFactory.class */
    static class SimpleIndexedPropertyValueFactory implements IndexedDataFactory {
        final SimpleIndexDefinition indexDefinition;

        public SimpleIndexedPropertyValueFactory(SimpleIndexDefinition indexDefinition) {
            this.indexDefinition = indexDefinition;
        }

        @Override // org.springframework.data.redis.core.convert.IndexedDataFactoryProvider.IndexedDataFactory
        public SimpleIndexedPropertyValue createIndexedDataFor(Object value) {
            return new SimpleIndexedPropertyValue(this.indexDefinition.getKeyspace(), this.indexDefinition.getIndexName(), this.indexDefinition.valueTransformer().convert2(value));
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/IndexedDataFactoryProvider$GeoIndexedPropertyValueFactory.class */
    static class GeoIndexedPropertyValueFactory implements IndexedDataFactory {
        final GeoIndexDefinition indexDefinition;

        public GeoIndexedPropertyValueFactory(GeoIndexDefinition indexDefinition) {
            this.indexDefinition = indexDefinition;
        }

        @Override // org.springframework.data.redis.core.convert.IndexedDataFactoryProvider.IndexedDataFactory
        public GeoIndexedPropertyValue createIndexedDataFor(Object value) {
            return new GeoIndexedPropertyValue(this.indexDefinition.getKeyspace(), this.indexDefinition.getPath(), (Point) this.indexDefinition.valueTransformer().convert2(value));
        }
    }
}
