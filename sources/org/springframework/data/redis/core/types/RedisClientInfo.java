package org.springframework.data.redis.core.types;

import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;
import org.springframework.util.Assert;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/types/RedisClientInfo.class */
public class RedisClientInfo {
    private final Properties clientProperties;

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/types/RedisClientInfo$INFO.class */
    public enum INFO {
        ADDRESS_PORT("addr"),
        FILE_DESCRIPTOR("fd"),
        CONNECTION_NAME("name"),
        CONNECTION_AGE("age"),
        CONNECTION_IDLE("idle"),
        FLAGS("flags"),
        DATABSE_ID("db"),
        CHANNEL_SUBSCRIBTIONS(Claims.SUBJECT),
        PATTERN_SUBSCRIBTIONS("psub"),
        MULIT_COMMAND_CONTEXT("multi"),
        BUFFER_LENGTH("qbuf"),
        BUFFER_FREE_SPACE("qbuf-free"),
        OUTPUT_BUFFER_LENGTH("obl"),
        OUTPUT_LIST_LENGTH("oll"),
        OUTPUT_BUFFER_MEMORY_USAGE("omem"),
        EVENTS("events"),
        LAST_COMMAND("cmd");

        String key;

        INFO(String key) {
            this.key = key;
        }
    }

    public RedisClientInfo(Properties properties) {
        Assert.notNull(properties, "Cannot initialize client information for given 'null' properties.");
        this.clientProperties = new Properties();
        this.clientProperties.putAll(properties);
    }

    public String getAddressPort() {
        return get(INFO.ADDRESS_PORT);
    }

    public String getFileDescriptor() {
        return get(INFO.FILE_DESCRIPTOR);
    }

    public String getName() {
        return get(INFO.CONNECTION_NAME);
    }

    public Long getAge() {
        return getLongValueOf(INFO.CONNECTION_AGE);
    }

    public Long getIdle() {
        return getLongValueOf(INFO.CONNECTION_IDLE);
    }

    public String getFlags() {
        return get(INFO.FLAGS);
    }

    public Long getDatabaseId() {
        return getLongValueOf(INFO.DATABSE_ID);
    }

    public Long getChannelSubscribtions() {
        return getLongValueOf(INFO.CHANNEL_SUBSCRIBTIONS);
    }

    public Long getPatternSubscrbtions() {
        return getLongValueOf(INFO.PATTERN_SUBSCRIBTIONS);
    }

    public Long getMultiCommandContext() {
        return getLongValueOf(INFO.MULIT_COMMAND_CONTEXT);
    }

    public Long getBufferLength() {
        return getLongValueOf(INFO.BUFFER_LENGTH);
    }

    public Long getBufferFreeSpace() {
        return getLongValueOf(INFO.BUFFER_FREE_SPACE);
    }

    public Long getOutputBufferLength() {
        return getLongValueOf(INFO.OUTPUT_BUFFER_LENGTH);
    }

    public Long getOutputListLength() {
        return getLongValueOf(INFO.OUTPUT_LIST_LENGTH);
    }

    public Long getOutputBufferMemoryUsage() {
        return getLongValueOf(INFO.OUTPUT_BUFFER_MEMORY_USAGE);
    }

    public String getEvents() {
        return get(INFO.EVENTS);
    }

    public String getLastCommand() {
        return get(INFO.LAST_COMMAND);
    }

    public String get(INFO info) {
        Assert.notNull(info, "Cannot retrieve client information for 'null'.");
        return get(info.key);
    }

    public String get(String key) {
        Assert.hasText(key, "Cannot get client information for 'empty' / 'null' key.");
        return this.clientProperties.getProperty(key);
    }

    private Long getLongValueOf(INFO info) {
        String value = get(info);
        if (value == null) {
            return null;
        }
        return Long.valueOf(value);
    }

    public String toString() {
        return this.clientProperties.toString();
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/core/types/RedisClientInfo$RedisClientInfoBuilder.class */
    public static class RedisClientInfoBuilder {
        public static RedisClientInfo fromString(String source) throws IOException {
            Assert.notNull(source, "Cannot read client properties form 'null'.");
            Properties properties = new Properties();
            try {
                properties.load(new StringReader(source.replace(' ', '\n')));
                return new RedisClientInfo(properties);
            } catch (IOException e) {
                throw new IllegalArgumentException(String.format("Properties could not be loaded from String '%s'.", source), e);
            }
        }
    }

    public int hashCode() {
        int result = (31 * 1) + (this.clientProperties == null ? 0 : this.clientProperties.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof RedisClientInfo)) {
            return false;
        }
        RedisClientInfo other = (RedisClientInfo) obj;
        if (this.clientProperties == null) {
            if (other.clientProperties != null) {
                return false;
            }
            return true;
        }
        if (!this.clientProperties.equals(other.clientProperties)) {
            return false;
        }
        return true;
    }
}
