package redis.clients.jedis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BinaryJedisClusterCommands.class */
public interface BinaryJedisClusterCommands {
    String set(byte[] bArr, byte[] bArr2);

    String set(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, long j);

    byte[] get(byte[] bArr);

    Boolean exists(byte[] bArr);

    Long persist(byte[] bArr);

    String type(byte[] bArr);

    Long expire(byte[] bArr, int i);

    Long pexpire(byte[] bArr, long j);

    Long expireAt(byte[] bArr, long j);

    Long pexpireAt(byte[] bArr, long j);

    Long ttl(byte[] bArr);

    Long pttl(byte[] bArr);

    Boolean setbit(byte[] bArr, long j, boolean z);

    Boolean setbit(byte[] bArr, long j, byte[] bArr2);

    Boolean getbit(byte[] bArr, long j);

    Long setrange(byte[] bArr, long j, byte[] bArr2);

    byte[] getrange(byte[] bArr, long j, long j2);

    byte[] getSet(byte[] bArr, byte[] bArr2);

    Long setnx(byte[] bArr, byte[] bArr2);

    String setex(byte[] bArr, int i, byte[] bArr2);

    Long decrBy(byte[] bArr, long j);

    Long decr(byte[] bArr);

    Long incrBy(byte[] bArr, long j);

    Double incrByFloat(byte[] bArr, double d);

    Long incr(byte[] bArr);

    Long append(byte[] bArr, byte[] bArr2);

    byte[] substr(byte[] bArr, int i, int i2);

    Long hset(byte[] bArr, byte[] bArr2, byte[] bArr3);

    byte[] hget(byte[] bArr, byte[] bArr2);

    Long hsetnx(byte[] bArr, byte[] bArr2, byte[] bArr3);

    String hmset(byte[] bArr, Map<byte[], byte[]> map);

    List<byte[]> hmget(byte[] bArr, byte[]... bArr2);

    Long hincrBy(byte[] bArr, byte[] bArr2, long j);

    Double hincrByFloat(byte[] bArr, byte[] bArr2, double d);

    Boolean hexists(byte[] bArr, byte[] bArr2);

    Long hdel(byte[] bArr, byte[]... bArr2);

    Long hlen(byte[] bArr);

    Set<byte[]> hkeys(byte[] bArr);

    Collection<byte[]> hvals(byte[] bArr);

    Map<byte[], byte[]> hgetAll(byte[] bArr);

    Long rpush(byte[] bArr, byte[]... bArr2);

    Long lpush(byte[] bArr, byte[]... bArr2);

    Long llen(byte[] bArr);

    List<byte[]> lrange(byte[] bArr, long j, long j2);

    String ltrim(byte[] bArr, long j, long j2);

    byte[] lindex(byte[] bArr, long j);

    String lset(byte[] bArr, long j, byte[] bArr2);

    Long lrem(byte[] bArr, long j, byte[] bArr2);

    byte[] lpop(byte[] bArr);

    byte[] rpop(byte[] bArr);

    Long sadd(byte[] bArr, byte[]... bArr2);

    Set<byte[]> smembers(byte[] bArr);

    Long srem(byte[] bArr, byte[]... bArr2);

    byte[] spop(byte[] bArr);

    Set<byte[]> spop(byte[] bArr, long j);

    Long scard(byte[] bArr);

    Boolean sismember(byte[] bArr, byte[] bArr2);

    byte[] srandmember(byte[] bArr);

    List<byte[]> srandmember(byte[] bArr, int i);

    Long strlen(byte[] bArr);

    Long zadd(byte[] bArr, double d, byte[] bArr2);

    Long zadd(byte[] bArr, Map<byte[], Double> map);

    Long zadd(byte[] bArr, double d, byte[] bArr2, ZAddParams zAddParams);

    Long zadd(byte[] bArr, Map<byte[], Double> map, ZAddParams zAddParams);

    Set<byte[]> zrange(byte[] bArr, long j, long j2);

    Long zrem(byte[] bArr, byte[]... bArr2);

    Double zincrby(byte[] bArr, double d, byte[] bArr2);

    Double zincrby(byte[] bArr, double d, byte[] bArr2, ZIncrByParams zIncrByParams);

    Long zrank(byte[] bArr, byte[] bArr2);

    Long zrevrank(byte[] bArr, byte[] bArr2);

    Set<byte[]> zrevrange(byte[] bArr, long j, long j2);

    Set<Tuple> zrangeWithScores(byte[] bArr, long j, long j2);

    Set<Tuple> zrevrangeWithScores(byte[] bArr, long j, long j2);

    Long zcard(byte[] bArr);

    Double zscore(byte[] bArr, byte[] bArr2);

    List<byte[]> sort(byte[] bArr);

    List<byte[]> sort(byte[] bArr, SortingParams sortingParams);

    Long zcount(byte[] bArr, double d, double d2);

    Long zcount(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrangeByScore(byte[] bArr, double d, double d2);

    Set<byte[]> zrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrevrangeByScore(byte[] bArr, double d, double d2);

    Set<byte[]> zrangeByScore(byte[] bArr, double d, double d2, int i, int i2);

    Set<byte[]> zrevrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Set<byte[]> zrevrangeByScore(byte[] bArr, double d, double d2, int i, int i2);

    Set<Tuple> zrangeByScoreWithScores(byte[] bArr, double d, double d2);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] bArr, double d, double d2);

    Set<Tuple> zrangeByScoreWithScores(byte[] bArr, double d, double d2, int i, int i2);

    Set<byte[]> zrevrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Set<Tuple> zrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<Tuple> zrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] bArr, double d, double d2, int i, int i2);

    Set<Tuple> zrevrangeByScoreWithScores(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Long zremrangeByRank(byte[] bArr, long j, long j2);

    Long zremrangeByScore(byte[] bArr, double d, double d2);

    Long zremrangeByScore(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Long zlexcount(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Set<byte[]> zrevrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Set<byte[]> zrevrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3, int i, int i2);

    Long zremrangeByLex(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Long linsert(byte[] bArr, BinaryClient.LIST_POSITION list_position, byte[] bArr2, byte[] bArr3);

    Long lpushx(byte[] bArr, byte[]... bArr2);

    Long rpushx(byte[] bArr, byte[]... bArr2);

    Long del(byte[] bArr);

    byte[] echo(byte[] bArr);

    Long bitcount(byte[] bArr);

    Long bitcount(byte[] bArr, long j, long j2);

    Long pfadd(byte[] bArr, byte[]... bArr2);

    long pfcount(byte[] bArr);

    Long geoadd(byte[] bArr, double d, double d2, byte[] bArr2);

    Long geoadd(byte[] bArr, Map<byte[], GeoCoordinate> map);

    Double geodist(byte[] bArr, byte[] bArr2, byte[] bArr3);

    Double geodist(byte[] bArr, byte[] bArr2, byte[] bArr3, GeoUnit geoUnit);

    List<byte[]> geohash(byte[] bArr, byte[]... bArr2);

    List<GeoCoordinate> geopos(byte[] bArr, byte[]... bArr2);

    List<GeoRadiusResponse> georadius(byte[] bArr, double d, double d2, double d3, GeoUnit geoUnit);

    List<GeoRadiusResponse> georadius(byte[] bArr, double d, double d2, double d3, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    List<GeoRadiusResponse> georadiusByMember(byte[] bArr, byte[] bArr2, double d, GeoUnit geoUnit);

    List<GeoRadiusResponse> georadiusByMember(byte[] bArr, byte[] bArr2, double d, GeoUnit geoUnit, GeoRadiusParam geoRadiusParam);

    ScanResult<byte[]> scan(byte[] bArr, ScanParams scanParams);

    ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] bArr, byte[] bArr2);

    ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] bArr, byte[] bArr2, ScanParams scanParams);

    ScanResult<byte[]> sscan(byte[] bArr, byte[] bArr2);

    ScanResult<byte[]> sscan(byte[] bArr, byte[] bArr2, ScanParams scanParams);

    ScanResult<Tuple> zscan(byte[] bArr, byte[] bArr2);

    ScanResult<Tuple> zscan(byte[] bArr, byte[] bArr2, ScanParams scanParams);

    List<Long> bitfield(byte[] bArr, byte[]... bArr2);
}
