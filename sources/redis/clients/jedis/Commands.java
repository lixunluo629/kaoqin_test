package redis.clients.jedis;

import java.util.Map;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/Commands.class */
public interface Commands {
    void set(String str, String str2);

    void set(String str, String str2, String str3, String str4, long j);

    void get(String str);

    void exists(String str);

    void exists(String... strArr);

    void del(String... strArr);

    void type(String str);

    void keys(String str);

    void rename(String str, String str2);

    void renamenx(String str, String str2);

    void expire(String str, int i);

    void expireAt(String str, long j);

    void ttl(String str);

    void pttl(String str);

    void setbit(String str, long j, boolean z);

    void setbit(String str, long j, String str2);

    void getbit(String str, long j);

    void setrange(String str, long j, String str2);

    void getrange(String str, long j, long j2);

    void move(String str, int i);

    void getSet(String str, String str2);

    void mget(String... strArr);

    void setnx(String str, String str2);

    void setex(String str, int i, String str2);

    void mset(String... strArr);

    void msetnx(String... strArr);

    void decrBy(String str, long j);

    void decr(String str);

    void incrBy(String str, long j);

    void incrByFloat(String str, double d);

    void incr(String str);

    void append(String str, String str2);

    void substr(String str, int i, int i2);

    void hset(String str, String str2, String str3);

    void hget(String str, String str2);

    void hsetnx(String str, String str2, String str3);

    void hmset(String str, Map<String, String> map);

    void hmget(String str, String... strArr);

    void hincrBy(String str, String str2, long j);

    void hincrByFloat(String str, String str2, double d);

    void hexists(String str, String str2);

    void hdel(String str, String... strArr);

    void hlen(String str);

    void hkeys(String str);

    void hvals(String str);

    void hgetAll(String str);

    void rpush(String str, String... strArr);

    void lpush(String str, String... strArr);

    void llen(String str);

    void lrange(String str, long j, long j2);

    void ltrim(String str, long j, long j2);

    void lindex(String str, long j);

    void lset(String str, long j, String str2);

    void lrem(String str, long j, String str2);

    void lpop(String str);

    void rpop(String str);

    void rpoplpush(String str, String str2);

    void sadd(String str, String... strArr);

    void smembers(String str);

    void srem(String str, String... strArr);

    void spop(String str);

    void spop(String str, long j);

    void smove(String str, String str2, String str3);

    void scard(String str);

    void sismember(String str, String str2);

    void sinter(String... strArr);

    void sinterstore(String str, String... strArr);

    void sunion(String... strArr);

    void sunionstore(String str, String... strArr);

    void sdiff(String... strArr);

    void sdiffstore(String str, String... strArr);

    void srandmember(String str);

    void zadd(String str, double d, String str2);

    void zadd(String str, double d, String str2, ZAddParams zAddParams);

    void zadd(String str, Map<String, Double> map);

    void zadd(String str, Map<String, Double> map, ZAddParams zAddParams);

    void zrange(String str, long j, long j2);

    void zrem(String str, String... strArr);

    void zincrby(String str, double d, String str2);

    void zincrby(String str, double d, String str2, ZIncrByParams zIncrByParams);

    void zrank(String str, String str2);

    void zrevrank(String str, String str2);

    void zrevrange(String str, long j, long j2);

    void zrangeWithScores(String str, long j, long j2);

    void zrevrangeWithScores(String str, long j, long j2);

    void zcard(String str);

    void zscore(String str, String str2);

    void watch(String... strArr);

    void sort(String str);

    void sort(String str, SortingParams sortingParams);

    void blpop(String[] strArr);

    void sort(String str, SortingParams sortingParams, String str2);

    void sort(String str, String str2);

    void brpop(String[] strArr);

    void brpoplpush(String str, String str2, int i);

    void zcount(String str, double d, double d2);

    void zcount(String str, String str2, String str3);

    void zrangeByScore(String str, double d, double d2);

    void zrangeByScore(String str, String str2, String str3);

    void zrangeByScore(String str, double d, double d2, int i, int i2);

    void zrangeByScore(String str, String str2, String str3, int i, int i2);

    void zrangeByScoreWithScores(String str, double d, double d2);

    void zrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    void zrangeByScoreWithScores(String str, String str2, String str3);

    void zrangeByScoreWithScores(String str, String str2, String str3, int i, int i2);

    void zrevrangeByScore(String str, double d, double d2);

    void zrevrangeByScore(String str, String str2, String str3);

    void zrevrangeByScore(String str, double d, double d2, int i, int i2);

    void zrevrangeByScore(String str, String str2, String str3, int i, int i2);

    void zrevrangeByScoreWithScores(String str, double d, double d2);

    void zrevrangeByScoreWithScores(String str, double d, double d2, int i, int i2);

    void zrevrangeByScoreWithScores(String str, String str2, String str3);

    void zrevrangeByScoreWithScores(String str, String str2, String str3, int i, int i2);

    void zremrangeByRank(String str, long j, long j2);

    void zremrangeByScore(String str, double d, double d2);

    void zremrangeByScore(String str, String str2, String str3);

    void zunionstore(String str, String... strArr);

    void zunionstore(String str, ZParams zParams, String... strArr);

    void zinterstore(String str, String... strArr);

    void zinterstore(String str, ZParams zParams, String... strArr);

    void strlen(String str);

    void lpushx(String str, String... strArr);

    void persist(String str);

    void rpushx(String str, String... strArr);

    void echo(String str);

    void linsert(String str, BinaryClient.LIST_POSITION list_position, String str2, String str3);

    void bgrewriteaof();

    void bgsave();

    void lastsave();

    void save();

    void configSet(String str, String str2);

    void configGet(String str);

    void configResetStat();

    void multi();

    void exec();

    void discard();

    void objectRefcount(String str);

    void objectIdletime(String str);

    void objectEncoding(String str);

    void bitcount(String str);

    void bitcount(String str, long j, long j2);

    void bitop(BitOP bitOP, String str, String... strArr);

    @Deprecated
    void scan(int i, ScanParams scanParams);

    @Deprecated
    void hscan(String str, int i, ScanParams scanParams);

    @Deprecated
    void sscan(String str, int i, ScanParams scanParams);

    @Deprecated
    void zscan(String str, int i, ScanParams scanParams);

    void scan(String str, ScanParams scanParams);

    void hscan(String str, String str2, ScanParams scanParams);

    void sscan(String str, String str2, ScanParams scanParams);

    void zscan(String str, String str2, ScanParams scanParams);

    void waitReplicas(int i, long j);

    void bitfield(String str, String... strArr);
}
