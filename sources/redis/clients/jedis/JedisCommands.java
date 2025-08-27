package redis.clients.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/JedisCommands.class */
public interface JedisCommands {
    String set(String str, String str2);

    String set(String str, String str2, String str3, String str4, long j);

    String set(String str, String str2, String str3);

    String get(String str);

    Boolean exists(String str);

    Long persist(String str);

    String type(String str);

    Long expire(String str, int i);

    Long pexpire(String str, long j);

    Long expireAt(String str, long j);

    Long pexpireAt(String str, long j);

    Long ttl(String str);

    Long pttl(String str);

    Boolean setbit(String str, long j, boolean z);

    Boolean setbit(String str, long j, String str2);

    Boolean getbit(String str, long j);

    Long setrange(String str, long j, String str2);

    String getrange(String str, long j, long j2);

    String getSet(String str, String str2);

    Long setnx(String str, String str2);

    String setex(String str, int i, String str2);

    String psetex(String str, long j, String str2);

    Long decrBy(String str, long j);

    Long decr(String str);

    Long incrBy(String str, long j);

    Double incrByFloat(String str, double d);

    Long incr(String str);

    Long append(String str, String str2);

    String substr(String str, int i, int i2);

    Long hset(String str, String str2, String str3);

    String hget(String str, String str2);

    Long hsetnx(String str, String str2, String str3);

    String hmset(String str, Map<String, String> map);

    List<String> hmget(String str, String... strArr);

    Long hincrBy(String str, String str2, long j);

    Double hincrByFloat(String str, String str2, double d);

    Boolean hexists(String str, String str2);

    Long hdel(String str, String... strArr);

    Long hlen(String str);

    Set<String> hkeys(String str);

    List<String> hvals(String str);

    Map<String, String> hgetAll(String str);

    Long rpush(String str, String... strArr);

    Long lpush(String str, String... strArr);

    Long llen(String str);

    List<String> lrange(String str, long j, long j2);

    String ltrim(String str, long j, long j2);

    String lindex(String str, long j);

    String lset(String str, long j, String str2);

    Long lrem(String str, long j, String str2);

    String lpop(String str);

    String rpop(String str);

    Long sadd(String str, String... strArr);

    Set<String> smembers(String str);

    Long srem(String str, String... strArr);

    String spop(String str);

    Set<String> spop(String str, long j);

    Long scard(String str);

    Boolean sismember(String str, String str2);

    String srandmember(String str);

    List<String> srandmember(String str, int i);

    Long strlen(String str);

    Long zadd(String str, double d, String str2);

    Long zadd(String str, double d, String str2, ZAddParams zAddParams);

    Long zadd(String str, Map<String, Double> map);

    Long zadd(String str, Map<String, Double> map, ZAddParams zAddParams);

    Set<String> zrange(String str, long j, long j2);

    Long zrem(String str, String... strArr);

    Double zincrby(String str, double d, String str2);

    Double zincrby(String str, double d, String str2, ZIncrByParams zIncrByParams);

    Long zrank(String str, String str2);

    Long zrevrank(String str, String str2);

    Set<String> zrevrange(String str, long j, long j2);

    Set<Tuple> zrangeWithScores(String str, long j, long j2);

    Set<Tuple> zrevrangeWithScores(String str, long j, long j2);

    Long zcard(String str);

    Double zscore(String str, String str2);

    List<String> sort(String str);

    List<String> sort(String str, SortingParams sortingParams);

    Long zcount(String str, double d, double d2);

    Long zcount(String str, String str2, String str3);

    Set<String> zrangeByScore(String str, double d, double d2);

    Set<String> zrangeByScore(String str, String str2, String str3);

    Set<String> zrevrangeByScore(String str, double d, double d2);

    Set<String> zrangeByScore(String str, double d, double d2, int i, int i2);

    Set<String> zrevrangeByScore(String str, String str2, String str3);

    Set<String> zrangeByScore(String str, String str2, String str3, int i, int i2);

    Set<String> zrevrangeByScore(String str, double d, double d2, int i, int i2);

    Set<Tuple> zrangeByScoreWithScores(String str, double d, double d2);

    Set<Tuple> zrevrangeByScoreWithScores(String str, double d, double d2);

    Set<Tuple> zrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    Set<String> zrevrangeByScore(String str, String str2, String str3, int i, int i2);

    Set<Tuple> zrangeByScoreWithScores(String str, String str2, String str3);

    Set<Tuple> zrevrangeByScoreWithScores(String str, String str2, String str3);

    Set<Tuple> zrangeByScoreWithScores(String str, String str2, String str3, int i, int i2);

    Set<Tuple> zrevrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    Set<Tuple> zrevrangeByScoreWithScores(String str, String str2, String str3, int i, int i2);

    Long zremrangeByRank(String str, long j, long j2);

    Long zremrangeByScore(String str, double d, double d2);

    Long zremrangeByScore(String str, String str2, String str3);

    Long zlexcount(String str, String str2, String str3);

    Set<String> zrangeByLex(String str, String str2, String str3);

    Set<String> zrangeByLex(String str, String str2, String str3, int i, int i2);

    Set<String> zrevrangeByLex(String str, String str2, String str3);

    Set<String> zrevrangeByLex(String str, String str2, String str3, int i, int i2);

    Long zremrangeByLex(String str, String str2, String str3);

    Long linsert(String str, BinaryClient.LIST_POSITION list_position, String str2, String str3);

    Long lpushx(String str, String... strArr);

    Long rpushx(String str, String... strArr);

    @Deprecated
    List<String> blpop(String str);

    List<String> blpop(int i, String str);

    @Deprecated
    List<String> brpop(String str);

    List<String> brpop(int i, String str);

    Long del(String str);

    String echo(String str);

    Long move(String str, int i);

    Long bitcount(String str);

    Long bitcount(String str, long j, long j2);

    Long bitpos(String str, boolean z);

    Long bitpos(String str, boolean z, BitPosParams bitPosParams);

    @Deprecated
    ScanResult<Map.Entry<String, String>> hscan(String str, int i);

    @Deprecated
    ScanResult<String> sscan(String str, int i);

    @Deprecated
    ScanResult<Tuple> zscan(String str, int i);

    ScanResult<Map.Entry<String, String>> hscan(String str, String str2);

    ScanResult<Map.Entry<String, String>> hscan(String str, String str2, ScanParams scanParams);

    ScanResult<String> sscan(String str, String str2);

    ScanResult<String> sscan(String str, String str2, ScanParams scanParams);

    ScanResult<Tuple> zscan(String str, String str2);

    ScanResult<Tuple> zscan(String str, String str2, ScanParams scanParams);

    Long pfadd(String str, String... strArr);

    long pfcount(String str);

    Long geoadd(String str, double d, double d2, String str2);

    Long geoadd(String str, Map<String, GeoCoordinate> map);

    Double geodist(String str, String str2, String str3);

    Double geodist(String str, String str2, String str3, GeoUnit geoUnit);

    List<String> geohash(String str, String... strArr);

    List<GeoCoordinate> geopos(String str, String... strArr);

    List<GeoRadiusResponse> georadius(String str, double d, double d2, double d3, GeoUnit geoUnit);

    List<GeoRadiusResponse> georadius(String str, double d, double d2, double d3, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    List<GeoRadiusResponse> georadiusByMember(String str, String str2, double d, GeoUnit geoUnit);

    List<GeoRadiusResponse> georadiusByMember(String str, String str2, double d, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    List<Long> bitfield(String str, String... strArr);
}
