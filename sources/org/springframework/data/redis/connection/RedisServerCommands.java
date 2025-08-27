package org.springframework.data.redis.connection;

import java.util.List;
import java.util.Properties;
import org.springframework.data.redis.core.types.RedisClientInfo;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisServerCommands.class */
public interface RedisServerCommands {

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisServerCommands$MigrateOption.class */
    public enum MigrateOption {
        COPY,
        REPLACE
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisServerCommands$ShutdownOption.class */
    public enum ShutdownOption {
        SAVE,
        NOSAVE
    }

    @Deprecated
    void bgWriteAof();

    void bgReWriteAof();

    void bgSave();

    Long lastSave();

    void save();

    Long dbSize();

    void flushDb();

    void flushAll();

    Properties info();

    Properties info(String str);

    void shutdown();

    void shutdown(ShutdownOption shutdownOption);

    List<String> getConfig(String str);

    void setConfig(String str, String str2);

    void resetConfigStats();

    Long time();

    void killClient(String str, int i);

    void setClientName(byte[] bArr);

    String getClientName();

    List<RedisClientInfo> getClientList();

    void slaveOf(String str, int i);

    void slaveOfNoOne();

    void migrate(byte[] bArr, RedisNode redisNode, int i, MigrateOption migrateOption);

    void migrate(byte[] bArr, RedisNode redisNode, int i, MigrateOption migrateOption, long j);
}
