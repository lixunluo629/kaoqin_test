package org.springframework.data.redis.core;

import com.alibaba.excel.constant.ExcelXmlConstants;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/RedisCommand.class */
public enum RedisCommand {
    APPEND("rw", 2, 2),
    AUTH("rw", 1, 1),
    BGREWRITEAOF(ExcelXmlConstants.POSITION, 0, 0, "bgwriteaof"),
    BGSAVE(ExcelXmlConstants.POSITION, 0, 0),
    BITCOUNT(ExcelXmlConstants.POSITION, 1, 3),
    BITOP("rw", 3),
    BITPOS(ExcelXmlConstants.POSITION, 2, 4),
    BLPOP("rw", 2),
    BRPOP("rw", 2),
    BRPOPLPUSH("rw", 3),
    CLIENT_KILL("rw", 1, 1),
    CLIENT_LIST(ExcelXmlConstants.POSITION, 0, 0),
    CLIENT_GETNAME(ExcelXmlConstants.POSITION, 0, 0),
    CLIENT_PAUSE("rw", 1, 1),
    CLIENT_SETNAME("w", 1, 1),
    CONFIG_GET(ExcelXmlConstants.POSITION, 1, 1, "getconfig"),
    CONFIG_REWRITE("rw", 0, 0),
    CONFIG_SET("w", 2, 2, "setconfig"),
    CONFIG_RESETSTAT("w", 0, 0, "resetconfigstats"),
    DBSIZE(ExcelXmlConstants.POSITION, 0, 0),
    DECR("w", 1, 1),
    DECRBY("w", 2, 2),
    DEL("rw", 1),
    DISCARD("rw", 0, 0),
    DUMP(ExcelXmlConstants.POSITION, 1, 1),
    ECHO(ExcelXmlConstants.POSITION, 1, 1),
    EVAL("rw", 2),
    EVALSHA("rw", 2),
    EXEC("rw", 0, 0),
    EXISTS(ExcelXmlConstants.POSITION, 1, 1),
    EXPIRE("rw", 2, 2),
    EXPIREAT("rw", 2, 2),
    FLUSHALL("w", 0, 0),
    FLUSHDB("w", 0, 0),
    GET(ExcelXmlConstants.POSITION, 1, 1),
    GETBIT(ExcelXmlConstants.POSITION, 2, 2),
    GETRANGE(ExcelXmlConstants.POSITION, 3, 3),
    GETSET("rw", 2, 2),
    GEOADD("w", 3),
    GEODIST(ExcelXmlConstants.POSITION, 2),
    GEOHASH(ExcelXmlConstants.POSITION, 2),
    GEOPOS(ExcelXmlConstants.POSITION, 2),
    GEORADIUS(ExcelXmlConstants.POSITION, 4),
    GEORADIUSBYMEMBER(ExcelXmlConstants.POSITION, 3),
    HDEL("rw", 2),
    HEXISTS(ExcelXmlConstants.POSITION, 2, 2),
    HGET(ExcelXmlConstants.POSITION, 2, 2),
    HGETALL(ExcelXmlConstants.POSITION, 1, 1),
    HINCRBY("rw", 3, 3),
    HINCBYFLOAT("rw", 3, 3),
    HKEYS(ExcelXmlConstants.POSITION, 1),
    HLEN(ExcelXmlConstants.POSITION, 1),
    HMGET(ExcelXmlConstants.POSITION, 2),
    HMSET("w", 3),
    HSET("w", 3, 3),
    HSETNX("w", 3, 3),
    HVALS(ExcelXmlConstants.POSITION, 1, 1),
    INCR("rw", 1),
    INCRBYFLOAT("rw", 2, 2),
    INFO(ExcelXmlConstants.POSITION, 0),
    KEYS(ExcelXmlConstants.POSITION, 1),
    LASTSAVE(ExcelXmlConstants.POSITION, 0),
    LINDEX(ExcelXmlConstants.POSITION, 2, 2),
    LINSERT("rw", 4, 4),
    LLEN(ExcelXmlConstants.POSITION, 1, 1),
    LPOP("rw", 1, 1),
    LPUSH("rw", 2),
    LPUSHX("rw", 2),
    LRANGE(ExcelXmlConstants.POSITION, 3, 3),
    LREM("rw", 3, 3),
    LSET("w", 3, 3),
    LTRIM("w", 3, 3),
    MGET(ExcelXmlConstants.POSITION, 1),
    MIGRATE("rw", 0),
    MONITOR("rw", 0, 0),
    MOVE("rw", 2, 2),
    MSET("w", 2),
    MSETNX("w", 2),
    MULTI("rw", 0, 0),
    PERSIST("rw", 1, 1),
    PEXPIRE("rw", 2, 2),
    PEXPIREAT("rw", 2, 2),
    PING(ExcelXmlConstants.POSITION, 0, 0),
    PSETEX("w", 3),
    PSUBSCRIBE(ExcelXmlConstants.POSITION, 1),
    PTTL(ExcelXmlConstants.POSITION, 1, 1),
    QUIT("rw", 0, 0),
    RANDOMKEY(ExcelXmlConstants.POSITION, 0, 0),
    RANAME("w", 2, 2),
    RENAMENX("w", 2, 2),
    RESTORE("w", 3, 3),
    RPOP("rw", 1, 1),
    RPOPLPUSH("rw", 2, 2),
    RPUSH("rw", 2),
    RPUSHX("rw", 2, 2),
    SADD("rw", 2),
    SAVE("rw", 0, 0),
    SCARD(ExcelXmlConstants.POSITION, 1, 1),
    SCRIPT_EXISTS(ExcelXmlConstants.POSITION, 1),
    SCRIPT_FLUSH("rw", 0, 0),
    SCRIPT_KILL("rw", 0, 0),
    SCRIPT_LOAD("rw", 1, 1),
    SDIFF(ExcelXmlConstants.POSITION, 1),
    SDIFFSTORE("rw", 2),
    SELECT("rw", 0, 0),
    SET("w", 2),
    SETBIT("rw", 3, 3),
    SETEX("w", 3, 3),
    SETNX("w", 2, 2),
    SETRANGE("rw", 3, 3),
    SHUTDOWN("rw", 0),
    SINTER(ExcelXmlConstants.POSITION, 1),
    SINTERSTORE("rw", 2),
    SISMEMBER(ExcelXmlConstants.POSITION, 2),
    SLAVEOF("w", 2),
    SLOWLOG("rw", 1),
    SMEMBERS(ExcelXmlConstants.POSITION, 1, 1),
    SMOVE("rw", 3, 3),
    SORT("rw", 1),
    SPOP("rw", 1, 1),
    SRANDMEMBER(ExcelXmlConstants.POSITION, 1, 1),
    SREM("rw", 2),
    STRLEN(ExcelXmlConstants.POSITION, 1, 1),
    SUBSCRIBE("rw", 1),
    SUNION(ExcelXmlConstants.POSITION, 1),
    SUNIONSTORE("rw ", 2),
    SYNC("rw", 0, 0),
    TIME(ExcelXmlConstants.POSITION, 0, 0),
    TTL(ExcelXmlConstants.POSITION, 1, 1),
    TYPE(ExcelXmlConstants.POSITION, 1, 1),
    UNSUBSCRIBE("rw", 0),
    UNWATCH("rw", 0, 0),
    WATCH("rw", 1),
    ZADD("rw", 3),
    ZCARD(ExcelXmlConstants.POSITION, 1),
    ZCOUNT(ExcelXmlConstants.POSITION, 3, 3),
    ZINCRBY("rw", 3),
    ZINTERSTORE("rw", 3),
    ZRANGE(ExcelXmlConstants.POSITION, 3),
    ZRANGEBYSCORE(ExcelXmlConstants.POSITION, 3),
    ZRANK(ExcelXmlConstants.POSITION, 2, 2),
    ZREM("rw", 2),
    ZREMRANGEBYRANK("rw", 3, 3),
    ZREMRANGEBYSCORE("rm", 3, 3),
    ZREVRANGE(ExcelXmlConstants.POSITION, 3),
    ZREVRANGEBYSCORE(ExcelXmlConstants.POSITION, 3),
    ZREVRANK(ExcelXmlConstants.POSITION, 2, 2),
    ZSCORE(ExcelXmlConstants.POSITION, 2, 2),
    ZUNIONSTORE("rw", 3),
    SCAN(ExcelXmlConstants.POSITION, 1),
    SSCAN(ExcelXmlConstants.POSITION, 2),
    HSCAN(ExcelXmlConstants.POSITION, 2),
    ZSCAN(ExcelXmlConstants.POSITION, 2),
    UNKNOWN("rw", -1);

    private boolean read;
    private boolean write;
    private Set<String> alias;
    private int minArgs;
    private int maxArgs;
    private static final Map<String, RedisCommand> commandLookup = buildCommandLookupTable();

    private static Map<String, RedisCommand> buildCommandLookupTable() {
        RedisCommand[] cmds = values();
        Map<String, RedisCommand> map = new HashMap<>(cmds.length, 1.0f);
        for (RedisCommand cmd : cmds) {
            map.put(cmd.name().toLowerCase(), cmd);
            for (String alias : cmd.alias) {
                map.put(alias, cmd);
            }
        }
        return Collections.unmodifiableMap(map);
    }

    RedisCommand(String mode, int minArgs) {
        this(mode, minArgs, -1);
    }

    RedisCommand(String mode, int minArgs, int maxArgs) {
        this.read = true;
        this.write = true;
        this.alias = new HashSet(1);
        this.minArgs = -1;
        this.maxArgs = -1;
        if (StringUtils.hasText(mode)) {
            this.read = mode.toLowerCase().indexOf(114) > -1;
            this.write = mode.toLowerCase().indexOf(119) > -1;
        }
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
    }

    RedisCommand(String mode, int minArgs, int maxArgs, String... alias) {
        this(mode, minArgs, maxArgs);
        if (alias != null && alias.length > 0) {
            this.alias.addAll(Arrays.asList(alias));
        }
    }

    public boolean requiresArguments() {
        return this.minArgs >= 0;
    }

    public boolean requiresExactNumberOfArguments() {
        return this.maxArgs == 0 || this.minArgs == this.maxArgs;
    }

    public boolean isRead() {
        return this.read;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isReadonly() {
        return this.read && !this.write;
    }

    public boolean isRepresentedBy(String command) {
        if (!StringUtils.hasText(command)) {
            return false;
        }
        if (toString().equalsIgnoreCase(command)) {
            return true;
        }
        return this.alias.contains(command.toLowerCase());
    }

    public void validateArgumentCount(int nrArguments) {
        if (requiresArguments()) {
            if (requiresExactNumberOfArguments() && nrArguments != this.maxArgs) {
                throw new IllegalArgumentException(String.format("%s command requires %s arguments.", name(), Integer.valueOf(this.maxArgs)));
            }
            if (nrArguments < this.minArgs) {
                throw new IllegalArgumentException(String.format("%s command requires at least %s arguments.", name(), Integer.valueOf(this.minArgs)));
            }
            if (this.maxArgs > 0 && nrArguments > this.maxArgs) {
                throw new IllegalArgumentException(String.format("%s command requires at most %s arguments.", name(), Integer.valueOf(this.maxArgs)));
            }
        }
    }

    public static RedisCommand failsafeCommandLookup(String key) {
        if (!StringUtils.hasText(key)) {
            return UNKNOWN;
        }
        RedisCommand cmd = commandLookup.get(key.toLowerCase());
        if (cmd != null) {
            return cmd;
        }
        return UNKNOWN;
    }
}
