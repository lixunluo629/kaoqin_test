package org.springframework.data.redis.connection.srp;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.LongToBooleanConverter;
import org.springframework.data.redis.connection.convert.StringToRedisClientInfoConverter;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import redis.client.RedisException;
import redis.reply.IntegerReply;
import redis.reply.Reply;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpConverters.class */
public abstract class SrpConverters extends Converters {
    private static final byte[] BEFORE = "BEFORE".getBytes(Charsets.UTF_8);
    private static final byte[] AFTER = "AFTER".getBytes(Charsets.UTF_8);
    private static final Converter<String[], List<RedisClientInfo>> STRING_TO_LIST_OF_CLIENT_INFO = new StringToRedisClientInfoConverter();
    private static final Converter<Long, Boolean> LONG_TO_BOOLEAN = new LongToBooleanConverter();
    private static final Converter<Reply[], List<byte[]>> REPLIES_TO_BYTES_LIST = new Converter<Reply[], List<byte[]>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.1
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<byte[]> convert2(Reply[] replies) {
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
    };
    private static final Converter<Reply[], Set<byte[]>> REPLIES_TO_BYTES_SET = new Converter<Reply[], Set<byte[]>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.2
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Set<byte[]> convert2(Reply[] source) {
            if (source != null) {
                return new LinkedHashSet(SrpConverters.toBytesList(source));
            }
            return null;
        }
    };
    private static final Converter<byte[], Properties> BYTES_TO_PROPERTIES = new Converter<byte[], Properties>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.3
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Properties convert2(byte[] source) {
            if (source != null) {
                return SrpConverters.toProperties(new String(source, Charsets.UTF_8));
            }
            return null;
        }
    };
    private static final Converter<byte[], Double> BYTES_TO_DOUBLE = new Converter<byte[], Double>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.4
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Double convert2(byte[] bytes) {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            return Double.valueOf(new String(bytes, Charsets.UTF_8));
        }
    };
    private static final Converter<Reply[], Long> REPLIES_TO_TIME_AS_LONG = new Converter<Reply[], Long>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.5
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Long convert2(Reply[] reply) {
            Assert.notEmpty(reply, "Received invalid result from server. Expected 2 items in collection.");
            Assert.isTrue(reply.length == 2, "Received invalid nr of arguments from redis server. Expected 2 received " + reply.length);
            List<String> serverTimeInformation = (List) SrpConverters.REPLIES_TO_STRING_LIST.convert2(reply);
            return Converters.toTimeMillis(serverTimeInformation.get(0), serverTimeInformation.get(1));
        }
    };
    private static final Converter<Reply[], Set<RedisZSetCommands.Tuple>> REPLIES_TO_TUPLE_SET = new Converter<Reply[], Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.6
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Set<RedisZSetCommands.Tuple> convert2(Reply[] byteArrays) {
            if (byteArrays == null) {
                return null;
            }
            Set<RedisZSetCommands.Tuple> tuples = new LinkedHashSet<>((byteArrays.length / 2) + 1);
            int i = 0;
            while (i < byteArrays.length) {
                byte[] value = (byte[]) byteArrays[i].data();
                int i2 = i + 1;
                Double score = SrpConverters.toDouble((byte[]) byteArrays[i2].data());
                tuples.add(new DefaultTuple(value, score));
                i = i2 + 1;
            }
            return tuples;
        }
    };
    private static final Converter<Reply[], Map<byte[], byte[]>> REPLIES_TO_BYTES_MAP = new Converter<Reply[], Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.7
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Map<byte[], byte[]> convert2(Reply[] byteArrays) {
            if (byteArrays == null) {
                return null;
            }
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
    };
    private static final Converter<byte[], String> BYTES_TO_STRING = new Converter<byte[], String>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.8
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public String convert2(byte[] data) {
            if (data != null) {
                return new String(data, Charsets.UTF_8);
            }
            return null;
        }
    };
    private static final Converter<Reply[], List<Boolean>> REPLIES_TO_BOOLEAN_LIST = new Converter<Reply[], List<Boolean>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.9
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<Boolean> convert2(Reply[] source) {
            if (source == null) {
                return null;
            }
            List<Boolean> results = new ArrayList<>();
            for (Reply r : source) {
                results.add(SrpConverters.toBoolean(((IntegerReply) r).data()));
            }
            return results;
        }
    };
    private static final Converter<Reply[], List<String>> REPLIES_TO_STRING_LIST = new Converter<Reply[], List<String>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.10
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<String> convert2(Reply[] source) {
            if (source == null) {
                return null;
            }
            List<String> results = new ArrayList<>();
            for (Reply r : source) {
                results.add(SrpConverters.toString((byte[]) r.data()));
            }
            return results;
        }
    };
    private static final Converter<Reply, String> REPLY_TO_STRING = new Converter<Reply, String>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.11
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public String convert2(Reply source) {
            if (source == null) {
                return null;
            }
            return SrpConverters.toString((byte[]) source.data());
        }
    };
    private static final Converter<Reply, List<RedisClientInfo>> REPLY_T0_LIST_OF_CLIENT_INFO = new Converter<Reply, List<RedisClientInfo>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.12
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<RedisClientInfo> convert2(Reply source) {
            if (source == null || source.data() == null) {
                return Collections.emptyList();
            }
            Assert.isInstanceOf(byte[].class, source.data(), "Expected data to be an instace of byte [].");
            return (List) SrpConverters.BYTEARRAY_T0_LIST_OF_CLIENT_INFO.convert2((byte[]) source.data());
        }
    };
    private static final Converter<byte[], List<RedisClientInfo>> BYTEARRAY_T0_LIST_OF_CLIENT_INFO = new Converter<byte[], List<RedisClientInfo>>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.13
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<RedisClientInfo> convert2(byte[] source) {
            if (source == null || source.length == 0) {
                return Collections.emptyList();
            }
            String s = SrpConverters.toString(source);
            return (List) SrpConverters.STRING_TO_LIST_OF_CLIENT_INFO.convert2(s.split("\\r?\\n"));
        }
    };
    private static final Converter<IntegerReply, Boolean> INTEGER_REPLY_TO_BOOLEAN = new Converter<IntegerReply, Boolean>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.14
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Boolean convert2(IntegerReply source) {
            if (source == null || source.data() == null) {
                return false;
            }
            return Boolean.valueOf(source.data().longValue() == 1);
        }
    };
    private static final Converter<Exception, DataAccessException> EXCEPTION_CONVERTER = new Converter<Exception, DataAccessException>() { // from class: org.springframework.data.redis.connection.srp.SrpConverters.15
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public DataAccessException convert2(Exception ex) {
            if (ex instanceof RedisException) {
                return new RedisSystemException("redis exception", ex);
            }
            if (ex instanceof IOException) {
                return new RedisConnectionFailureException("Redis connection failed", (IOException) ex);
            }
            return null;
        }
    };

    public static Converter<Reply[], List<byte[]>> repliesToBytesList() {
        return REPLIES_TO_BYTES_LIST;
    }

    public static Converter<Reply[], Set<byte[]>> repliesToBytesSet() {
        return REPLIES_TO_BYTES_SET;
    }

    public static Converter<byte[], Properties> bytesToProperties() {
        return BYTES_TO_PROPERTIES;
    }

    public static Converter<byte[], Double> bytesToDouble() {
        return BYTES_TO_DOUBLE;
    }

    public static Converter<Reply[], Set<RedisZSetCommands.Tuple>> repliesToTupleSet() {
        return REPLIES_TO_TUPLE_SET;
    }

    public static Converter<Reply[], Map<byte[], byte[]>> repliesToBytesMap() {
        return REPLIES_TO_BYTES_MAP;
    }

    public static Converter<byte[], String> bytesToString() {
        return BYTES_TO_STRING;
    }

    public static Converter<Reply, String> replyToString() {
        return REPLY_TO_STRING;
    }

    public static Converter<Reply[], List<Boolean>> repliesToBooleanList() {
        return REPLIES_TO_BOOLEAN_LIST;
    }

    public static Converter<Reply[], List<String>> repliesToStringList() {
        return REPLIES_TO_STRING_LIST;
    }

    public static Converter<Reply[], Long> repliesToTimeAsLong() {
        return REPLIES_TO_TIME_AS_LONG;
    }

    public static List<RedisClientInfo> toListOfRedisClientInformation(Reply reply) {
        return REPLY_T0_LIST_OF_CLIENT_INFO.convert2(reply);
    }

    public static Converter<Long, Boolean> longToBooleanConverter() {
        return LONG_TO_BOOLEAN;
    }

    public static List<byte[]> toBytesList(Reply[] source) {
        return REPLIES_TO_BYTES_LIST.convert2(source);
    }

    public static Set<byte[]> toBytesSet(Reply[] source) {
        return REPLIES_TO_BYTES_SET.convert2(source);
    }

    public static Properties toProperties(byte[] source) {
        return BYTES_TO_PROPERTIES.convert2(source);
    }

    public static Double toDouble(byte[] source) {
        return BYTES_TO_DOUBLE.convert2(source);
    }

    public static Set<RedisZSetCommands.Tuple> toTupleSet(Reply[] source) {
        return REPLIES_TO_TUPLE_SET.convert2(source);
    }

    public static Map<byte[], byte[]> toBytesMap(Reply[] source) {
        return REPLIES_TO_BYTES_MAP.convert2(source);
    }

    public static String toString(Reply source) {
        return REPLY_TO_STRING.convert2(source);
    }

    public static String toString(byte[] source) {
        return BYTES_TO_STRING.convert2(source);
    }

    public static List<Boolean> toBooleanList(Reply[] source) {
        return REPLIES_TO_BOOLEAN_LIST.convert2(source);
    }

    public static List<String> toStringList(Reply[] source) {
        return REPLIES_TO_STRING_LIST.convert2(source);
    }

    public static Long toTimeAsLong(Reply[] source) {
        return REPLIES_TO_TIME_AS_LONG.convert2(source);
    }

    public static byte[] toBytes(RedisStringCommands.BitOperation op) {
        Assert.notNull(op, "The bit operation is required");
        return op.name().toUpperCase().getBytes(Charsets.UTF_8);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [byte[], byte[][]] */
    public static byte[][] toByteArrays(Map<byte[], byte[]> source) {
        ?? r0 = new byte[source.size() * 2];
        int index = 0;
        for (Map.Entry<byte[], byte[]> entry : source.entrySet()) {
            int i = index;
            int index2 = index + 1;
            r0[i] = entry.getKey();
            index = index2 + 1;
            r0[index2] = entry.getValue();
        }
        return r0;
    }

    public static byte[] toBytes(RedisListCommands.Position source) {
        Assert.notNull(source, "list positions are mandatory");
        return RedisListCommands.Position.AFTER.equals(source) ? AFTER : BEFORE;
    }

    public static List<String> toStringList(String source) {
        return Collections.singletonList(source);
    }

    public static Converter<byte[], List<RedisClientInfo>> replyToListOfRedisClientInfo() {
        return BYTEARRAY_T0_LIST_OF_CLIENT_INFO;
    }

    public static Boolean toBoolean(IntegerReply reply) {
        return INTEGER_REPLY_TO_BOOLEAN.convert2(reply);
    }

    public static Converter<Exception, DataAccessException> exceptionConverter() {
        return EXCEPTION_CONVERTER;
    }

    public static byte[] boundaryToBytesForZRange(RedisZSetCommands.Range.Boundary boundary, byte[] defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return defaultValue;
        }
        return boundaryToBytes(boundary, new byte[0], "(".getBytes(Charsets.UTF_8));
    }

    public static byte[] toBytes(String source) {
        return source.getBytes(Charsets.UTF_8);
    }

    private static byte[] boundaryToBytes(RedisZSetCommands.Range.Boundary boundary, byte[] inclPrefix, byte[] exclPrefix) {
        byte[] value;
        byte[] prefix = boundary.isIncluding() ? inclPrefix : exclPrefix;
        if (boundary.getValue() instanceof byte[]) {
            value = (byte[]) boundary.getValue();
        } else {
            value = toBytes(boundary.getValue().toString());
        }
        ByteBuffer buffer = ByteBuffer.allocate(prefix.length + value.length);
        buffer.put(prefix);
        buffer.put(value);
        return buffer.array();
    }
}
