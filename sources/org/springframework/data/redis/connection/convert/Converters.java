package org.springframework.data.redis.connection.convert;

import com.moredian.onpremise.core.common.constants.SymbolConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.PropertyAccessor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Metrics;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/Converters.class */
public abstract class Converters {
    private static final String CLUSTER_NODES_LINE_SEPARATOR = "\n";
    private static final Converter<String, RedisClusterNode> STRING_TO_CLUSTER_NODE_CONVERTER;
    private static final byte[] ONE = {49};
    private static final byte[] ZERO = {48};
    private static final Converter<String, Properties> STRING_TO_PROPS = new StringToPropertiesConverter();
    private static final Converter<Long, Boolean> LONG_TO_BOOLEAN = new LongToBooleanConverter();
    private static final Converter<String, DataType> STRING_TO_DATA_TYPE = new StringToDataTypeConverter();
    private static final Converter<Map<?, ?>, Properties> MAP_TO_PROPERTIES = MapToPropertiesConverter.INSTANCE;
    private static final Map<String, RedisClusterNode.Flag> flagLookupMap = new LinkedHashMap(RedisClusterNode.Flag.values().length, 1.0f);

    static {
        for (RedisClusterNode.Flag flag : RedisClusterNode.Flag.values()) {
            flagLookupMap.put(flag.getRaw(), flag);
        }
        STRING_TO_CLUSTER_NODE_CONVERTER = new Converter<String, RedisClusterNode>() { // from class: org.springframework.data.redis.connection.convert.Converters.1
            static final int ID_INDEX = 0;
            static final int HOST_PORT_INDEX = 1;
            static final int FLAGS_INDEX = 2;
            static final int MASTER_ID_INDEX = 3;
            static final int LINK_STATE_INDEX = 7;
            static final int SLOTS_INDEX = 8;

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public RedisClusterNode convert2(String source) {
                String[] args = source.split(SymbolConstants.SPACE_SYMBOL);
                String[] hostAndPort = StringUtils.split(args[1], ":");
                RedisClusterNode.SlotRange range = parseSlotRange(args);
                Set<RedisClusterNode.Flag> flags = parseFlags(args);
                String portPart = hostAndPort[1];
                if (portPart.contains("@")) {
                    portPart = portPart.substring(0, portPart.indexOf(64));
                }
                RedisClusterNode.RedisClusterNodeBuilder nodeBuilder = RedisClusterNode.newRedisClusterNode().listeningAt(hostAndPort[0], Integer.valueOf(portPart).intValue()).withId(args[0]).promotedAs(flags.contains(RedisClusterNode.Flag.MASTER) ? RedisNode.NodeType.MASTER : RedisNode.NodeType.SLAVE).serving(range).withFlags(flags).linkState(parseLinkState(args));
                if (!args[3].isEmpty() && !args[3].startsWith("-")) {
                    nodeBuilder.slaveOf(args[3]);
                }
                return nodeBuilder.build();
            }

            private Set<RedisClusterNode.Flag> parseFlags(String[] args) {
                String raw = args[2];
                LinkedHashSet linkedHashSet = new LinkedHashSet(8, 1.0f);
                if (StringUtils.hasText(raw)) {
                    for (String flag2 : raw.split(",")) {
                        linkedHashSet.add(Converters.flagLookupMap.get(flag2));
                    }
                }
                return linkedHashSet;
            }

            private RedisClusterNode.LinkState parseLinkState(String[] args) {
                String raw = args[7];
                if (StringUtils.hasText(raw)) {
                    return RedisClusterNode.LinkState.valueOf(raw.toUpperCase());
                }
                return RedisClusterNode.LinkState.DISCONNECTED;
            }

            private RedisClusterNode.SlotRange parseSlotRange(String[] args) {
                Set<Integer> slots = new LinkedHashSet<>();
                for (int i = 8; i < args.length; i++) {
                    String raw = args[i];
                    if (!raw.startsWith(PropertyAccessor.PROPERTY_KEY_PREFIX)) {
                        if (raw.contains("-")) {
                            String[] slotRange = StringUtils.split(raw, "-");
                            if (slotRange != null) {
                                int from = Integer.valueOf(slotRange[0]).intValue();
                                int to = Integer.valueOf(slotRange[1]).intValue();
                                for (int slot = from; slot <= to; slot++) {
                                    slots.add(Integer.valueOf(slot));
                                }
                            }
                        } else {
                            slots.add(Integer.valueOf(raw));
                        }
                    }
                }
                RedisClusterNode.SlotRange range = new RedisClusterNode.SlotRange(slots);
                return range;
            }
        };
    }

    public static Converter<String, Properties> stringToProps() {
        return STRING_TO_PROPS;
    }

    public static Converter<Long, Boolean> longToBoolean() {
        return LONG_TO_BOOLEAN;
    }

    public static Converter<String, DataType> stringToDataType() {
        return STRING_TO_DATA_TYPE;
    }

    public static Properties toProperties(String source) {
        return STRING_TO_PROPS.convert2(source);
    }

    public static Properties toProperties(Map<?, ?> source) {
        return MAP_TO_PROPERTIES.convert2(source);
    }

    public static Boolean toBoolean(Long source) {
        return LONG_TO_BOOLEAN.convert2(source);
    }

    public static DataType toDataType(String source) {
        return STRING_TO_DATA_TYPE.convert2(source);
    }

    public static byte[] toBit(Boolean source) {
        return source.booleanValue() ? ONE : ZERO;
    }

    protected static RedisClusterNode toClusterNode(String clusterNodesLine) {
        return STRING_TO_CLUSTER_NODE_CONVERTER.convert2(clusterNodesLine);
    }

    public static Set<RedisClusterNode> toSetOfRedisClusterNodes(Collection<String> lines) {
        if (CollectionUtils.isEmpty(lines)) {
            return Collections.emptySet();
        }
        Set<RedisClusterNode> nodes = new LinkedHashSet<>(lines.size());
        for (String line : lines) {
            nodes.add(toClusterNode(line));
        }
        return nodes;
    }

    public static Set<RedisClusterNode> toSetOfRedisClusterNodes(String clusterNodes) {
        if (StringUtils.isEmpty(clusterNodes)) {
            return Collections.emptySet();
        }
        String[] lines = clusterNodes.split("\n");
        return toSetOfRedisClusterNodes(Arrays.asList(lines));
    }

    public static List<Object> toObjects(Set<RedisZSetCommands.Tuple> tuples) {
        List<Object> tupleArgs = new ArrayList<>(tuples.size() * 2);
        for (RedisZSetCommands.Tuple tuple : tuples) {
            tupleArgs.add(tuple.getScore());
            tupleArgs.add(tuple.getValue());
        }
        return tupleArgs;
    }

    public static Long toTimeMillis(String seconds, String microseconds) {
        return Long.valueOf((((Long) NumberUtils.parseNumber(seconds, Long.class)).longValue() * 1000) + (((Long) NumberUtils.parseNumber(microseconds, Long.class)).longValue() / 1000));
    }

    public static long secondsToTimeUnit(long seconds, TimeUnit targetUnit) {
        Assert.notNull(targetUnit, "TimeUnit must not be null!");
        if (seconds > 0) {
            return targetUnit.convert(seconds, TimeUnit.SECONDS);
        }
        return seconds;
    }

    public static Converter<Long, Long> secondsToTimeUnit(final TimeUnit timeUnit) {
        return new Converter<Long, Long>() { // from class: org.springframework.data.redis.connection.convert.Converters.2
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public Long convert2(Long seconds) {
                return Long.valueOf(Converters.secondsToTimeUnit(seconds.longValue(), timeUnit));
            }
        };
    }

    public static long millisecondsToTimeUnit(long milliseconds, TimeUnit targetUnit) {
        Assert.notNull(targetUnit, "TimeUnit must not be null!");
        if (milliseconds > 0) {
            return targetUnit.convert(milliseconds, TimeUnit.MILLISECONDS);
        }
        return milliseconds;
    }

    public static Converter<Long, Long> millisecondsToTimeUnit(final TimeUnit timeUnit) {
        return new Converter<Long, Long>() { // from class: org.springframework.data.redis.connection.convert.Converters.3
            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public Long convert2(Long seconds) {
                return Long.valueOf(Converters.millisecondsToTimeUnit(seconds.longValue(), timeUnit));
            }
        };
    }

    public static <V> Converter<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<V>>> deserializingGeoResultsConverter(RedisSerializer<V> serializer) {
        return new DeserializingGeoResultsConverter(serializer);
    }

    public static Converter<Double, Distance> distanceConverterForMetric(Metric metric) {
        return DistanceConverterFactory.INSTANCE.forMetric(metric);
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/Converters$DistanceConverterFactory.class */
    enum DistanceConverterFactory {
        INSTANCE;

        DistanceConverter forMetric(Metric metric) {
            return new DistanceConverter((metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric);
        }

        /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/Converters$DistanceConverterFactory$DistanceConverter.class */
        static class DistanceConverter implements Converter<Double, Distance> {
            private Metric metric;

            public DistanceConverter(Metric metric) {
                this.metric = (metric == null || ObjectUtils.nullSafeEquals(Metrics.NEUTRAL, metric)) ? RedisGeoCommands.DistanceUnit.METERS : metric;
            }

            @Override // org.springframework.core.convert.converter.Converter
            /* renamed from: convert, reason: avoid collision after fix types in other method */
            public Distance convert2(Double source) {
                if (source == null) {
                    return null;
                }
                return new Distance(source.doubleValue(), this.metric);
            }
        }
    }

    /* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/convert/Converters$DeserializingGeoResultsConverter.class */
    static class DeserializingGeoResultsConverter<V> implements Converter<GeoResults<RedisGeoCommands.GeoLocation<byte[]>>, GeoResults<RedisGeoCommands.GeoLocation<V>>> {
        final RedisSerializer<V> serializer;

        public DeserializingGeoResultsConverter(RedisSerializer<V> serializer) {
            this.serializer = serializer;
        }

        @Override // org.springframework.core.convert.converter.Converter
        /* renamed from: convert, reason: avoid collision after fix types in other method */
        public GeoResults<RedisGeoCommands.GeoLocation<V>> convert2(GeoResults<RedisGeoCommands.GeoLocation<byte[]>> source) {
            if (source == null) {
                return new GeoResults<>(Collections.emptyList());
            }
            List<GeoResult<RedisGeoCommands.GeoLocation<V>>> values = new ArrayList<>(source.getContent().size());
            for (GeoResult<RedisGeoCommands.GeoLocation<byte[]>> value : source.getContent()) {
                values.add(new GeoResult<>(new RedisGeoCommands.GeoLocation(this.serializer.deserialize(value.getContent().getName()), value.getContent().getPoint()), value.getDistance()));
            }
            return new GeoResults<>(values, source.getAverageDistance().getMetric());
        }
    }
}
