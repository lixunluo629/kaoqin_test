package redis.clients.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryRedisPipeline.class */
public interface BinaryRedisPipeline {
    Response<Long> append(byte[] bArr, byte[] bArr2);

    Response<List<byte[]>> blpop(byte[] bArr);

    Response<List<byte[]>> brpop(byte[] bArr);

    Response<Long> decr(byte[] bArr);

    Response<Long> decrBy(byte[] bArr, long j);

    Response<Long> del(byte[] bArr);

    Response<byte[]> echo(byte[] bArr);

    Response<Boolean> exists(byte[] bArr);

    Response<Long> expire(byte[] bArr, int i);

    Response<Long> pexpire(byte[] bArr, long j);

    Response<Long> expireAt(byte[] bArr, long j);

    Response<Long> pexpireAt(byte[] bArr, long j);

    Response<byte[]> get(byte[] bArr);

    Response<Boolean> getbit(byte[] bArr, long j);

    Response<byte[]> getSet(byte[] bArr, byte[] bArr2);

    Response<Long> getrange(byte[] bArr, long j, long j2);

    Response<Long> hdel(byte[] bArr, byte[]... bArr2);

    Response<Boolean> hexists(byte[] bArr, byte[] bArr2);

    Response<byte[]> hget(byte[] bArr, byte[] bArr2);

    Response<Map<byte[], byte[]>> hgetAll(byte[] bArr);

    Response<Long> hincrBy(byte[] bArr, byte[] bArr2, long j);

    Response<Set<byte[]>> hkeys(byte[] bArr);

    Response<Long> hlen(byte[] bArr);

    Response<List<byte[]>> hmget(byte[] bArr, byte[]... bArr2);

    Response<String> hmset(byte[] bArr, Map<byte[], byte[]> map);

    Response<Long> hset(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Long> hsetnx(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<List<byte[]>> hvals(byte[] bArr);

    Response<Long> incr(byte[] bArr);

    Response<Long> incrBy(byte[] bArr, long j);

    Response<byte[]> lindex(byte[] bArr, long j);

    Response<Long> linsert(byte[] bArr, BinaryClient.LIST_POSITION list_position, byte[] bArr2, byte[] bArr3);

    Response<Long> llen(byte[] bArr);

    Response<byte[]> lpop(byte[] bArr);

    Response<Long> lpush(byte[] bArr, byte[]... bArr2);

    Response<Long> lpushx(byte[] bArr, byte[]... bArr2);

    Response<List<byte[]>> lrange(byte[] bArr, long j, long j2);

    Response<Long> lrem(byte[] bArr, long j, byte[] bArr2);

    Response<String> lset(byte[] bArr, long j, byte[] bArr2);

    Response<String> ltrim(byte[] bArr, long j, long j2);

    Response<Long> move(byte[] bArr, int i);

    Response<Long> persist(byte[] bArr);

    Response<byte[]> rpop(byte[] bArr);

    Response<Long> rpush(byte[] bArr, byte[]... bArr2);

    Response<Long> rpushx(byte[] bArr, byte[]... bArr2);

    Response<Long> sadd(byte[] bArr, byte[]... bArr2);

    Response<Long> scard(byte[] bArr);

    Response<String> set(byte[] bArr, byte[] bArr2);

    Response<Boolean> setbit(byte[] bArr, long j, byte[] bArr2);

    Response<Long> setrange(byte[] bArr, long j, byte[] bArr2);

    Response<String> setex(byte[] bArr, int i, byte[] bArr2);

    Response<Long> setnx(byte[] bArr, byte[] bArr2);

    Response<Long> setrange(String str, long j, String str2);

    Response<Set<byte[]>> smembers(byte[] bArr);

    Response<Boolean> sismember(byte[] bArr, byte[] bArr2);

    Response<List<byte[]>> sort(byte[] bArr);

    Response<List<byte[]>> sort(byte[] bArr, SortingParams sortingParams);

    Response<byte[]> spop(byte[] bArr);

    Response<Set<byte[]>> spop(byte[] bArr, long j);

    Response<byte[]> srandmember(byte[] bArr);

    Response<Long> srem(byte[] bArr, byte[]... bArr2);

    Response<Long> strlen(byte[] bArr);

    Response<String> substr(byte[] bArr, int i, int i2);

    Response<Long> ttl(byte[] bArr);

    Response<Long> pttl(byte[] bArr);

    Response<String> type(byte[] bArr);

    Response<Long> zadd(byte[] bArr, double d, byte[] bArr2);

    Response<Long> zadd(byte[] bArr, double d, byte[] bArr2, ZAddParams zAddParams);

    Response<Long> zadd(byte[] bArr, Map<byte[], Double> map);

    Response<Long> zadd(byte[] bArr, Map<byte[], Double> map, ZAddParams zAddParams);

    Response<Long> zcard(byte[] bArr);

    Response<Long> zcount(byte[] bArr, double d, double d2);

    Response<Long> zcount(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Double> zincrby(byte[] bArr, double d, byte[] bArr2);

    Response<Double> zincrby(byte[] bArr, double d, byte[] bArr2, ZIncrByParams zIncrByParams);

    Response<Set<byte[]>> zrange(byte[] bArr, long j, long j2);

    Response<Set<byte[]>> zrangeByScore(byte[] bArr, double d, double d2);

    Response<Set<byte[]>> zrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrangeByScore(byte[] bArr, double d, double d2, int i, int i2);

    Response<Set<byte[]>> zrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] bArr, double d, double d2);

    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] bArr, double d, double d2, int i, int i2);

    Response<Set<Tuple>> zrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Set<byte[]>> zrevrangeByScore(byte[] bArr, double d, double d2);

    Response<Set<byte[]>> zrevrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrevrangeByScore(byte[] bArr, double d, double d2, int i, int i2);

    Response<Set<byte[]>> zrevrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] bArr, double d, double d2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] bArr, double d, double d2, int i, int i2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Set<Tuple>> zrangeWithScores(byte[] bArr, long j, long j2);

    Response<Long> zrank(byte[] bArr, byte[] bArr2);

    Response<Long> zrem(byte[] bArr, byte[]... bArr2);

    Response<Long> zremrangeByRank(byte[] bArr, long j, long j2);

    Response<Long> zremrangeByScore(byte[] bArr, double d, double d2);

    Response<Long> zremrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrevrange(byte[] bArr, long j, long j2);

    Response<Set<Tuple>> zrevrangeWithScores(byte[] bArr, long j, long j2);

    Response<Long> zrevrank(byte[] bArr, byte[] bArr2);

    Response<Double> zscore(byte[] bArr, byte[] bArr2);

    Response<Long> zlexcount(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Set<byte[]>> zrevrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Set<byte[]>> zrevrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Response<Long> zremrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Long> bitcount(byte[] bArr);

    Response<Long> bitcount(byte[] bArr, long j, long j2);

    Response<Long> pfadd(byte[] bArr, byte[]... bArr2);

    Response<Long> pfcount(byte[] bArr);

    Response<Long> geoadd(byte[] bArr, double d, double d2, byte[] bArr2);

    Response<Long> geoadd(byte[] bArr, Map<byte[], GeoCoordinate> map);

    Response<Double> geodist(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Response<Double> geodist(byte[] bArr, byte[] bArr2, byte[] bArr3, GeoUnit geoUnit);

    Response<List<byte[]>> geohash(byte[] bArr, byte[]... bArr2);

    Response<List<GeoCoordinate>> geopos(byte[] bArr, byte[]... bArr2);

    Response<List<GeoRadiusResponse>> georadius(byte[] bArr, double d, double d2, double d3, GeoUnit geoUnit);

    Response<List<GeoRadiusResponse>> georadius(byte[] bArr, double d, double d2, double d3, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    Response<List<GeoRadiusResponse>> georadiusByMember(byte[] bArr, byte[] bArr2, double d, GeoUnit geoUnit);

    Response<List<GeoRadiusResponse>> georadiusByMember(byte[] bArr, byte[] bArr2, double d, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    Response<List<Long>> bitfield(byte[] bArr, byte[]... bArr2);
}
