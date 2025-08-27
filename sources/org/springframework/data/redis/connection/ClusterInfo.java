package org.springframework.data.redis.connection;

import java.util.Properties;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterInfo.class */
public class ClusterInfo {
    private final Properties clusterProperties;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/ClusterInfo$Info.class */
    public enum Info {
        STATE("cluster_state"),
        SLOTS_ASSIGNED("cluster_slots_assigned"),
        SLOTS_OK("cluster_slots_ok"),
        SLOTS_PFAIL("cluster_slots_pfail"),
        SLOTS_FAIL("cluster_slots_fail"),
        KNOWN_NODES("cluster_known_nodes"),
        SIZE("cluster_size"),
        CURRENT_EPOCH("cluster_current_epoch"),
        MY_EPOCH("cluster_my_epoch"),
        MESSAGES_SENT("cluster_stats_messages_sent"),
        MESSAGES_RECEIVED("cluster_stats_messages_received");

        String key;

        Info(String key) {
            this.key = key;
        }
    }

    public ClusterInfo(Properties clusterProperties) {
        Assert.notNull(clusterProperties, "ClusterProperties must not be null!");
        this.clusterProperties = clusterProperties;
    }

    public String getState() {
        return get(Info.STATE);
    }

    public Long getSlotsAssigned() {
        return getLongValueOf(Info.SLOTS_ASSIGNED);
    }

    public Long getSlotsOk() {
        return getLongValueOf(Info.SLOTS_OK);
    }

    public Long getSlotsPfail() {
        return getLongValueOf(Info.SLOTS_PFAIL);
    }

    public Long getSlotsFail() {
        return getLongValueOf(Info.SLOTS_FAIL);
    }

    public Long getKnownNodes() {
        return getLongValueOf(Info.KNOWN_NODES);
    }

    public Long getClusterSize() {
        return getLongValueOf(Info.SIZE);
    }

    public Long getCurrentEpoch() {
        return getLongValueOf(Info.CURRENT_EPOCH);
    }

    public Long getMessagesSent() {
        return getLongValueOf(Info.MESSAGES_SENT);
    }

    public Long getMessagesReceived() {
        return getLongValueOf(Info.MESSAGES_RECEIVED);
    }

    public String get(Info info) {
        Assert.notNull(info, "Cannot retrieve cluster information for 'null'.");
        return get(info.key);
    }

    public String get(String key) {
        Assert.hasText(key, "Cannot get cluster information for 'empty' / 'null' key.");
        return this.clusterProperties.getProperty(key);
    }

    private Long getLongValueOf(Info info) {
        String value = get(info);
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    public String toString() {
        return this.clusterProperties.toString();
    }
}
