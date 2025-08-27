package org.springframework.data.redis.connection;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisGeoCommands.class */
public interface RedisGeoCommands {
    Long geoAdd(byte[] bArr, Point point, byte[] bArr2);

    Long geoAdd(byte[] bArr, GeoLocation<byte[]> geoLocation);

    Long geoAdd(byte[] bArr, Map<byte[], Point> map);

    Long geoAdd(byte[] bArr, Iterable<GeoLocation<byte[]>> iterable);

    Distance geoDist(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Distance geoDist(byte[] bArr, byte[] bArr2, byte[] bArr3, Metric metric);

    List<String> geoHash(byte[] bArr, byte[]... bArr2);

    List<Point> geoPos(byte[] bArr, byte[]... bArr2);

    GeoResults<GeoLocation<byte[]>> geoRadius(byte[] bArr, Circle circle);

    GeoResults<GeoLocation<byte[]>> geoRadius(byte[] bArr, Circle circle, GeoRadiusCommandArgs geoRadiusCommandArgs);

    GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] bArr, byte[] bArr2, double d);

    GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] bArr, byte[] bArr2, Distance distance);

    GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] bArr, byte[] bArr2, Distance distance, GeoRadiusCommandArgs geoRadiusCommandArgs);

    Long geoRemove(byte[] bArr, byte[]... bArr2);

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisGeoCommands$GeoRadiusCommandArgs.class */
    public static class GeoRadiusCommandArgs {
        Set<Flag> flags = new LinkedHashSet(2, 1.0f);
        Long limit;
        Sort.Direction sortDirection;

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisGeoCommands$GeoRadiusCommandArgs$Flag.class */
        public enum Flag {
            WITHCOORD,
            WITHDIST
        }

        private GeoRadiusCommandArgs() {
        }

        public static GeoRadiusCommandArgs newGeoRadiusArgs() {
            return new GeoRadiusCommandArgs();
        }

        public GeoRadiusCommandArgs includeCoordinates() {
            this.flags.add(Flag.WITHCOORD);
            return this;
        }

        public GeoRadiusCommandArgs includeDistance() {
            this.flags.add(Flag.WITHDIST);
            return this;
        }

        public GeoRadiusCommandArgs sortAscending() {
            this.sortDirection = Sort.Direction.ASC;
            return this;
        }

        public GeoRadiusCommandArgs sortDescending() {
            this.sortDirection = Sort.Direction.DESC;
            return this;
        }

        public GeoRadiusCommandArgs limit(long count) {
            Assert.isTrue(count > 0, "Count has to positive value.");
            this.limit = Long.valueOf(count);
            return this;
        }

        public Set<Flag> getFlags() {
            return this.flags;
        }

        public Long getLimit() {
            return this.limit;
        }

        public Sort.Direction getSortDirection() {
            return this.sortDirection;
        }

        public boolean hasFlags() {
            return !this.flags.isEmpty();
        }

        public boolean hasSortDirection() {
            return this.sortDirection != null;
        }

        public boolean hasLimit() {
            return this.limit != null;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisGeoCommands$GeoLocation.class */
    public static class GeoLocation<T> {
        private final T name;
        private final Point point;

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof GeoLocation)) {
                return false;
            }
            GeoLocation<?> other = (GeoLocation) o;
            if (!other.canEqual(this)) {
                return false;
            }
            Object this$name = getName();
            Object other$name = other.getName();
            if (this$name == null) {
                if (other$name != null) {
                    return false;
                }
            } else if (!this$name.equals(other$name)) {
                return false;
            }
            Object this$point = getPoint();
            Object other$point = other.getPoint();
            return this$point == null ? other$point == null : this$point.equals(other$point);
        }

        protected boolean canEqual(Object other) {
            return other instanceof GeoLocation;
        }

        public int hashCode() {
            Object $name = getName();
            int result = (1 * 59) + ($name == null ? 43 : $name.hashCode());
            Object $point = getPoint();
            return (result * 59) + ($point == null ? 43 : $point.hashCode());
        }

        public String toString() {
            return "RedisGeoCommands.GeoLocation(name=" + getName() + ", point=" + getPoint() + ")";
        }

        public GeoLocation(T name, Point point) {
            this.name = name;
            this.point = point;
        }

        public T getName() {
            return this.name;
        }

        public Point getPoint() {
            return this.point;
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisGeoCommands$DistanceUnit.class */
    public enum DistanceUnit implements Metric {
        METERS(6378137.0d, ANSIConstants.ESC_END),
        KILOMETERS(6378.137d, "km"),
        MILES(3963.191d, "mi"),
        FEET(2.0925646325E7d, "ft");

        private final double multiplier;
        private final String abbreviation;

        DistanceUnit(double multiplier, String abbreviation) {
            this.multiplier = multiplier;
            this.abbreviation = abbreviation;
        }

        @Override // org.springframework.data.geo.Metric
        public double getMultiplier() {
            return this.multiplier;
        }

        @Override // org.springframework.data.geo.Metric
        public String getAbbreviation() {
            return this.abbreviation;
        }
    }
}
