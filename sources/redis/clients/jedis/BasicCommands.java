package redis.clients.jedis;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BasicCommands.class */
public interface BasicCommands {
    String ping();

    String quit();

    String flushDB();

    Long dbSize();

    String select(int i);

    String flushAll();

    String auth(String str);

    String save();

    String bgsave();

    String bgrewriteaof();

    Long lastsave();

    String shutdown();

    String info();

    String info(String str);

    String slaveof(String str, int i);

    String slaveofNoOne();

    Long getDB();

    String debug(DebugParams debugParams);

    String configResetStat();

    Long waitReplicas(int i, long j);
}
