package org.springframework.data.redis.connection;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/StringRedisConnection.class */
public interface StringRedisConnection extends RedisConnection {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/StringRedisConnection$StringTuple.class */
    public interface StringTuple extends RedisZSetCommands.Tuple {
        String getValueAsString();
    }

    Object execute(String str, String... strArr);

    Object execute(String str);

    Boolean exists(String str);

    Long del(String... strArr);

    DataType type(String str);

    Collection<String> keys(String str);

    void rename(String str, String str2);

    Boolean renameNX(String str, String str2);

    Boolean expire(String str, long j);

    Boolean pExpire(String str, long j);

    Boolean expireAt(String str, long j);

    Boolean pExpireAt(String str, long j);

    Boolean persist(String str);

    Boolean move(String str, int i);

    Long ttl(String str);

    Long ttl(String str, TimeUnit timeUnit);

    Long pTtl(String str);

    Long pTtl(String str, TimeUnit timeUnit);

    String echo(String str);

    List<String> sort(String str, SortParameters sortParameters);

    Long sort(String str, SortParameters sortParameters, String str2);

    String get(String str);

    String getSet(String str, String str2);

    List<String> mGet(String... strArr);

    void set(String str, String str2);

    void set(String str, String str2, Expiration expiration, RedisStringCommands.SetOption setOption);

    Boolean setNX(String str, String str2);

    void setEx(String str, long j, String str2);

    void pSetEx(String str, long j, String str2);

    void mSetString(Map<String, String> map);

    Boolean mSetNXString(Map<String, String> map);

    Long incr(String str);

    Long incrBy(String str, long j);

    Double incrBy(String str, double d);

    Long decr(String str);

    Long decrBy(String str, long j);

    Long append(String str, String str2);

    String getRange(String str, long j, long j2);

    void setRange(String str, String str2, long j);

    Boolean getBit(String str, long j);

    Boolean setBit(String str, long j, boolean z);

    Long bitCount(String str);

    Long bitCount(String str, long j, long j2);

    Long bitOp(RedisStringCommands.BitOperation bitOperation, String str, String... strArr);

    Long strLen(String str);

    Long rPush(String str, String... strArr);

    Long lPush(String str, String... strArr);

    Long rPushX(String str, String str2);

    Long lPushX(String str, String str2);

    Long lLen(String str);

    List<String> lRange(String str, long j, long j2);

    void lTrim(String str, long j, long j2);

    String lIndex(String str, long j);

    Long lInsert(String str, RedisListCommands.Position position, String str2, String str3);

    void lSet(String str, long j, String str2);

    Long lRem(String str, long j, String str2);

    String lPop(String str);

    String rPop(String str);

    List<String> bLPop(int i, String... strArr);

    List<String> bRPop(int i, String... strArr);

    String rPopLPush(String str, String str2);

    String bRPopLPush(int i, String str, String str2);

    Long sAdd(String str, String... strArr);

    Long sRem(String str, String... strArr);

    String sPop(String str);

    Boolean sMove(String str, String str2, String str3);

    Long sCard(String str);

    Boolean sIsMember(String str, String str2);

    Set<String> sInter(String... strArr);

    Long sInterStore(String str, String... strArr);

    Set<String> sUnion(String... strArr);

    Long sUnionStore(String str, String... strArr);

    Set<String> sDiff(String... strArr);

    Long sDiffStore(String str, String... strArr);

    Set<String> sMembers(String str);

    String sRandMember(String str);

    List<String> sRandMember(String str, long j);

    Cursor<String> sScan(String str, ScanOptions scanOptions);

    Boolean zAdd(String str, double d, String str2);

    Long zAdd(String str, Set<StringTuple> set);

    Long zRem(String str, String... strArr);

    Double zIncrBy(String str, double d, String str2);

    Long zRank(String str, String str2);

    Long zRevRank(String str, String str2);

    Set<String> zRange(String str, long j, long j2);

    Set<StringTuple> zRangeWithScores(String str, long j, long j2);

    Set<String> zRangeByScore(String str, double d, double d2);

    Set<StringTuple> zRangeByScoreWithScores(String str, double d, double d2);

    Set<String> zRangeByScore(String str, double d, double d2, long j, long j2);

    Set<StringTuple> zRangeByScoreWithScores(String str, double d, double d2, long j, long j2);

    Set<String> zRevRange(String str, long j, long j2);

    Set<StringTuple> zRevRangeWithScores(String str, long j, long j2);

    Set<String> zRevRangeByScore(String str, double d, double d2);

    Set<StringTuple> zRevRangeByScoreWithScores(String str, double d, double d2);

    Set<String> zRevRangeByScore(String str, double d, double d2, long j, long j2);

    Set<StringTuple> zRevRangeByScoreWithScores(String str, double d, double d2, long j, long j2);

    Long zCount(String str, double d, double d2);

    Long zCard(String str);

    Double zScore(String str, String str2);

    Long zRemRange(String str, long j, long j2);

    Long zRemRangeByScore(String str, double d, double d2);

    Long zUnionStore(String str, String... strArr);

    Long zUnionStore(String str, RedisZSetCommands.Aggregate aggregate, int[] iArr, String... strArr);

    Long zInterStore(String str, String... strArr);

    Long zInterStore(String str, RedisZSetCommands.Aggregate aggregate, int[] iArr, String... strArr);

    Cursor<StringTuple> zScan(String str, ScanOptions scanOptions);

    Set<byte[]> zRangeByScore(String str, String str2, String str3);

    Set<byte[]> zRangeByScore(String str, String str2, String str3, long j, long j2);

    Set<String> zRangeByLex(String str);

    Set<String> zRangeByLex(String str, RedisZSetCommands.Range range);

    Set<String> zRangeByLex(String str, RedisZSetCommands.Range range, RedisZSetCommands.Limit limit);

    Boolean hSet(String str, String str2, String str3);

    Boolean hSetNX(String str, String str2, String str3);

    String hGet(String str, String str2);

    List<String> hMGet(String str, String... strArr);

    void hMSet(String str, Map<String, String> map);

    Long hIncrBy(String str, String str2, long j);

    Double hIncrBy(String str, String str2, double d);

    Boolean hExists(String str, String str2);

    Long hDel(String str, String... strArr);

    Long hLen(String str);

    Set<String> hKeys(String str);

    List<String> hVals(String str);

    Map<String, String> hGetAll(String str);

    Cursor<Map.Entry<String, String>> hScan(String str, ScanOptions scanOptions);

    Long pfAdd(String str, String... strArr);

    Long pfCount(String... strArr);

    void pfMerge(String str, String... strArr);

    Long geoAdd(String str, Point point, String str2);

    Long geoAdd(String str, RedisGeoCommands.GeoLocation<String> geoLocation);

    Long geoAdd(String str, Map<String, Point> map);

    Long geoAdd(String str, Iterable<RedisGeoCommands.GeoLocation<String>> iterable);

    Distance geoDist(String str, String str2, String str3);

    Distance geoDist(String str, String str2, String str3, Metric metric);

    List<String> geoHash(String str, String... strArr);

    List<Point> geoPos(String str, String... strArr);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String str, Circle circle);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String str, Circle circle, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String str, String str2, double d);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String str, String str2, Distance distance);

    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadiusByMember(String str, String str2, Distance distance, RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs);

    Long geoRemove(String str, String... strArr);

    Long publish(String str, String str2);

    void subscribe(MessageListener messageListener, String... strArr);

    void pSubscribe(MessageListener messageListener, String... strArr);

    String scriptLoad(String str);

    <T> T eval(String str, ReturnType returnType, int i, String... strArr);

    <T> T evalSha(String str, ReturnType returnType, int i, String... strArr);

    void setClientName(String str);

    List<RedisClientInfo> getClientList();
}
