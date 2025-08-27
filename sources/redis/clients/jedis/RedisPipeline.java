package redis.clients.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/RedisPipeline.class */
public interface RedisPipeline {
    Response<Long> append(String str, String str2);

    Response<List<String>> blpop(String str);

    Response<List<String>> brpop(String str);

    Response<Long> decr(String str);

    Response<Long> decrBy(String str, long j);

    Response<Long> del(String str);

    Response<String> echo(String str);

    Response<Boolean> exists(String str);

    Response<Long> expire(String str, int i);

    Response<Long> pexpire(String str, long j);

    Response<Long> expireAt(String str, long j);

    Response<Long> pexpireAt(String str, long j);

    Response<String> get(String str);

    Response<Boolean> getbit(String str, long j);

    Response<String> getrange(String str, long j, long j2);

    Response<String> getSet(String str, String str2);

    Response<Long> hdel(String str, String... strArr);

    Response<Boolean> hexists(String str, String str2);

    Response<String> hget(String str, String str2);

    Response<Map<String, String>> hgetAll(String str);

    Response<Long> hincrBy(String str, String str2, long j);

    Response<Set<String>> hkeys(String str);

    Response<Long> hlen(String str);

    Response<List<String>> hmget(String str, String... strArr);

    Response<String> hmset(String str, Map<String, String> map);

    Response<Long> hset(String str, String str2, String str3);

    Response<Long> hsetnx(String str, String str2, String str3);

    Response<List<String>> hvals(String str);

    Response<Long> incr(String str);

    Response<Long> incrBy(String str, long j);

    Response<String> lindex(String str, long j);

    Response<Long> linsert(String str, BinaryClient.LIST_POSITION list_position, String str2, String str3);

    Response<Long> llen(String str);

    Response<String> lpop(String str);

    Response<Long> lpush(String str, String... strArr);

    Response<Long> lpushx(String str, String... strArr);

    Response<List<String>> lrange(String str, long j, long j2);

    Response<Long> lrem(String str, long j, String str2);

    Response<String> lset(String str, long j, String str2);

    Response<String> ltrim(String str, long j, long j2);

    Response<Long> move(String str, int i);

    Response<Long> persist(String str);

    Response<String> rpop(String str);

    Response<Long> rpush(String str, String... strArr);

    Response<Long> rpushx(String str, String... strArr);

    Response<Long> sadd(String str, String... strArr);

    Response<Long> scard(String str);

    Response<Boolean> sismember(String str, String str2);

    Response<String> set(String str, String str2);

    Response<Boolean> setbit(String str, long j, boolean z);

    Response<String> setex(String str, int i, String str2);

    Response<Long> setnx(String str, String str2);

    Response<Long> setrange(String str, long j, String str2);

    Response<Set<String>> smembers(String str);

    Response<List<String>> sort(String str);

    Response<List<String>> sort(String str, SortingParams sortingParams);

    Response<String> spop(String str);

    Response<Set<String>> spop(String str, long j);

    Response<String> srandmember(String str);

    Response<Long> srem(String str, String... strArr);

    Response<Long> strlen(String str);

    Response<String> substr(String str, int i, int i2);

    Response<Long> ttl(String str);

    Response<Long> pttl(String str);

    Response<String> type(String str);

    Response<Long> zadd(String str, double d, String str2);

    Response<Long> zadd(String str, double d, String str2, ZAddParams zAddParams);

    Response<Long> zadd(String str, Map<String, Double> map);

    Response<Long> zadd(String str, Map<String, Double> map, ZAddParams zAddParams);

    Response<Long> zcard(String str);

    Response<Long> zcount(String str, double d, double d2);

    Response<Long> zcount(String str, String str2, String str3);

    Response<Double> zincrby(String str, double d, String str2);

    Response<Double> zincrby(String str, double d, String str2, ZIncrByParams zIncrByParams);

    Response<Set<String>> zrange(String str, long j, long j2);

    Response<Set<String>> zrangeByScore(String str, double d, double d2);

    Response<Set<String>> zrangeByScore(String str, String str2, String str3);

    Response<Set<String>> zrangeByScore(String str, double d, double d2, int i, int i2);

    Response<Set<String>> zrangeByScore(String str, String str2, String str3, int i, int i2);

    Response<Set<Tuple>> zrangeByScoreWithScores(String str, double d, double d2);

    Response<Set<Tuple>> zrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    Response<Set<String>> zrevrangeByScore(String str, double d, double d2);

    Response<Set<String>> zrevrangeByScore(String str, String str2, String str3);

    Response<Set<String>> zrevrangeByScore(String str, double d, double d2, int i, int i2);

    Response<Set<String>> zrevrangeByScore(String str, String str2, String str3, int i, int i2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(String str, double d, double d2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(String str, String str2, String str3);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    Response<Set<Tuple>> zrevrangeByScoreWithScores(String str, String str2, String str3, int i, int i2);

    Response<Set<Tuple>> zrangeWithScores(String str, long j, long j2);

    Response<Long> zrank(String str, String str2);

    Response<Long> zrem(String str, String... strArr);

    Response<Long> zremrangeByRank(String str, long j, long j2);

    Response<Long> zremrangeByScore(String str, double d, double d2);

    Response<Long> zremrangeByScore(String str, String str2, String str3);

    Response<Set<String>> zrevrange(String str, long j, long j2);

    Response<Set<Tuple>> zrevrangeWithScores(String str, long j, long j2);

    Response<Long> zrevrank(String str, String str2);

    Response<Double> zscore(String str, String str2);

    Response<Long> zlexcount(String str, String str2, String str3);

    Response<Set<String>> zrangeByLex(String str, String str2, String str3);

    Response<Set<String>> zrangeByLex(String str, String str2, String str3, int i, int i2);

    Response<Set<String>> zrevrangeByLex(String str, String str2, String str3);

    Response<Set<String>> zrevrangeByLex(String str, String str2, String str3, int i, int i2);

    Response<Long> zremrangeByLex(String str, String str2, String str3);

    Response<Long> bitcount(String str);

    Response<Long> bitcount(String str, long j, long j2);

    Response<Long> pfadd(String str, String... strArr);

    Response<Long> pfcount(String str);

    Response<List<Long>> bitfield(String str, String... strArr);

    Response<Long> geoadd(String str, double d, double d2, String str2);

    Response<Long> geoadd(String str, Map<String, GeoCoordinate> map);

    Response<Double> geodist(String str, String str2, String str3);

    Response<Double> geodist(String str, String str2, String str3, GeoUnit geoUnit);

    Response<List<String>> geohash(String str, String... strArr);

    Response<List<GeoCoordinate>> geopos(String str, String... strArr);

    Response<List<GeoRadiusResponse>> georadius(String str, double d, double d2, double d3, GeoUnit geoUnit);

    Response<List<GeoRadiusResponse>> georadius(String str, double d, double d2, double d3, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    Response<List<GeoRadiusResponse>> georadiusByMember(String str, String str2, double d, GeoUnit geoUnit);

    Response<List<GeoRadiusResponse>> georadiusByMember(String str, String str2, double d, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);
}
