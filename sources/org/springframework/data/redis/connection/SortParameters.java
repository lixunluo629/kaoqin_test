package org.springframework.data.redis.connection;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/SortParameters.class */
public interface SortParameters {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/SortParameters$Order.class */
    public enum Order {
        ASC,
        DESC
    }

    Order getOrder();

    Boolean isAlphabetic();

    byte[] getByPattern();

    byte[][] getGetPattern();

    Range getLimit();

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/SortParameters$Range.class */
    public static class Range {
        private final long start;
        private final long count;

        public Range(long start, long count) {
            this.start = start;
            this.count = count;
        }

        public long getStart() {
            return this.start;
        }

        public long getCount() {
            return this.count;
        }
    }
}
