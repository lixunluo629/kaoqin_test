package redis.clients.jedis;

import java.util.List;
import java.util.Set;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/MultiKeyCommands.class */
public interface MultiKeyCommands {
    Long del(String... strArr);

    Long exists(String... strArr);

    List<String> blpop(int i, String... strArr);

    List<String> brpop(int i, String... strArr);

    List<String> blpop(String... strArr);

    List<String> brpop(String... strArr);

    Set<String> keys(String str);

    List<String> mget(String... strArr);

    String mset(String... strArr);

    Long msetnx(String... strArr);

    String rename(String str, String str2);

    Long renamenx(String str, String str2);

    String rpoplpush(String str, String str2);

    Set<String> sdiff(String... strArr);

    Long sdiffstore(String str, String... strArr);

    Set<String> sinter(String... strArr);

    Long sinterstore(String str, String... strArr);

    Long smove(String str, String str2, String str3);

    Long sort(String str, SortingParams sortingParams, String str2);

    Long sort(String str, String str2);

    Set<String> sunion(String... strArr);

    Long sunionstore(String str, String... strArr);

    String watch(String... strArr);

    String unwatch();

    Long zinterstore(String str, String... strArr);

    Long zinterstore(String str, ZParams zParams, String... strArr);

    Long zunionstore(String str, String... strArr);

    Long zunionstore(String str, ZParams zParams, String... strArr);

    String brpoplpush(String str, String str2, int i);

    Long publish(String str, String str2);

    void subscribe(JedisPubSub jedisPubSub, String... strArr);

    void psubscribe(JedisPubSub jedisPubSub, String... strArr);

    String randomKey();

    Long bitop(BitOP bitOP, String str, String... strArr);

    @Deprecated
    ScanResult<String> scan(int i);

    ScanResult<String> scan(String str);

    ScanResult<String> scan(String str, ScanParams scanParams);

    String pfmerge(String str, String... strArr);

    long pfcount(String... strArr);
}
