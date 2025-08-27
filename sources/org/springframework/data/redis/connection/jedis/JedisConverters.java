package org.springframework.data.redis.connection.jedis;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.data.redis.connection.convert.Converters;
import org.springframework.data.redis.connection.convert.ListConverter;
import org.springframework.data.redis.connection.convert.MapConverter;
import org.springframework.data.redis.connection.convert.SetConverter;
import org.springframework.data.redis.connection.convert.StringToRedisClientInfoConverter;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.util.SafeEncoder;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters.class */
public abstract class JedisConverters extends Converters {
    private static final Converter<Exception, DataAccessException> EXCEPTION_CONVERTER = new JedisExceptionConverter();
    private static final Converter<String[], List<RedisClientInfo>> STRING_TO_CLIENT_INFO_CONVERTER = new StringToRedisClientInfoConverter();
    private static final Converter<byte[], String> BYTES_TO_STRING_CONVERTER = new Converter<byte[], String>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.1
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public String convert2(byte[] source) {
            if (source == null) {
                return null;
            }
            return SafeEncoder.encode(source);
        }
    };
    private static final ListConverter<byte[], String> BYTES_LIST_TO_STRING_LIST_CONVERTER = new ListConverter<>(BYTES_TO_STRING_CONVERTER);
    private static final Converter<String, byte[]> STRING_TO_BYTES = new Converter<String, byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.2
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public byte[] convert2(String source) {
            if (source == null) {
                return null;
            }
            return SafeEncoder.encode(source);
        }
    };
    private static final ListConverter<String, byte[]> STRING_LIST_TO_BYTE_LIST = new ListConverter<>(STRING_TO_BYTES);
    private static final SetConverter<String, byte[]> STRING_SET_TO_BYTE_SET = new SetConverter<>(STRING_TO_BYTES);
    private static final MapConverter<String, byte[]> STRING_MAP_TO_BYTE_MAP = new MapConverter<>(STRING_TO_BYTES);
    private static final Converter<Tuple, RedisZSetCommands.Tuple> TUPLE_CONVERTER = new Converter<Tuple, RedisZSetCommands.Tuple>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.3
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public RedisZSetCommands.Tuple convert2(Tuple source) {
            if (source != null) {
                return new DefaultTuple(source.getBinaryElement(), Double.valueOf(source.getScore()));
            }
            return null;
        }
    };
    private static final SetConverter<Tuple, RedisZSetCommands.Tuple> TUPLE_SET_TO_TUPLE_SET = new SetConverter<>(TUPLE_CONVERTER);
    private static final ListConverter<Tuple, RedisZSetCommands.Tuple> TUPLE_LIST_TO_TUPLE_LIST_CONVERTER = new ListConverter<>(TUPLE_CONVERTER);
    public static final byte[] PLUS_BYTES = toBytes("+");
    public static final byte[] MINUS_BYTES = toBytes("-");
    public static final byte[] POSITIVE_INFINITY_BYTES = toBytes("+inf");
    public static final byte[] NEGATIVE_INFINITY_BYTES = toBytes("-inf");
    private static final Converter<Object, RedisClusterNode> OBJECT_TO_CLUSTER_NODE_CONVERTER = new Converter<Object, RedisClusterNode>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.4
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert */
        public RedisClusterNode convert2(Object infos) {
            List<Object> values = (List) infos;
            RedisClusterNode.SlotRange range = new RedisClusterNode.SlotRange(Integer.valueOf(((Number) values.get(0)).intValue()), Integer.valueOf(((Number) values.get(1)).intValue()));
            List<Object> nodeInfo = (List) values.get(2);
            return new RedisClusterNode(JedisConverters.toString((byte[]) nodeInfo.get(0)), ((Number) nodeInfo.get(1)).intValue(), range);
        }
    };
    private static final byte[] EX = toBytes("EX");
    private static final byte[] PX = toBytes("PX");
    private static final Converter<Expiration, byte[]> EXPIRATION_TO_COMMAND_OPTION_CONVERTER = new Converter<Expiration, byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.5
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public byte[] convert2(Expiration source) {
            if (source == null || source.isPersistent()) {
                return new byte[0];
            }
            return ObjectUtils.nullSafeEquals(TimeUnit.MILLISECONDS, source.getTimeUnit()) ? JedisConverters.PX : JedisConverters.EX;
        }
    };
    private static final byte[] NX = toBytes("NX");
    private static final byte[] XX = toBytes("XX");
    private static final Converter<RedisStringCommands.SetOption, byte[]> SET_OPTION_TO_COMMAND_OPTION_CONVERTER = new Converter<RedisStringCommands.SetOption, byte[]>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.6
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public byte[] convert2(RedisStringCommands.SetOption source) {
            switch (AnonymousClass9.$SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[source.ordinal()]) {
                case 1:
                    return new byte[0];
                case 2:
                    return JedisConverters.NX;
                case 3:
                    return JedisConverters.XX;
                default:
                    throw new IllegalArgumentException(String.format("Invalid argument %s for SetOption.", source));
            }
        }
    };
    private static final Converter<List<String>, Long> STRING_LIST_TO_TIME_CONVERTER = new Converter<List<String>, Long>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.7
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Long convert2(List<String> source) {
            Assert.notEmpty(source, "Received invalid result from server. Expected 2 items in collection.");
            Assert.isTrue(source.size() == 2, "Received invalid nr of arguments from redis server. Expected 2 received " + source.size());
            return Converters.toTimeMillis(source.get(0), source.get(1));
        }
    };
    private static final Converter<GeoCoordinate, Point> GEO_COORDINATE_TO_POINT_CONVERTER = new Converter<GeoCoordinate, Point>() { // from class: org.springframework.data.redis.connection.jedis.JedisConverters.8
        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public Point convert2(GeoCoordinate geoCoordinate) {
            if (geoCoordinate != null) {
                return new Point(geoCoordinate.getLongitude(), geoCoordinate.getLatitude());
            }
            return null;
        }
    };
    private static final ListConverter<GeoCoordinate, Point> LIST_GEO_COORDINATE_TO_POINT_CONVERTER = new ListConverter<>(GEO_COORDINATE_TO_POINT_CONVERTER);

    public static Converter<String, byte[]> stringToBytes() {
        return STRING_TO_BYTES;
    }

    public static ListConverter<Tuple, RedisZSetCommands.Tuple> tuplesToTuples() {
        return TUPLE_LIST_TO_TUPLE_LIST_CONVERTER;
    }

    public static ListConverter<String, byte[]> stringListToByteList() {
        return STRING_LIST_TO_BYTE_LIST;
    }

    public static SetConverter<String, byte[]> stringSetToByteSet() {
        return STRING_SET_TO_BYTE_SET;
    }

    public static MapConverter<String, byte[]> stringMapToByteMap() {
        return STRING_MAP_TO_BYTE_MAP;
    }

    public static SetConverter<Tuple, RedisZSetCommands.Tuple> tupleSetToTupleSet() {
        return TUPLE_SET_TO_TUPLE_SET;
    }

    public static Converter<Exception, DataAccessException> exceptionConverter() {
        return EXCEPTION_CONVERTER;
    }

    public static String[] toStrings(byte[][] source) {
        String[] result = new String[source.length];
        for (int i = 0; i < source.length; i++) {
            result[i] = SafeEncoder.encode(source[i]);
        }
        return result;
    }

    public static Set<RedisZSetCommands.Tuple> toTupleSet(Set<Tuple> source) {
        return TUPLE_SET_TO_TUPLE_SET.convert(source);
    }

    public static Map<byte[], Double> toTupleMap(Set<RedisZSetCommands.Tuple> tuples) {
        Assert.notNull(tuples, "Tuple set must not be null!");
        Map<byte[], Double> args = new LinkedHashMap<>(tuples.size(), 1.0f);
        Set<Double> scores = new HashSet<>(tuples.size(), 1.0f);
        boolean isAtLeastJedis24 = JedisVersionUtil.atLeastJedis24();
        for (RedisZSetCommands.Tuple tuple : tuples) {
            if (!isAtLeastJedis24) {
                if (scores.contains(tuple.getScore())) {
                    throw new UnsupportedOperationException("Bulk add of multiple elements with the same score is not supported. Add the elements individually.");
                }
                scores.add(tuple.getScore());
            }
            args.put(tuple.getValue(), tuple.getScore());
        }
        return args;
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

    public static byte[] toBytes(String source) {
        return STRING_TO_BYTES.convert2(source);
    }

    public static String toString(byte[] source) {
        if (source == null) {
            return null;
        }
        return SafeEncoder.encode(source);
    }

    public static RedisClusterNode toNode(Object source) {
        return OBJECT_TO_CLUSTER_NODE_CONVERTER.convert2(source);
    }

    public static List<RedisClientInfo> toListOfRedisClientInformation(String source) {
        if (!StringUtils.hasText(source)) {
            return Collections.emptyList();
        }
        return STRING_TO_CLIENT_INFO_CONVERTER.convert2(source.split("\\r?\\n"));
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

    public static DataAccessException toDataAccessException(Exception ex) {
        return EXCEPTION_CONVERTER.convert2(ex);
    }

    public static BinaryClient.LIST_POSITION toListPosition(RedisListCommands.Position source) {
        Assert.notNull(source, "list positions are mandatory");
        return RedisListCommands.Position.AFTER.equals(source) ? BinaryClient.LIST_POSITION.AFTER : BinaryClient.LIST_POSITION.BEFORE;
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

    public static SortingParams toSortingParams(SortParameters params) {
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

    public static BitOP toBitOp(RedisStringCommands.BitOperation bitOp) {
        switch (bitOp) {
            case AND:
                return BitOP.AND;
            case OR:
                return BitOP.OR;
            case NOT:
                return BitOP.NOT;
            case XOR:
                return BitOP.XOR;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static byte[] boundaryToBytesForZRange(RedisZSetCommands.Range.Boundary boundary, byte[] defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return defaultValue;
        }
        return boundaryToBytes(boundary, new byte[0], toBytes("("));
    }

    public static byte[] boundaryToBytesForZRangeByLex(RedisZSetCommands.Range.Boundary boundary, byte[] defaultValue) {
        if (boundary == null || boundary.getValue() == null) {
            return defaultValue;
        }
        return boundaryToBytes(boundary, toBytes(PropertyAccessor.PROPERTY_KEY_PREFIX), toBytes("("));
    }

    public static byte[] toSetCommandExPxArgument(Expiration expiration) {
        return EXPIRATION_TO_COMMAND_OPTION_CONVERTER.convert2(expiration);
    }

    public static byte[] toSetCommandNxXxArgument(RedisStringCommands.SetOption option) {
        return SET_OPTION_TO_COMMAND_OPTION_CONVERTER.convert2(option);
    }

    private static byte[] boundaryToBytes(RedisZSetCommands.Range.Boundary boundary, byte[] inclPrefix, byte[] exclPrefix) {
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
        return buffer.array();
    }

    public static ScanParams toScanParams(ScanOptions options) {
        ScanParams sp = new ScanParams();
        if (!options.equals(ScanOptions.NONE)) {
            if (options.getCount() != null) {
                sp.count(Integer.valueOf(options.getCount().intValue()));
            }
            if (StringUtils.hasText(options.getPattern())) {
                sp.match(options.getPattern());
            }
        }
        return sp;
    }

    static Converter<List<String>, Long> toTimeConverter() {
        return STRING_LIST_TO_TIME_CONVERTER;
    }

    public static List<String> toStrings(List<byte[]> source) {
        return BYTES_LIST_TO_STRING_LIST_CONVERTER.convert(source);
    }

    public static ListConverter<byte[], String> bytesListToStringListConverter() {
        return BYTES_LIST_TO_STRING_LIST_CONVERTER;
    }

    public static ListConverter<GeoCoordinate, Point> geoCoordinateToPointConverter() {
        return LIST_GEO_COORDINATE_TO_POINT_CONVERTER;
    }

    public static Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> geoRadiusResponseToGeoResultsConverter(Metric metric) {
        return GeoResultsConverterFactory.INSTANCE.forMetric(metric);
    }

    public static GeoUnit toGeoUnit(Metric metric) {
        Metric metricToUse = (metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric;
        return (GeoUnit) ObjectUtils.caseInsensitiveValueOf(GeoUnit.values(), metricToUse.getAbbreviation());
    }

    public static GeoCoordinate toGeoCoordinate(Point source) {
        if (source == null) {
            return null;
        }
        return new GeoCoordinate(source.getX(), source.getY());
    }

    public static GeoRadiusParam toGeoRadiusParam(RedisGeoCommands.GeoRadiusCommandArgs source) {
        GeoRadiusParam param = GeoRadiusParam.geoRadiusParam();
        if (source == null) {
            return param;
        }
        if (source.hasFlags()) {
            for (RedisGeoCommands.GeoRadiusCommandArgs.Flag flag : source.getFlags()) {
                switch (flag) {
                    case WITHCOORD:
                        param.withCoord();
                        break;
                    case WITHDIST:
                        param.withDist();
                        break;
                }
            }
        }
        if (source.hasSortDirection()) {
            switch (source.getSortDirection()) {
                case ASC:
                    param.sortAscending();
                    break;
                case DESC:
                    param.sortDescending();
                    break;
            }
        }
        if (source.hasLimit()) {
            param.count(source.getLimit().intValue());
        }
        return param;
    }

    /* renamed from: org.springframework.data.redis.connection.jedis.JedisConverters$9, reason: invalid class name */
    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters$9.class */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption;

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
            $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$BitOperation = new int[RedisStringCommands.BitOperation.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$BitOperation[RedisStringCommands.BitOperation.AND.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$BitOperation[RedisStringCommands.BitOperation.OR.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$BitOperation[RedisStringCommands.BitOperation.NOT.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$BitOperation[RedisStringCommands.BitOperation.XOR.ordinal()] = 4;
            } catch (NoSuchFieldError e8) {
            }
            $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption = new int[RedisStringCommands.SetOption.values().length];
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[RedisStringCommands.SetOption.UPSERT.ordinal()] = 1;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[RedisStringCommands.SetOption.SET_IF_ABSENT.ordinal()] = 2;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$org$springframework$data$redis$connection$RedisStringCommands$SetOption[RedisStringCommands.SetOption.SET_IF_PRESENT.ordinal()] = 3;
            } catch (NoSuchFieldError e11) {
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters$GeoResultsConverterFactory.class */
    enum GeoResultsConverterFactory {
        INSTANCE;

        Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new GeoResultsConverter((metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters$GeoResultsConverterFactory$GeoResultsConverter.class */
        private static class GeoResultsConverter implements Converter<List<GeoRadiusResponse>, GeoResults<RedisGeoCommands.GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultsConverter(Metric metric) {
                this.metric = metric;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public GeoResults<RedisGeoCommands.GeoLocation<byte[]>> convert2(List<GeoRadiusResponse> source) {
                List<GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> results = new ArrayList<>(source.size());
                Converter<GeoRadiusResponse, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> converter = GeoResultConverterFactory.INSTANCE.forMetric(this.metric);
                for (GeoRadiusResponse result : source) {
                    results.add(converter.convert2(result));
                }
                return new GeoResults<>(results, this.metric);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters$GeoResultConverterFactory.class */
    enum GeoResultConverterFactory {
        INSTANCE;

        Converter<GeoRadiusResponse, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> forMetric(Metric metric) {
            return new GeoResultConverter(metric);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisConverters$GeoResultConverterFactory$GeoResultConverter.class */
        private static class GeoResultConverter implements Converter<GeoRadiusResponse, GeoResult<RedisGeoCommands.GeoLocation<byte[]>>> {
            private Metric metric;

            public GeoResultConverter(Metric metric) {
                this.metric = metric;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public GeoResult<RedisGeoCommands.GeoLocation<byte[]>> convert2(GeoRadiusResponse source) {
                Point point = (Point) JedisConverters.GEO_COORDINATE_TO_POINT_CONVERTER.convert2(source.getCoordinate());
                return new GeoResult<>(new RedisGeoCommands.GeoLocation(source.getMember(), point), new Distance(source.getDistance(), this.metric));
            }
        }
    }
}
