package org.springframework.data.redis.connection.lettuce;

import com.lambdaworks.redis.GeoArgs;
import com.lambdaworks.redis.GeoCoordinates;
import com.lambdaworks.redis.GeoWithin;
import com.lambdaworks.redis.KeyValue;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.ScoredValue;
import com.lambdaworks.redis.ScriptOutputType;
import com.lambdaworks.redis.SetArgs;
import com.lambdaworks.redis.SortArgs;
import com.lambdaworks.redis.cluster.models.partitions.Partitions;
import com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode;
import com.lambdaworks.redis.protocol.LettuceCharsets;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.PropertyAccessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.DefaultTuple;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.convert.LongToBooleanConverter;
import org.springframework.data.redis.connection.convert.StringToRedisClientInfoConverter;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters.class */
public abstract class LettuceConverters extends Converters {
    private static final Converter<Exception, DataAccessException> EXCEPTION_CONVERTER = new LettuceExceptionConverter();
    private static final Converter<Long, Boolean> LONG_TO_BOOLEAN = new LongToBooleanConverter();
    private static final Converter<String[], List<RedisClientInfo>> STRING_TO_LIST_OF_CLIENT_INFO = new StringToRedisClientInfoConverter();
    private static final Converter<Date, Long> DATE_TO_LONG = new Converter<Date, Long>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.1
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Long convert2(Date source) {
            if (source != null) {
                return Long.valueOf(source.getTime());
            }
            return null;
        }
    };
    private static final Converter<List<byte[]>, Set<byte[]>> BYTES_LIST_TO_BYTES_SET = new Converter<List<byte[]>, Set<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.2
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Set<byte[]> convert2(List<byte[]> results) {
            if (results != null) {
                return new LinkedHashSet(results);
            }
            return null;
        }
    };
    private static final Converter<byte[], String> BYTES_TO_STRING = new Converter<byte[], String>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.3
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public String convert2(byte[] source) {
            if (source == null || Arrays.equals(source, new byte[0])) {
                return null;
            }
            return new String(source);
        }
    };
    private static final Converter<String, byte[]> STRING_TO_BYTES = new Converter<String, byte[]>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.4
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public byte[] convert2(String source) {
            if (source == null) {
                return null;
            }
            return source.getBytes();
        }
    };
    private static final Converter<Set<byte[]>, List<byte[]>> BYTES_SET_TO_BYTES_LIST = new Converter<Set<byte[]>, List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.5
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<byte[]> convert2(Set<byte[]> results) {
            if (results != null) {
                return new ArrayList(results);
            }
            return null;
        }
    };
    private static final Converter<Collection<byte[]>, List<byte[]>> BYTES_COLLECTION_TO_BYTES_LIST = new Converter<Collection<byte[]>, List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.6
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<byte[]> convert2(Collection<byte[]> results) {
            if (results instanceof List) {
                return (List) results;
            }
            if (results != null) {
                return new ArrayList(results);
            }
            return null;
        }
    };
    private static final Converter<KeyValue<byte[], byte[]>, List<byte[]>> KEY_VALUE_TO_BYTES_LIST = new Converter<KeyValue<byte[], byte[]>, List<byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.7
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<byte[]> convert2(KeyValue<byte[], byte[]> source) {
            if (source == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList(2);
            arrayList.add(source.key);
            arrayList.add(source.value);
            return arrayList;
        }
    };
    private static final Converter<List<byte[]>, Map<byte[], byte[]>> BYTES_LIST_TO_MAP = new Converter<List<byte[]>, Map<byte[], byte[]>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.8
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Map<byte[], byte[]> convert2(List<byte[]> source) {
            if (CollectionUtils.isEmpty(source)) {
                return Collections.emptyMap();
            }
            Map<byte[], byte[]> target = new LinkedHashMap<>();
            Iterator<byte[]> kv = source.iterator();
            while (kv.hasNext()) {
                target.put(kv.next(), kv.hasNext() ? kv.next() : null);
            }
            return target;
        }
    };
    private static final Converter<List<ScoredValue<byte[]>>, Set<RedisZSetCommands.Tuple>> SCORED_VALUES_TO_TUPLE_SET = new Converter<List<ScoredValue<byte[]>>, Set<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.9
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Set<RedisZSetCommands.Tuple> convert2(List<ScoredValue<byte[]>> source) {
            if (source == null) {
                return null;
            }
            Set<RedisZSetCommands.Tuple> tuples = new LinkedHashSet<>(source.size());
            for (ScoredValue<byte[]> value : source) {
                tuples.add(LettuceConverters.toTuple(value));
            }
            return tuples;
        }
    };
    private static final Converter<List<ScoredValue<byte[]>>, List<RedisZSetCommands.Tuple>> SCORED_VALUES_TO_TUPLE_LIST = new Converter<List<ScoredValue<byte[]>>, List<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.10
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<RedisZSetCommands.Tuple> convert2(List<ScoredValue<byte[]>> source) {
            if (source == null) {
                return null;
            }
            List<RedisZSetCommands.Tuple> tuples = new ArrayList<>(source.size());
            for (ScoredValue<byte[]> value : source) {
                tuples.add(LettuceConverters.toTuple(value));
            }
            return tuples;
        }
    };
    private static final Converter<ScoredValue<byte[]>, RedisZSetCommands.Tuple> SCORED_VALUE_TO_TUPLE = new Converter<ScoredValue<byte[]>, RedisZSetCommands.Tuple>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.11
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public RedisZSetCommands.Tuple convert2(ScoredValue<byte[]> source) {
            if (source != null) {
                return new DefaultTuple((byte[]) source.value, Double.valueOf(source.score));
            }
            return null;
        }
    };
    private static final Converter<List<byte[]>, List<RedisZSetCommands.Tuple>> BYTES_LIST_TO_TUPLE_LIST_CONVERTER = new Converter<List<byte[]>, List<RedisZSetCommands.Tuple>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.12
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<RedisZSetCommands.Tuple> convert2(List<byte[]> source) {
            if (CollectionUtils.isEmpty(source)) {
                return Collections.emptyList();
            }
            List<RedisZSetCommands.Tuple> tuples = new ArrayList<>();
            Iterator<byte[]> it = source.iterator();
            while (it.hasNext()) {
                tuples.add(new DefaultTuple(it.next(), it.hasNext() ? Double.valueOf(LettuceConverters.toString(it.next())) : null));
            }
            return tuples;
        }
    };
    private static final Converter<Partitions, List<RedisClusterNode>> PARTITIONS_TO_CLUSTER_NODES = new Converter<Partitions, List<RedisClusterNode>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.13
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public List<RedisClusterNode> convert2(Partitions source) {
            if (source == null) {
                return Collections.emptyList();
            }
            ArrayList arrayList = new ArrayList();
            for (com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode node : source.getPartitions()) {
                arrayList.add(LettuceConverters.CLUSTER_NODE_TO_CLUSTER_NODE_CONVERTER.convert2(node));
            }
            return arrayList;
        }
    };
    private static Converter<com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode, RedisClusterNode> CLUSTER_NODE_TO_CLUSTER_NODE_CONVERTER = new Converter<com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode, RedisClusterNode>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.14
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public RedisClusterNode convert2(com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode source) {
            Set<RedisClusterNode.Flag> flags = parseFlags(source.getFlags());
            return RedisClusterNode.newRedisClusterNode().listeningAt(source.getUri().getHost(), source.getUri().getPort()).withId(source.getNodeId()).promotedAs(flags.contains(RedisClusterNode.Flag.MASTER) ? RedisNode.NodeType.MASTER : RedisNode.NodeType.SLAVE).serving(new RedisClusterNode.SlotRange(source.getSlots())).withFlags(flags).linkState(source.isConnected() ? RedisClusterNode.LinkState.CONNECTED : RedisClusterNode.LinkState.DISCONNECTED).slaveOf(source.getSlaveOf()).build();
        }

        private Set<RedisClusterNode.Flag> parseFlags(Set<RedisClusterNode.NodeFlag> source) {
            Set<RedisClusterNode.Flag> flags = new LinkedHashSet<>(source != null ? source.size() : 8, 1.0f);
            for (RedisClusterNode.NodeFlag flag : source) {
                switch (AnonymousClass19.$SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[flag.ordinal()]) {
                    case 1:
                        flags.add(RedisClusterNode.Flag.NOFLAGS);
                        break;
                    case 2:
                        flags.add(RedisClusterNode.Flag.PFAIL);
                        break;
                    case 3:
                        flags.add(RedisClusterNode.Flag.FAIL);
                        break;
                    case 4:
                        flags.add(RedisClusterNode.Flag.HANDSHAKE);
                        break;
                    case 5:
                        flags.add(RedisClusterNode.Flag.MASTER);
                        break;
                    case 6:
                        flags.add(RedisClusterNode.Flag.MYSELF);
                        break;
                    case 7:
                        flags.add(RedisClusterNode.Flag.NOADDR);
                        break;
                    case 8:
                        flags.add(RedisClusterNode.Flag.SLAVE);
                        break;
                }
            }
            return flags;
        }
    };
    public static final byte[] PLUS_BYTES = toBytes("+");
    public static final byte[] MINUS_BYTES = toBytes("-");
    public static final byte[] POSITIVE_INFINITY_BYTES = toBytes("+inf");
    public static final byte[] NEGATIVE_INFINITY_BYTES = toBytes("-inf");
    private static final Converter<List<byte[]>, Long> BYTES_LIST_TO_TIME_CONVERTER = new Converter<List<byte[]>, Long>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.15
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Long convert2(List<byte[]> source) {
            Assert.notEmpty(source, "Received invalid result from server. Expected 2 items in collection.");
            Assert.isTrue(source.size() == 2, "Received invalid nr of arguments from redis server. Expected 2 received " + source.size());
            return Converters.toTimeMillis(LettuceConverters.toString(source.get(0)), LettuceConverters.toString(source.get(1)));
        }
    };
    private static final Converter<GeoCoordinates, Point> GEO_COORDINATE_TO_POINT_CONVERTER = new Converter<GeoCoordinates, Point>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.16
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Point convert2(GeoCoordinates geoCoordinate) {
            if (geoCoordinate != null) {
                return new Point(geoCoordinate.x.doubleValue(), geoCoordinate.y.doubleValue());
            }
            return null;
        }
    };
    private static final ListConverter<GeoCoordinates, Point> GEO_COORDINATE_LIST_TO_POINT_LIST_CONVERTER = new ListConverter<>(GEO_COORDINATE_TO_POINT_CONVERTER);

    public static List<RedisZSetCommands.Tuple> toTuple(List<byte[]> list) {
        return BYTES_LIST_TO_TUPLE_LIST_CONVERTER.convert2(list);
    }

    public static Converter<List<byte[]>, List<RedisZSetCommands.Tuple>> bytesListToTupleListConverter() {
        return BYTES_LIST_TO_TUPLE_LIST_CONVERTER;
    }

    public static Converter<String, List<RedisClientInfo>> stringToRedisClientListConverter() {
        return new Converter<String, List<RedisClientInfo>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.17
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public List<RedisClientInfo> convert2(String source) {
                if (StringUtils.hasText(source)) {
                    return (List) LettuceConverters.STRING_TO_LIST_OF_CLIENT_INFO.convert2(source.split("\\r?\\n"));
                }
                return Collections.emptyList();
            }
        };
    }

    public static Converter<Date, Long> dateToLong() {
        return DATE_TO_LONG;
    }

    public static Converter<List<byte[]>, Set<byte[]>> bytesListToBytesSet() {
        return BYTES_LIST_TO_BYTES_SET;
    }

    public static Converter<byte[], String> bytesToString() {
        return BYTES_TO_STRING;
    }

    public static Converter<KeyValue<byte[], byte[]>, List<byte[]>> keyValueToBytesList() {
        return KEY_VALUE_TO_BYTES_LIST;
    }

    public static Converter<Collection<byte[]>, List<byte[]>> bytesSetToBytesList() {
        return BYTES_COLLECTION_TO_BYTES_LIST;
    }

    public static Converter<Collection<byte[]>, List<byte[]>> bytesCollectionToBytesList() {
        return BYTES_COLLECTION_TO_BYTES_LIST;
    }

    public static Converter<List<ScoredValue<byte[]>>, Set<RedisZSetCommands.Tuple>> scoredValuesToTupleSet() {
        return SCORED_VALUES_TO_TUPLE_SET;
    }

    public static Converter<List<ScoredValue<byte[]>>, List<RedisZSetCommands.Tuple>> scoredValuesToTupleList() {
        return SCORED_VALUES_TO_TUPLE_LIST;
    }

    public static Converter<ScoredValue<byte[]>, RedisZSetCommands.Tuple> scoredValueToTuple() {
        return SCORED_VALUE_TO_TUPLE;
    }

    public static Converter<Exception, DataAccessException> exceptionConverter() {
        return EXCEPTION_CONVERTER;
    }

    public static Converter<Long, Boolean> longToBooleanConverter() {
        return LONG_TO_BOOLEAN;
    }

    public static Long toLong(Date source) {
        return DATE_TO_LONG.convert2(source);
    }

    public static Set<byte[]> toBytesSet(List<byte[]> source) {
        return BYTES_LIST_TO_BYTES_SET.convert2(source);
    }

    public static List<byte[]> toBytesList(KeyValue<byte[], byte[]> source) {
        return KEY_VALUE_TO_BYTES_LIST.convert2(source);
    }

    public static List<byte[]> toBytesList(Collection<byte[]> source) {
        return BYTES_COLLECTION_TO_BYTES_LIST.convert2(source);
    }

    public static Set<RedisZSetCommands.Tuple> toTupleSet(List<ScoredValue<byte[]>> source) {
        return SCORED_VALUES_TO_TUPLE_SET.convert2(source);
    }

    public static RedisZSetCommands.Tuple toTuple(ScoredValue<byte[]> source) {
        return SCORED_VALUE_TO_TUPLE.convert2(source);
    }

    public static String toString(byte[] source) {
        return BYTES_TO_STRING.convert2(source);
    }

    public static ScriptOutputType toScriptOutputType(ReturnType returnType) {
        switch (returnType) {
            case BOOLEAN:
                return ScriptOutputType.BOOLEAN;
            case MULTI:
                return ScriptOutputType.MULTI;
            case VALUE:
                return ScriptOutputType.VALUE;
            case INTEGER:
                return ScriptOutputType.INTEGER;
            case STATUS:
                return ScriptOutputType.STATUS;
            default:
                throw new IllegalArgumentException("Return type " + returnType + " is not a supported script output type");
        }
    }

    public static boolean toBoolean(RedisListCommands.Position where) {
        Assert.notNull(where, "list positions are mandatory");
        return !RedisListCommands.Position.AFTER.equals(where);
    }

    public static int toInt(boolean value) {
        return value ? 1 : 0;
    }

    public static Map<byte[], byte[]> toMap(List<byte[]> source) {
        return BYTES_LIST_TO_MAP.convert2(source);
    }

    public static Converter<List<byte[]>, Map<byte[], byte[]>> bytesListToMapConverter() {
        return BYTES_LIST_TO_MAP;
    }

    public static SortArgs toSortArgs(SortParameters params) {
        SortArgs args = new SortArgs();
        if (params == null) {
            return args;
        }
        if (params.getByPattern() != null) {
            args.by(new String(params.getByPattern(), LettuceCharsets.ASCII));
        }
        if (params.getLimit() != null) {
            args.limit(params.getLimit().getStart(), params.getLimit().getCount());
        }
        if (params.getGetPattern() != null) {
            byte[][] pattern = params.getGetPattern();
            for (byte[] bs : pattern) {
                args.get(new String(bs, LettuceCharsets.ASCII));
            }
        }
        if (params.getOrder() != null) {
            if (params.getOrder() == SortParameters.Order.ASC) {
                args.asc();
            } else {
                args.desc();
            }
        }
        Boolean isAlpha = params.isAlphabetic();
        if (isAlpha != null && isAlpha.booleanValue()) {
            args.alpha();
        }
        return args;
    }

    public static List<RedisClientInfo> toListOfRedisClientInformation(String clientList) {
        return stringToRedisClientListConverter().convert2(clientList);
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [byte[], byte[][], java.lang.Object] */
    public static byte[][] subarray(byte[][] input, int index) {
        if (input.length > index) {
            ?? r0 = new byte[input.length - index];
            System.arraycopy(input, index, r0, 0, r0.length);
            return r0;
        }
        return (byte[][]) null;
    }

    public static String boundaryToStringForZRange(RedisZSetCommands.Range.Boundary boundary, String defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return defaultValue;
        }
        return boundaryToString(boundary, "", "(");
    }

    private static String boundaryToString(RedisZSetCommands.Range.Boundary boundary, String inclPrefix, String exclPrefix) {
        String value;
        String prefix = boundary.isIncluding() ? inclPrefix : exclPrefix;
        if (boundary.getValue() instanceof byte[]) {
            value = toString((byte[]) boundary.getValue());
        } else {
            value = boundary.getValue().toString();
        }
        return prefix + value;
    }

    public static List<RedisServer> toListOfRedisServer(List<Map<String, String>> source) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<RedisServer> sentinels = new ArrayList<>();
        for (Map<String, String> info : source) {
            sentinels.add(RedisServer.newServerFrom(Converters.toProperties(info)));
        }
        return sentinels;
    }

    public static RedisURI sentinelConfigurationToRedisURI(RedisSentinelConfiguration sentinelConfiguration) {
        Assert.notNull(sentinelConfiguration, "RedisSentinelConfiguration is required");
        Set<RedisNode> sentinels = sentinelConfiguration.getSentinels();
        RedisURI.Builder builder = null;
        for (RedisNode sentinel : sentinels) {
            if (builder == null) {
                builder = RedisURI.Builder.sentinel(sentinel.getHost(), sentinel.getPort().intValue(), sentinelConfiguration.getMaster().getName());
            } else {
                builder.withSentinel(sentinel.getHost(), sentinel.getPort().intValue());
            }
        }
        return builder.build();
    }

    public static byte[] toBytes(String source) {
        return STRING_TO_BYTES.convert2(source);
    }

    public static byte[] toBytes(Integer source) {
        return String.valueOf(source).getBytes();
    }

    public static byte[] toBytes(Long source) {
        return String.valueOf(source).getBytes();
    }

    public static byte[] toBytes(Double source) {
        return toBytes(String.valueOf(source));
    }

    public static String boundaryToBytesForZRange(RedisZSetCommands.Range.Boundary boundary, byte[] defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return toString(defaultValue);
        }
        return boundaryToBytes(boundary, new byte[0], toBytes("("));
    }

    public static String boundaryToBytesForZRangeByLex(RedisZSetCommands.Range.Boundary boundary, byte[] defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return toString(defaultValue);
        }
        return boundaryToBytes(boundary, toBytes(PropertyAccessor.PROPERTY_KEY_PREFIX), toBytes("("));
    }

    private static String boundaryToBytes(RedisZSetCommands.Range.Boundary boundary, byte[] inclPrefix, byte[] exclPrefix) {
        byte[] value;
        byte[] prefix = boundary.isIncluding() ? inclPrefix : exclPrefix;
        if (boundary.getValue() instanceof byte[]) {
            value = (byte[]) boundary.getValue();
        } else if (boundary.getValue() instanceof Double) {
            value = toBytes((Double) boundary.getValue());
        } else if (boundary.getValue() instanceof Long) {
            value = toBytes((Long) boundary.getValue());
        } else if (boundary.getValue() instanceof Integer) {
            value = toBytes((Integer) boundary.getValue());
        } else if (boundary.getValue() instanceof String) {
            value = toBytes((String) boundary.getValue());
        } else {
            throw new IllegalArgumentException(String.format("Cannot convert %s to binary format", boundary.getValue()));
        }
        ByteBuffer buffer = ByteBuffer.allocate(prefix.length + value.length);
        buffer.put(prefix);
        buffer.put(value);
        return toString(buffer.array());
    }

    public static List<org.springframework.data.redis.connection.RedisClusterNode> partitionsToClusterNodes(Partitions partitions) {
        return PARTITIONS_TO_CLUSTER_NODES.convert2(partitions);
    }

    public static org.springframework.data.redis.connection.RedisClusterNode toRedisClusterNode(com.lambdaworks.redis.cluster.models.partitions.RedisClusterNode source) {
        return CLUSTER_NODE_TO_CLUSTER_NODE_CONVERTER.convert2(source);
    }

    public static SetArgs toSetArgs(Expiration expiration, RedisStringCommands.SetOption option) {
        SetArgs args = new SetArgs();
        if (expiration != null && !expiration.isPersistent()) {
            switch (AnonymousClass19.$SwitchMap$java$util$concurrent$TimeUnit[expiration.getTimeUnit().ordinal()]) {
                case 1:
                    args.ex(expiration.getExpirationTime());
                    break;
                default:
                    args.px(expiration.getConverted(TimeUnit.MILLISECONDS));
                    break;
            }
        }
        if (option != null) {
            switch (option) {
                case SET_IF_ABSENT:
                    args.nx();
                    break;
                case SET_IF_PRESENT:
                    args.xx();
                    break;
            }
        }
        return args;
    }

    static Converter<List<byte[]>, Long> toTimeConverter() {
        return BYTES_LIST_TO_TIME_CONVERTER;
    }

    public static GeoArgs.Unit toGeoArgsUnit(Metric metric) {
        Metric metricToUse = (metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric;
        return ObjectUtils.caseInsensitiveValueOf(GeoArgs.Unit.values(), metricToUse.getAbbreviation());
    }

    public static GeoArgs toGeoArgs(RedisGeoCommands.GeoRadiusCommandArgs args) {
        GeoArgs geoArgs = new GeoArgs();
        if (args.hasFlags()) {
            for (RedisGeoCommands.GeoRadiusCommandArgs.Flag flag : args.getFlags()) {
                switch (flag) {
                    case WITHCOORD:
                        geoArgs.withCoordinates();
                        break;
                    case WITHDIST:
                        geoArgs.withDistance();
                        break;
                }
            }
        }
        if (args.hasSortDirection()) {
            switch (args.getSortDirection()) {
                case ASC:
                    geoArgs.asc();
                    break;
                case DESC:
                    geoArgs.desc();
                    break;
            }
        }
        if (args.hasLimit()) {
            geoArgs.withCount(args.getLimit().longValue());
        }
        return geoArgs;
    }

    /* renamed from: org.springframework.data.redis.connection.lettuce.LettuceConverters$19, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters$19.class */
    static /* synthetic */ class AnonymousClass19 {
        static final /* synthetic */ int[] $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag;
        static final /* synthetic */ int[] $SwitchMap$java$util$concurrent$TimeUnit;

        static {
            try {
                $SwitchMap$org$springframework$data$domain$Sort$Direction[Sort.Direction.ASC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$springframework$data$domain$Sort$Direction[Sort.Direction.DESC.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SwitchMap$org$springframework$data$redis$connection$RedisGeoCommands$GeoRadiusCommandArgs$Flag = new int[RedisGeoCommands.GeoRadiusCommandArgs.Flag.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisGeoCommands$GeoRadiusCommandArgs$Flag[RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHCOORD.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisGeoCommands$GeoRadiusCommandArgs$Flag[RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHDIST.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption = new int[RedisStringCommands.SetOption.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[RedisStringCommands.SetOption.SET_IF_ABSENT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[RedisStringCommands.SetOption.SET_IF_PRESENT.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            $SwitchMap$java$util$concurrent$TimeUnit = new int[TimeUnit.values().length];
            try {
                $SwitchMap$java$util$concurrent$TimeUnit[TimeUnit.SECONDS.ordinal()] = 1;
            } catch (NoSuchFieldError e7) {
            }
            $SwitchMap$org$springframework$data$redis$connection$ReturnType = new int[ReturnType.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$ReturnType[ReturnType.BOOLEAN.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$ReturnType[ReturnType.MULTI.ordinal()] = 2;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$ReturnType[ReturnType.VALUE.ordinal()] = 3;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$ReturnType[ReturnType.INTEGER.ordinal()] = 4;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$ReturnType[ReturnType.STATUS.ordinal()] = 5;
            } catch (NoSuchFieldError e12) {
            }
            $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag = new int[RedisClusterNode.NodeFlag.values().length];
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.NOFLAGS.ordinal()] = 1;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.EVENTUAL_FAIL.ordinal()] = 2;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.FAIL.ordinal()] = 3;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.HANDSHAKE.ordinal()] = 4;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.MASTER.ordinal()] = 5;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.MYSELF.ordinal()] = 6;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.NOADDR.ordinal()] = 7;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$com$lambdaworks$redis$cluster$models$partitions$RedisClusterNode$NodeFlag[RedisClusterNode.NodeFlag.SLAVE.ordinal()] = 8;
            } catch (NoSuchFieldError e20) {
            }
        }
    }

    public static Converter<Set<byte[]>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> bytesSetToGeoResultsConverter() {
        return new Converter<Set<byte[]>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>>() { // from class: org.springframework.data.redis.connection.lettuce.LettuceConverters.18
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> convert2(Set<byte[]> source) {
                if (CollectionUtils.isEmpty(source)) {
                    return new GeoResults<>(Collections.emptyList());
                }
                List<GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> results = new ArrayList<>(source.size());
                Iterator<byte[]> it = source.iterator();
                while (it.hasNext()) {
                    results.add(new GeoResult<>(new RedisGeoCommands.GeoLocation(it.next(), null), new Distance(0.0d)));
                }
                return new GeoResults<>(results);
            }
        };
    }

    public static Converter<List<GeoWithin<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> geoRadiusResponseToGeoResultsConverter(Metric metric) {
        return GeoResultsConverterFactory.INSTANCE.forMetric(metric);
    }

    public static ListConverter<GeoCoordinates, Point> geoCoordinatesToPointConverter() {
        return GEO_COORDINATE_LIST_TO_POINT_LIST_CONVERTER;
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters$GeoResultsConverterFactory.class */
    enum GeoResultsConverterFactory {
        INSTANCE;

        Converter<List<GeoWithin<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new GeoResultsConverter((metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters$GeoResultsConverterFactory$GeoResultsConverter.class */
        private static class GeoResultsConverter implements Converter<List<GeoWithin<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultsConverter(Metric metric) {
                this.metric = metric;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> convert2(List<GeoWithin<byte[]>> source) {
                List<GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> results = new ArrayList<>(source.size());
                Converter<GeoWithin<byte[]>, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> converter = GeoResultConverterFactory.INSTANCE.forMetric(this.metric);
                for (GeoWithin<byte[]> result : source) {
                    results.add(converter.convert2(result));
                }
                return new GeoResults<>(results, this.metric);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters$GeoResultConverterFactory.class */
    enum GeoResultConverterFactory {
        INSTANCE;

        Converter<GeoWithin<byte[]>, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new GeoResultConverter(metric);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/lettuce/LettuceConverters$GeoResultConverterFactory$GeoResultConverter.class */
        private static class GeoResultConverter implements Converter<GeoWithin<byte[]>, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultConverter(Metric metric) {
                this.metric = metric;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public GeoResult<RedisGeoCommands.GeoLocation<byte[]>> convert2(GeoWithin<byte[]> source) {
                Point point = (Point) LettuceConverters.GEO_COORDINATE_TO_POINT_CONVERTER.convert2(source.coordinates);
                return new GeoResult<>(new RedisGeoCommands.GeoLocation(source.member, point), new Distance(source.distance != null ? source.distance.doubleValue() : 0.0d, this.metric));
            }
        }
    }
}
