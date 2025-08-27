package org.springframework.data.redis.core.convert;

import org.springframework.data.geo.Point;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/convert/GeoIndexedPropertyValue.class */
public class GeoIndexedPropertyValue implements IndexedData {
    private final String keyspace;
    private final String indexName;
    private final Point value;

    public GeoIndexedPropertyValue(String keyspace, String indexName, Point value) {
        this.keyspace = keyspace;
        this.indexName = indexName;
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GeoIndexedPropertyValue)) {
            return false;
        }
        GeoIndexedPropertyValue other = (GeoIndexedPropertyValue) o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$keyspace = getKeyspace();
        Object other$keyspace = other.getKeyspace();
        if (this$keyspace == null) {
            if (other$keyspace != null) {
                return false;
            }
        } else if (!this$keyspace.equals(other$keyspace)) {
            return false;
        }
        Object this$indexName = getIndexName();
        Object other$indexName = other.getIndexName();
        if (this$indexName == null) {
            if (other$indexName != null) {
                return false;
            }
        } else if (!this$indexName.equals(other$indexName)) {
            return false;
        }
        Object this$value = getValue();
        Object other$value = other.getValue();
        return this$value == null ? other$value == null : this$value.equals(other$value);
    }

    protected boolean canEqual(Object other) {
        return other instanceof GeoIndexedPropertyValue;
    }

    public int hashCode() {
        Object $keyspace = getKeyspace();
        int result = (1 * 59) + ($keyspace == null ? 43 : $keyspace.hashCode());
        Object $indexName = getIndexName();
        int result2 = (result * 59) + ($indexName == null ? 43 : $indexName.hashCode());
        Object $value = getValue();
        return (result2 * 59) + ($value == null ? 43 : $value.hashCode());
    }

    public String toString() {
        return "GeoIndexedPropertyValue(keyspace=" + getKeyspace() + ", indexName=" + getIndexName() + ", value=" + getValue() + ")";
    }

    public Point getValue() {
        return this.value;
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getIndexName() {
        return geoIndexName(this.indexName);
    }

    @Override // org.springframework.data.redis.core.convert.IndexedData
    public String getKeyspace() {
        return this.keyspace;
    }

    public Point getPoint() {
        return this.value;
    }

    public static String geoIndexName(String path) {
        int index = path.lastIndexOf(46);
        if (index == -1) {
            return path;
        }
        StringBuilder sb = new StringBuilder(path);
        sb.setCharAt(index, ':');
        return sb.toString();
    }
}
