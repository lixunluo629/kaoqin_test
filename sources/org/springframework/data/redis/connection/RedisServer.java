package org.springframework.data.redis.connection;

import java.util.Properties;
import org.hyperic.sigar.NetFlags;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisServer.class */
public class RedisServer extends RedisNode {
    private Properties properties;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/RedisServer$INFO.class */
    public enum INFO {
        NAME("name"),
        HOST("ip"),
        PORT("port"),
        RUN_ID("runid"),
        FLAGS("flags"),
        PENDING_COMMANDS("pending-commands"),
        LAST_PING_SENT("last-ping-sent"),
        LAST_OK_PING_REPLY("last-ok-ping-reply"),
        DOWN_AFTER_MILLISECONDS("down-after-milliseconds"),
        INFO_REFRESH("info-refresh"),
        ROLE_REPORTED("role-reported"),
        ROLE_REPORTED_TIME("role-reported-time"),
        CONFIG_EPOCH("config-epoch"),
        NUMBER_SLAVES("num-slaves"),
        NUMBER_OTHER_SENTINELS("num-other-sentinels"),
        BUFFER_LENGTH("qbuf"),
        BUFFER_FREE_SPACE("qbuf-free"),
        OUTPUT_BUFFER_LENGTH("obl"),
        OUTPUT_LIST_LENGTH("number-other-sentinels"),
        QUORUM("quorum"),
        FAILOVER_TIMEOUT("failover-timeout"),
        PARALLEL_SYNCS("parallel-syncs");

        String key;

        INFO(String key) {
            this.key = key;
        }
    }

    public RedisServer(String host, int port) {
        this(host, port, new Properties());
    }

    public RedisServer(String host, int port, Properties properties) {
        super(host, port);
        this.properties = properties;
        String name = host + ":" + port;
        if (properties != null && properties.containsKey(INFO.NAME.key)) {
            name = get(INFO.NAME);
        }
        setName(name);
    }

    public static RedisServer newServerFrom(Properties properties) throws NumberFormatException {
        String host = properties.getProperty(INFO.HOST.key, NetFlags.LOOPBACK_ADDRESS);
        int port = Integer.parseInt(properties.getProperty(INFO.PORT.key, "26379"));
        return new RedisServer(host, port, properties);
    }

    public void setQuorum(Long quorum) {
        if (quorum == null) {
            this.properties.remove(INFO.QUORUM.key);
        } else {
            this.properties.put(INFO.QUORUM.key, quorum.toString());
        }
    }

    public String getRunId() {
        return get(INFO.RUN_ID);
    }

    public String getFlags() {
        return get(INFO.FLAGS);
    }

    @Override // org.springframework.data.redis.connection.RedisNode
    public boolean isMaster() {
        String role = getRoleReported();
        if (!StringUtils.hasText(role)) {
            return false;
        }
        return role.equalsIgnoreCase("master");
    }

    public Long getPendingCommands() {
        return getLongValueOf(INFO.PENDING_COMMANDS);
    }

    public Long getLastPingSent() {
        return getLongValueOf(INFO.LAST_PING_SENT);
    }

    public Long getLastOkPingReply() {
        return getLongValueOf(INFO.LAST_OK_PING_REPLY);
    }

    public Long getDownAfterMilliseconds() {
        return getLongValueOf(INFO.DOWN_AFTER_MILLISECONDS);
    }

    public Long getInfoRefresh() {
        return getLongValueOf(INFO.INFO_REFRESH);
    }

    public String getRoleReported() {
        return get(INFO.ROLE_REPORTED);
    }

    public Long roleReportedTime() {
        return getLongValueOf(INFO.ROLE_REPORTED_TIME);
    }

    public Long getConfigEpoch() {
        return getLongValueOf(INFO.CONFIG_EPOCH);
    }

    public Long getNumberSlaves() {
        return getLongValueOf(INFO.NUMBER_SLAVES);
    }

    public Long getNumberOtherSentinels() {
        return getLongValueOf(INFO.NUMBER_OTHER_SENTINELS);
    }

    public Long getQuorum() {
        return getLongValueOf(INFO.QUORUM);
    }

    public Long getFailoverTimeout() {
        return getLongValueOf(INFO.FAILOVER_TIMEOUT);
    }

    public Long getParallelSyncs() {
        return getLongValueOf(INFO.PARALLEL_SYNCS);
    }

    public String get(INFO info) {
        Assert.notNull(info, "Cannot retrieve client information for 'null'.");
        return get(info.key);
    }

    public String get(String key) {
        Assert.hasText(key, "Cannot get information for 'empty' / 'null' key.");
        return this.properties.getProperty(key);
    }

    private Long getLongValueOf(INFO info) {
        String value = get(info);
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }
}
