package org.springframework.data.redis.repository.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/query/RedisOperationChain.class */
public class RedisOperationChain {
    private Set<PathAndValue> sismember = new LinkedHashSet();
    private Set<PathAndValue> orSismember = new LinkedHashSet();
    private NearPath near;

    public void sismember(String path, Object value) {
        sismember(new PathAndValue(path, value));
    }

    public void sismember(PathAndValue pathAndValue) {
        this.sismember.add(pathAndValue);
    }

    public Set<PathAndValue> getSismember() {
        return this.sismember;
    }

    public void orSismember(String path, Object value) {
        orSismember(new PathAndValue(path, value));
    }

    public void orSismember(PathAndValue pathAndValue) {
        this.orSismember.add(pathAndValue);
    }

    public void orSismember(Collection<PathAndValue> next) {
        this.orSismember.addAll(next);
    }

    public Set<PathAndValue> getOrSismember() {
        return this.orSismember;
    }

    public void near(NearPath near) {
        this.near = near;
    }

    public NearPath getNear() {
        return this.near;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/query/RedisOperationChain$PathAndValue.class */
    public static class PathAndValue {
        private final String path;
        private final Collection<Object> values;

        public PathAndValue(String path, Object singleValue) {
            this.path = path;
            this.values = Collections.singleton(singleValue);
        }

        public PathAndValue(String path, Collection<Object> values) {
            this.path = path;
            this.values = values != null ? values : Collections.emptySet();
        }

        public boolean isSingleValue() {
            return this.values.size() == 1;
        }

        public String getPath() {
            return this.path;
        }

        public Collection<Object> values() {
            return this.values;
        }

        public Object getFirstValue() {
            if (this.values.isEmpty()) {
                return null;
            }
            return this.values.iterator().next();
        }

        public String toString() {
            return this.path + ":" + (isSingleValue() ? getFirstValue() : this.values);
        }

        public int hashCode() {
            int result = ObjectUtils.nullSafeHashCode(this.path);
            return result + ObjectUtils.nullSafeHashCode(this.values);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof PathAndValue)) {
                return false;
            }
            PathAndValue that = (PathAndValue) obj;
            if (!ObjectUtils.nullSafeEquals(this.path, that.path)) {
                return false;
            }
            return ObjectUtils.nullSafeEquals(this.values, that.values);
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/repository/query/RedisOperationChain$NearPath.class */
    public static class NearPath extends PathAndValue {
        public NearPath(String path, Point point, Distance distance) {
            super(path, (Collection<Object>) Arrays.asList(point, distance));
        }

        public Point getPoint() {
            return (Point) getFirstValue();
        }

        public Distance getDistance() {
            Iterator<Object> it = values().iterator();
            it.next();
            return (Distance) it.next();
        }
    }
}
