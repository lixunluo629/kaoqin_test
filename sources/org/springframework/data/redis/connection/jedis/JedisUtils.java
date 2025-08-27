package org.springframework.data.redis.connection.jedis;

import java.io.IOException;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.util.Assert;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.SafeEncoder;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisUtils.class */
public abstract class JedisUtils {
    private static final String OK_CODE = "OK";
    private static final String OK_MULTI_CODE = "+OK";
    private static final byte[] ONE = {49};
    private static final byte[] ZERO = {48};

    public static DataAccessException convertJedisAccessException(JedisException ex) {
        if (ex instanceof JedisDataException) {
            return new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
        }
        if (ex instanceof JedisConnectionException) {
            return new RedisConnectionFailureException(ex.getMessage(), ex);
        }
        return new InvalidDataAccessApiUsageException(ex.getMessage(), ex);
    }

    public static DataAccessException convertJedisAccessException(RuntimeException ex) {
        if (ex instanceof JedisException) {
            return convertJedisAccessException((JedisException) ex);
        }
        return null;
    }

    static DataAccessException convertJedisAccessException(IOException ex) {
        if (ex instanceof UnknownHostException) {
            return new RedisConnectionFailureException("Unknown host " + ex.getMessage(), ex);
        }
        return new RedisConnectionFailureException("Could not connect to Redis server", ex);
    }

    static DataAccessException convertJedisAccessException(TimeoutException ex) {
        throw new RedisConnectionFailureException("Jedis pool timed out. Could not get Redis Connection", ex);
    }

    static boolean isStatusOk(String status) {
        return status != null && (OK_CODE.equals(status) || OK_MULTI_CODE.equals(status));
    }

    @Deprecated
    static Boolean convertCodeReply(Number code) {
        if (code != null) {
            return Boolean.valueOf(code.intValue() == 1);
        }
        return null;
    }

    @Deprecated
    static Set<RedisZSetCommands.Tuple> convertJedisTuple(Set<Tuple> tuples) {
        Set<RedisZSetCommands.Tuple> value = new LinkedHashSet<>(tuples.size());
        for (Tuple tuple : tuples) {
            value.add(new DefaultTuple(tuple.getBinaryElement(), Double.valueOf(tuple.getScore())));
        }
        return value;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    static byte[][] convert(Map<byte[], byte[]> hgetAll) {
        ?? r0 = new byte[hgetAll.size() * 2];
        int index = 0;
        for (Map.Entry<byte[], byte[]> entry : hgetAll.entrySet()) {
            int i = index;
            int index2 = index + 1;
            r0[i] = entry.getKey();
            index = index2 + 1;
            r0[index2] = entry.getValue();
        }
        return r0;
    }

    @Deprecated
    static Map<String, String> convert(String[] fields, String[] values) {
        Map<String, String> result = new LinkedHashMap<>(fields.length);
        for (int i = 0; i < values.length; i++) {
            result.put(fields[i], values[i]);
        }
        return result;
    }

    @Deprecated
    static String[] arrange(String[] keys, String[] values) {
        String[] result = new String[keys.length * 2];
        for (int i = 0; i < keys.length; i++) {
            int index = i << 1;
            result[index] = keys[i];
            result[index + 1] = values[i];
        }
        return result;
    }

    static SortingParams convertSortParams(SortParameters params) {
        SortingParams jedisParams = null;
        if (params != null) {
            jedisParams = new SortingParams();
            byte[] byPattern = params.getByPattern();
            if (byPattern != null) {
                jedisParams.by(params.getByPattern());
            }
            byte[][] getPattern = params.getGetPattern();
            if (getPattern != null) {
                jedisParams.get(getPattern);
            }
            SortParameters.Range limit = params.getLimit();
            if (limit != null) {
                jedisParams.limit((int) limit.getStart(), (int) limit.getCount());
            }
            SortParameters.Order order = params.getOrder();
            if (order != null && order.equals(SortParameters.Order.DESC)) {
                jedisParams.desc();
            }
            Boolean isAlpha = params.isAlphabetic();
            if (isAlpha != null && isAlpha.booleanValue()) {
                jedisParams.alpha();
            }
        }
        return jedisParams;
    }

    @Deprecated
    static byte[] asBit(boolean value) {
        return value ? ONE : ZERO;
    }

    static BinaryClient.LIST_POSITION convertPosition(RedisListCommands.Position where) {
        Assert.notNull(where, "list positions are mandatory");
        return RedisListCommands.Position.AFTER.equals(where) ? BinaryClient.LIST_POSITION.AFTER : BinaryClient.LIST_POSITION.BEFORE;
    }

    @Deprecated
    static Properties info(String string) {
        Properties info = new Properties();
        StringReader stringReader = new StringReader(string);
        try {
            try {
                info.load(stringReader);
                stringReader.close();
                return info;
            } catch (Exception ex) {
                throw new RedisSystemException("Cannot read Redis info", ex);
            }
        } catch (Throwable th) {
            stringReader.close();
            throw th;
        }
    }

    static BinaryJedisPubSub adaptPubSub(MessageListener listener) {
        return new JedisMessageListener(listener);
    }

    @Deprecated
    static String[] convert(byte[]... raw) {
        String[] result = new String[raw.length];
        for (int i = 0; i < raw.length; i++) {
            result[i] = SafeEncoder.encode(raw[i]);
        }
        return result;
    }

    static byte[][] bXPopArgs(int timeout, byte[]... keys) {
        List<byte[]> args = new ArrayList<>();
        for (byte[] arg : keys) {
            args.add(arg);
        }
        args.add(Protocol.toByteArray(timeout));
        return (byte[][]) args.toArray((Object[]) new byte[args.size()]);
    }

    @Deprecated
    static byte[] asBytes(int number) {
        return String.valueOf(number).getBytes();
    }

    @Deprecated
    static String asString(byte[] raw) {
        return SafeEncoder.encode(raw);
    }

    static Object convertScriptReturn(ReturnType returnType, Object result) {
        if (result instanceof String) {
            return SafeEncoder.encode((String) result);
        }
        if (returnType == ReturnType.STATUS) {
            return asString((byte[]) result);
        }
        if (returnType == ReturnType.BOOLEAN) {
            if (result == null) {
                return Boolean.FALSE;
            }
            return Boolean.valueOf(((Long) result).longValue() == 1);
        }
        if (returnType == ReturnType.MULTI) {
            List<Object> resultList = (List) result;
            List<Object> convertedResults = new ArrayList<>();
            for (Object res : resultList) {
                if (res instanceof String) {
                    convertedResults.add(SafeEncoder.encode((String) res));
                } else {
                    convertedResults.add(res);
                }
            }
            return convertedResults;
        }
        return result;
    }
}
