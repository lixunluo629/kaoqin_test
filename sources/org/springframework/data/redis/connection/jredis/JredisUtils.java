package org.springframework.data.redis.connection.jredis;

import java.util.Map;
import java.util.Properties;
import org.jredis.ClientRuntimeException;
import org.jredis.RedisException;
import org.jredis.RedisType;
import org.jredis.Sort;
import org.jredis.connector.NotConnectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.SortParameters;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisUtils.class */
public abstract class JredisUtils {
    public static DataAccessException convertJredisAccessException(RedisException ex) {
        return new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
    }

    public static DataAccessException convertJredisAccessException(ClientRuntimeException ex) {
        if (ex instanceof NotConnectedException) {
            return new RedisConnectionFailureException(ex.getMessage(), ex);
        }
        return new InvalidDataAccessResourceUsageException(ex.getMessage(), ex);
    }

    /* renamed from: org.springframework.data.redis.connection.jredis.JredisUtils$1, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jredis/JredisUtils$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$jredis$RedisType = new int[RedisType.values().length];

        static {
            try {
                $SwitchMap$org$jredis$RedisType[RedisType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jredis$RedisType[RedisType.string.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jredis$RedisType[RedisType.list.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$jredis$RedisType[RedisType.set.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$jredis$RedisType[RedisType.hash.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    static DataType convertDataType(RedisType type) {
        switch (AnonymousClass1.$SwitchMap$org$jredis$RedisType[type.ordinal()]) {
            case 1:
                return DataType.NONE;
            case 2:
                return DataType.STRING;
            case 3:
                return DataType.LIST;
            case 4:
                return DataType.SET;
            case 5:
                return DataType.HASH;
            default:
                return null;
        }
    }

    static Sort applySortingParams(Sort jredisSort, SortParameters params, byte[] storeKey) {
        if (params != null) {
            byte[] byPattern = params.getByPattern();
            if (byPattern != null) {
                jredisSort.BY(byPattern);
            }
            byte[][] getPattern = params.getGetPattern();
            if (getPattern != null && getPattern.length > 0) {
                for (byte[] bs : getPattern) {
                    jredisSort.GET(bs);
                }
            }
            SortParameters.Range limit = params.getLimit();
            if (limit != null) {
                jredisSort.LIMIT(limit.getStart(), limit.getCount());
            }
            SortParameters.Order order = params.getOrder();
            if (order != null && order.equals(SortParameters.Order.DESC)) {
                jredisSort.DESC();
            }
            Boolean isAlpha = params.isAlphabetic();
            if (isAlpha != null && isAlpha.booleanValue()) {
                jredisSort.ALPHA();
            }
        }
        if (storeKey != null) {
            jredisSort.STORE(storeKey);
        }
        return jredisSort;
    }

    static Properties info(Map<String, String> map) {
        Properties info = new Properties();
        info.putAll(map);
        return info;
    }

    static Long toLong(Boolean source) {
        return Long.valueOf(source.booleanValue() ? 1L : 0L);
    }
}
