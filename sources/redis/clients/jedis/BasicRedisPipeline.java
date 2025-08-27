package redis.clients.jedis;

import java.util.List;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BasicRedisPipeline.class */
public interface BasicRedisPipeline {
    Response<String> bgrewriteaof();

    Response<String> bgsave();

    Response<List<String>> configGet(String str);

    Response<String> configSet(String str, String str2);

    Response<String> configResetStat();

    Response<String> save();

    Response<Long> lastsave();

    Response<String> flushDB();

    Response<String> flushAll();

    Response<String> info();

    Response<List<String>> time();

    Response<Long> dbSize();

    Response<String> shutdown();

    Response<String> ping();

    Response<String> select(int i);
}
