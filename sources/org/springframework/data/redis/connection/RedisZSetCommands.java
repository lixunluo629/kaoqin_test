package org.springframework.data.redis.connection;

import java.util.Set;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands.class */
public interface RedisZSetCommands {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands$Aggregate.class */
    public enum Aggregate {
        SUM,
        MIN,
        MAX
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands$Tuple.class */
    public interface Tuple extends Comparable<Double> {
        byte[] getValue();

        Double getScore();
    }

    Boolean zAdd(byte[] bArr, double d, byte[] bArr2);

    Long zAdd(byte[] bArr, Set<Tuple> set);

    Long zRem(byte[] bArr, byte[]... bArr2);

    Double zIncrBy(byte[] bArr, double d, byte[] bArr2);

    Long zRank(byte[] bArr, byte[] bArr2);

    Long zRevRank(byte[] bArr, byte[] bArr2);

    Set<byte[]> zRange(byte[] bArr, long j, long j2);

    Set<Tuple> zRangeWithScores(byte[] bArr, long j, long j2);

    Set<byte[]> zRangeByScore(byte[] bArr, double d, double d2);

    Set<Tuple> zRangeByScoreWithScores(byte[] bArr, Range range);

    Set<Tuple> zRangeByScoreWithScores(byte[] bArr, double d, double d2);

    Set<byte[]> zRangeByScore(byte[] bArr, double d, double d2, long j, long j2);

    Set<Tuple> zRangeByScoreWithScores(byte[] bArr, double d, double d2, long j, long j2);

    Set<Tuple> zRangeByScoreWithScores(byte[] bArr, Range range, Limit limit);

    Set<byte[]> zRevRange(byte[] bArr, long j, long j2);

    Set<Tuple> zRevRangeWithScores(byte[] bArr, long j, long j2);

    Set<byte[]> zRevRangeByScore(byte[] bArr, double d, double d2);

    Set<byte[]> zRevRangeByScore(byte[] bArr, Range range);

    Set<Tuple> zRevRangeByScoreWithScores(byte[] bArr, double d, double d2);

    Set<byte[]> zRevRangeByScore(byte[] bArr, double d, double d2, long j, long j2);

    Set<byte[]> zRevRangeByScore(byte[] bArr, Range range, Limit limit);

    Set<Tuple> zRevRangeByScoreWithScores(byte[] bArr, double d, double d2, long j, long j2);

    Set<Tuple> zRevRangeByScoreWithScores(byte[] bArr, Range range);

    Set<Tuple> zRevRangeByScoreWithScores(byte[] bArr, Range range, Limit limit);

    Long zCount(byte[] bArr, double d, double d2);

    Long zCount(byte[] bArr, Range range);

    Long zCard(byte[] bArr);

    Double zScore(byte[] bArr, byte[] bArr2);

    Long zRemRange(byte[] bArr, long j, long j2);

    Long zRemRangeByScore(byte[] bArr, double d, double d2);

    Long zRemRangeByScore(byte[] bArr, Range range);

    Long zUnionStore(byte[] bArr, byte[]... bArr2);

    Long zUnionStore(byte[] bArr, Aggregate aggregate, int[] iArr, byte[]... bArr2);

    Long zInterStore(byte[] bArr, byte[]... bArr2);

    Long zInterStore(byte[] bArr, Aggregate aggregate, int[] iArr, byte[]... bArr2);

    Cursor<Tuple> zScan(byte[] bArr, ScanOptions scanOptions);

    Set<byte[]> zRangeByScore(byte[] bArr, String str, String str2);

    Set<byte[]> zRangeByScore(byte[] bArr, Range range);

    Set<byte[]> zRangeByScore(byte[] bArr, String str, String str2, long j, long j2);

    Set<byte[]> zRangeByScore(byte[] bArr, Range range, Limit limit);

    Set<byte[]> zRangeByLex(byte[] bArr);

    Set<byte[]> zRangeByLex(byte[] bArr, Range range);

    Set<byte[]> zRangeByLex(byte[] bArr, Range range, Limit limit);

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands$Range.class */
    public static class Range {
        Boundary min;
        Boundary max;

        public static Range range() {
            return new Range();
        }

        public static Range unbounded() {
            Range range = new Range();
            range.min = Boundary.infinite();
            range.max = Boundary.infinite();
            return range;
        }

        public Range gte(Object min) {
            Assert.notNull(min, "Min already set for range.");
            this.min = new Boundary(min, true);
            return this;
        }

        public Range gt(Object min) {
            Assert.notNull(min, "Min already set for range.");
            this.min = new Boundary(min, false);
            return this;
        }

        public Range lte(Object max) {
            Assert.notNull(max, "Max already set for range.");
            this.max = new Boundary(max, true);
            return this;
        }

        public Range lt(Object max) {
            Assert.notNull(max, "Max already set for range.");
            this.max = new Boundary(max, false);
            return this;
        }

        public Boundary getMin() {
            return this.min;
        }

        public Boundary getMax() {
            return this.max;
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands$Range$Boundary.class */
        public static class Boundary {
            Object value;
            boolean including;

            static Boundary infinite() {
                return new Boundary(null, true);
            }

            Boundary(Object value, boolean including) {
                this.value = value;
                this.including = including;
            }

            public Object getValue() {
                return this.value;
            }

            public boolean isIncluding() {
                return this.including;
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisZSetCommands$Limit.class */
    public static class Limit {
        int offset;
        int count;

        public static Limit limit() {
            return new Limit();
        }

        public Limit offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Limit count(int count) {
            this.count = count;
            return this;
        }

        public int getCount() {
            return this.count;
        }

        public int getOffset() {
            return this.offset;
        }
    }
}
