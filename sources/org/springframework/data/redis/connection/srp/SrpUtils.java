package org.springframework.data.redis.connection.srp;

import com.google.common.base.Charsets;
import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.util.Assert;
import redis.client.RedisException;
import redis.reply.BulkReply;
import redis.reply.IntegerReply;
import redis.reply.MultiBulkReply;
import redis.reply.Reply;
import redis.reply.StatusReply;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpUtils.class */
abstract class SrpUtils {
    private static final byte[] ONE = {49};
    private static final byte[] ZERO = {48};
    private static final byte[] BEFORE = "BEFORE".getBytes(Charsets.UTF_8);
    private static final byte[] AFTER = "AFTER".getBytes(Charsets.UTF_8);
    static final byte[] WITHSCORES = "WITHSCORES".getBytes(Charsets.UTF_8);
    private static final byte[] SPACE = SymbolConstants.SPACE_SYMBOL.getBytes(Charsets.UTF_8);
    private static final byte[] BY = "BY".getBytes(Charsets.UTF_8);
    private static final byte[] GET = "GET".getBytes(Charsets.UTF_8);
    private static final byte[] ALPHA = "ALPHA".getBytes(Charsets.UTF_8);
    private static final byte[] STORE = "STORE".getBytes(Charsets.UTF_8);

    SrpUtils() {
    }

    static DataAccessException convertSRedisAccessException(RuntimeException ex) {
        if (ex instanceof RedisException) {
            return new RedisSystemException("redis exception", ex);
        }
        return null;
    }

    static Properties info(BulkReply reply) {
        Properties info = new Properties();
        StringReader stringReader = new StringReader(new String(reply.data(), Charsets.UTF_8));
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

    static List<byte[]> toBytesList(Reply[] replies) {
        if (replies == null) {
            return null;
        }
        List<byte[]> list = new ArrayList<>(replies.length);
        for (Reply reply : replies) {
            Object data = reply.data();
            if (data == null) {
                list.add(null);
            } else if (data instanceof byte[]) {
                list.add((byte[]) data);
            } else {
                throw new IllegalArgumentException("array contains more then just nulls and bytes -> " + data);
            }
        }
        return list;
    }

    static List<String> asStatusList(Reply[] replies) {
        List<String> statuses = new ArrayList<>();
        for (Reply reply : replies) {
            statuses.add(((StatusReply) reply).data());
        }
        return statuses;
    }

    static <T> List<T> toList(T[] byteArrays) {
        return Arrays.asList(byteArrays);
    }

    static Set<byte[]> toSet(Reply[] byteArrays) {
        return new LinkedHashSet(toBytesList(byteArrays));
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

    static byte[] asBit(boolean value) {
        return value ? ONE : ZERO;
    }

    static byte[] convertPosition(RedisListCommands.Position where) {
        Assert.notNull(where, "list positions are mandatory");
        return RedisListCommands.Position.AFTER.equals(where) ? AFTER : BEFORE;
    }

    static Double toDouble(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return Double.valueOf(new String(bytes, Charsets.UTF_8));
    }

    static Long toLong(Object[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return Long.valueOf(new String((byte[]) bytes[0], Charsets.UTF_8));
    }

    static Set<RedisZSetCommands.Tuple> convertTuple(MultiBulkReply zrange) {
        return convertTuple(zrange.data());
    }

    static Set<RedisZSetCommands.Tuple> convertTuple(Reply[] byteArrays) {
        Set<RedisZSetCommands.Tuple> tuples = new LinkedHashSet<>((byteArrays.length / 2) + 1);
        int i = 0;
        while (i < byteArrays.length) {
            byte[] value = (byte[]) byteArrays[i].data();
            int i2 = i + 1;
            Double score = toDouble((byte[]) byteArrays[i2].data());
            tuples.add(new DefaultTuple(value, score));
            i = i2 + 1;
        }
        return tuples;
    }

    static Object[] convert(int timeout, byte[]... keys) {
        int length = keys != null ? keys.length + 1 : 1;
        Object[] args = new Object[length];
        if (keys != null) {
            for (int i = 0; i < keys.length; i++) {
                args[i] = keys[i];
            }
        }
        args[length - 1] = String.valueOf(timeout).getBytes();
        return args;
    }

    static Map<byte[], byte[]> toMap(Reply[] byteArrays) {
        Map<byte[], byte[]> map = new LinkedHashMap<>(byteArrays.length / 2);
        int i = 0;
        while (i < byteArrays.length) {
            int i2 = i;
            int i3 = i + 1;
            map.put((byte[]) byteArrays[i2].data(), (byte[]) byteArrays[i3].data());
            i = i3 + 1;
        }
        return map;
    }

    static Boolean asBoolean(IntegerReply reply) {
        if (reply == null) {
            return null;
        }
        Long l = 1L;
        return Boolean.valueOf(l.equals(reply.data()));
    }

    static byte[] limit(long offset, long count) {
        return ("LIMIT " + offset + SymbolConstants.SPACE_SYMBOL + count).getBytes(Charsets.UTF_8);
    }

    static Object[] limitParams(long offset, long count) {
        return new Object[]{"LIMIT".getBytes(Charsets.UTF_8), String.valueOf(offset).getBytes(Charsets.UTF_8), String.valueOf(count).getBytes(Charsets.UTF_8)};
    }

    static byte[] sort(SortParameters params) {
        return sort(params, null);
    }

    static byte[] sort(SortParameters params, byte[] sortKey) {
        List<byte[]> arrays = new ArrayList<>();
        Object[] sortParams = sortParams(params, sortKey);
        for (Object param : sortParams) {
            arrays.add((byte[]) param);
            arrays.add(SPACE);
        }
        arrays.remove(arrays.size() - 1);
        int size = 0;
        Iterator<byte[]> it = arrays.iterator();
        while (it.hasNext()) {
            size += ((byte[]) it.next()).length;
        }
        byte[] result = new byte[size];
        int index = 0;
        for (byte[] bs : arrays) {
            System.arraycopy(bs, 0, result, index, bs.length);
            index += bs.length;
        }
        return result;
    }

    static Object[] sortParams(SortParameters params) {
        return sortParams(params, null);
    }

    static Object[] sortParams(SortParameters params, byte[] sortKey) {
        List<byte[]> arrays = new ArrayList<>();
        if (params != null) {
            if (params.getByPattern() != null) {
                arrays.add(BY);
                arrays.add(params.getByPattern());
            }
            if (params.getLimit() != null) {
                arrays.add(limit(params.getLimit().getStart(), params.getLimit().getCount()));
            }
            if (params.getGetPattern() != null) {
                byte[][] pattern = params.getGetPattern();
                for (byte[] bs : pattern) {
                    arrays.add(GET);
                    arrays.add(bs);
                }
            }
            if (params.getOrder() != null) {
                arrays.add(params.getOrder().name().getBytes(Charsets.UTF_8));
            }
            Boolean isAlpha = params.isAlphabetic();
            if (isAlpha != null && isAlpha.booleanValue()) {
                arrays.add(ALPHA);
            }
        }
        if (sortKey != null) {
            arrays.add(STORE);
            arrays.add(sortKey);
        }
        return arrays.toArray();
    }

    static byte[] bitOp(RedisStringCommands.BitOperation op) {
        Assert.notNull(op, "The bit operation is required");
        return op.name().toUpperCase().getBytes(Charsets.UTF_8);
    }

    static String asShasum(Reply reply) {
        Object data = reply.data();
        return data instanceof String ? (String) data : new String((byte[]) data, Charsets.UTF_8);
    }

    static List<Boolean> asBooleanList(Reply reply) {
        if (!(reply instanceof MultiBulkReply)) {
            throw new IllegalArgumentException();
        }
        List<Boolean> results = new ArrayList<>();
        for (IntegerReply integerReply : ((MultiBulkReply) reply).data()) {
            results.add(asBoolean(integerReply));
        }
        return results;
    }

    static List<Long> asIntegerList(Reply[] replies) {
        List<Long> results = new ArrayList<>();
        for (Reply reply : replies) {
            results.add(((IntegerReply) reply).data());
        }
        return results;
    }

    static List<Object> asList(MultiBulkReply genericReply) {
        Reply[] replies = genericReply.data();
        List<Object> results = new ArrayList<>();
        for (Reply reply : replies) {
            results.add(reply.data());
        }
        return results;
    }

    static Object convertScriptReturn(ReturnType returnType, Reply reply) {
        if (reply instanceof MultiBulkReply) {
            return asList((MultiBulkReply) reply);
        }
        if (returnType == ReturnType.BOOLEAN) {
            if (reply.data() == null) {
                return Boolean.FALSE;
            }
            return Boolean.valueOf(((Long) reply.data()).longValue() == 1);
        }
        return reply.data();
    }
}
