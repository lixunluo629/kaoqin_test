package redis.clients.jedis;

import io.swagger.models.properties.StringProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlErrorCodes;
import redis.clients.util.JedisByteHashMap;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/BuilderFactory.class */
public final class BuilderFactory {
    public static final Builder<Double> DOUBLE = new Builder<Double>() { // from class: redis.clients.jedis.BuilderFactory.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Double build(Object data) {
            String string = BuilderFactory.STRING.build(data);
            if (string == null) {
                return null;
            }
            try {
                return Double.valueOf(string);
            } catch (NumberFormatException e) {
                if (string.equals("inf") || string.equals("+inf")) {
                    return Double.valueOf(Double.POSITIVE_INFINITY);
                }
                if (string.equals("-inf")) {
                    return Double.valueOf(Double.NEGATIVE_INFINITY);
                }
                throw e;
            }
        }

        public String toString() {
            return XmlErrorCodes.DOUBLE;
        }
    };
    public static final Builder<Boolean> BOOLEAN = new Builder<Boolean>() { // from class: redis.clients.jedis.BuilderFactory.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Boolean build(Object data) {
            return Boolean.valueOf(((Long) data).longValue() == 1);
        }

        public String toString() {
            return "boolean";
        }
    };
    public static final Builder<byte[]> BYTE_ARRAY = new Builder<byte[]>() { // from class: redis.clients.jedis.BuilderFactory.3
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public byte[] build(Object data) {
            return (byte[]) data;
        }

        public String toString() {
            return "byte[]";
        }
    };
    public static final Builder<Long> LONG = new Builder<Long>() { // from class: redis.clients.jedis.BuilderFactory.4
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Long build(Object data) {
            return (Long) data;
        }

        public String toString() {
            return XmlErrorCodes.LONG;
        }
    };
    public static final Builder<String> STRING = new Builder<String>() { // from class: redis.clients.jedis.BuilderFactory.5
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public String build(Object data) {
            if (data == null) {
                return null;
            }
            return SafeEncoder.encode((byte[]) data);
        }

        public String toString() {
            return StringProperty.TYPE;
        }
    };
    public static final Builder<List<String>> STRING_LIST = new Builder<List<String>>() { // from class: redis.clients.jedis.BuilderFactory.6
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<String> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            ArrayList<String> result = new ArrayList<>(l.size());
            for (byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(SafeEncoder.encode(barray));
                }
            }
            return result;
        }

        public String toString() {
            return "List<String>";
        }
    };
    public static final Builder<Map<String, String>> STRING_MAP = new Builder<Map<String, String>>() { // from class: redis.clients.jedis.BuilderFactory.7
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Map<String, String> build(Object data) {
            List<byte[]> flatHash = (List) data;
            Map<String, String> hash = new HashMap<>(flatHash.size() / 2, 1.0f);
            Iterator<byte[]> iterator = flatHash.iterator();
            while (iterator.hasNext()) {
                hash.put(SafeEncoder.encode(iterator.next()), SafeEncoder.encode(iterator.next()));
            }
            return hash;
        }

        public String toString() {
            return "Map<String, String>";
        }
    };
    public static final Builder<Map<String, String>> PUBSUB_NUMSUB_MAP = new Builder<Map<String, String>>() { // from class: redis.clients.jedis.BuilderFactory.8
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Map<String, String> build(Object data) {
            List<Object> flatHash = (List) data;
            Map<String, String> hash = new HashMap<>(flatHash.size() / 2, 1.0f);
            Iterator<Object> iterator = flatHash.iterator();
            while (iterator.hasNext()) {
                hash.put(SafeEncoder.encode((byte[]) iterator.next()), String.valueOf((Long) iterator.next()));
            }
            return hash;
        }

        public String toString() {
            return "PUBSUB_NUMSUB_MAP<String, String>";
        }
    };
    public static final Builder<Set<String>> STRING_SET = new Builder<Set<String>>() { // from class: redis.clients.jedis.BuilderFactory.9
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Set<String> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            Set<String> result = new HashSet<>(l.size(), 1.0f);
            for (byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(SafeEncoder.encode(barray));
                }
            }
            return result;
        }

        public String toString() {
            return "Set<String>";
        }
    };
    public static final Builder<List<byte[]>> BYTE_ARRAY_LIST = new Builder<List<byte[]>>() { // from class: redis.clients.jedis.BuilderFactory.10
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<byte[]> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            return l;
        }

        public String toString() {
            return "List<byte[]>";
        }
    };
    public static final Builder<Set<byte[]>> BYTE_ARRAY_ZSET = new Builder<Set<byte[]>>() { // from class: redis.clients.jedis.BuilderFactory.11
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Set<byte[]> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            Set<byte[]> result = new LinkedHashSet<>(l);
            for (byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(barray);
                }
            }
            return result;
        }

        public String toString() {
            return "ZSet<byte[]>";
        }
    };
    public static final Builder<Map<byte[], byte[]>> BYTE_ARRAY_MAP = new Builder<Map<byte[], byte[]>>() { // from class: redis.clients.jedis.BuilderFactory.12
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Map<byte[], byte[]> build(Object data) {
            List<byte[]> flatHash = (List) data;
            Map<byte[], byte[]> hash = new JedisByteHashMap();
            Iterator<byte[]> iterator = flatHash.iterator();
            while (iterator.hasNext()) {
                hash.put(iterator.next(), iterator.next());
            }
            return hash;
        }

        public String toString() {
            return "Map<byte[], byte[]>";
        }
    };
    public static final Builder<Set<String>> STRING_ZSET = new Builder<Set<String>>() { // from class: redis.clients.jedis.BuilderFactory.13
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Set<String> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            Set<String> result = new LinkedHashSet<>(l.size(), 1.0f);
            for (byte[] barray : l) {
                if (barray == null) {
                    result.add(null);
                } else {
                    result.add(SafeEncoder.encode(barray));
                }
            }
            return result;
        }

        public String toString() {
            return "ZSet<String>";
        }
    };
    public static final Builder<Set<Tuple>> TUPLE_ZSET = new Builder<Set<Tuple>>() { // from class: redis.clients.jedis.BuilderFactory.14
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Set<Tuple> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            Set<Tuple> result = new LinkedHashSet<>(l.size() / 2, 1.0f);
            Iterator<byte[]> iterator = l.iterator();
            while (iterator.hasNext()) {
                result.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
            }
            return result;
        }

        public String toString() {
            return "ZSet<Tuple>";
        }
    };
    public static final Builder<Set<Tuple>> TUPLE_ZSET_BINARY = new Builder<Set<Tuple>>() { // from class: redis.clients.jedis.BuilderFactory.15
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public Set<Tuple> build(Object data) {
            if (null == data) {
                return null;
            }
            List<byte[]> l = (List) data;
            Set<Tuple> result = new LinkedHashSet<>(l.size() / 2, 1.0f);
            Iterator<byte[]> iterator = l.iterator();
            while (iterator.hasNext()) {
                result.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
            }
            return result;
        }

        public String toString() {
            return "ZSet<Tuple>";
        }
    };
    public static final Builder<Object> EVAL_RESULT = new Builder<Object>() { // from class: redis.clients.jedis.BuilderFactory.16
        @Override // redis.clients.jedis.Builder
        public Object build(Object data) {
            return evalResult(data);
        }

        public String toString() {
            return "Eval<Object>";
        }

        private Object evalResult(Object result) {
            if (result instanceof byte[]) {
                return SafeEncoder.encode((byte[]) result);
            }
            if (result instanceof List) {
                List<?> list = (List) result;
                List<Object> listResult = new ArrayList<>(list.size());
                for (Object bin : list) {
                    listResult.add(evalResult(bin));
                }
                return listResult;
            }
            return result;
        }
    };
    public static final Builder<Object> EVAL_BINARY_RESULT = new Builder<Object>() { // from class: redis.clients.jedis.BuilderFactory.17
        @Override // redis.clients.jedis.Builder
        public Object build(Object data) {
            return evalResult(data);
        }

        public String toString() {
            return "Eval<Object>";
        }

        private Object evalResult(Object result) {
            if (result instanceof List) {
                List<?> list = (List) result;
                List<Object> listResult = new ArrayList<>(list.size());
                for (Object bin : list) {
                    listResult.add(evalResult(bin));
                }
                return listResult;
            }
            return result;
        }
    };
    public static final Builder<List<GeoCoordinate>> GEO_COORDINATE_LIST = new Builder<List<GeoCoordinate>>() { // from class: redis.clients.jedis.BuilderFactory.18
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<GeoCoordinate> build(Object data) {
            if (null == data) {
                return null;
            }
            return interpretGeoposResult((List) data);
        }

        public String toString() {
            return "List<GeoCoordinate>";
        }

        private List<GeoCoordinate> interpretGeoposResult(List<Object> responses) {
            List<GeoCoordinate> responseCoordinate = new ArrayList<>(responses.size());
            for (Object response : responses) {
                if (response == null) {
                    responseCoordinate.add(null);
                } else {
                    List<Object> respList = (List) response;
                    GeoCoordinate coord = new GeoCoordinate(BuilderFactory.DOUBLE.build(respList.get(0)).doubleValue(), BuilderFactory.DOUBLE.build(respList.get(1)).doubleValue());
                    responseCoordinate.add(coord);
                }
            }
            return responseCoordinate;
        }
    };
    public static final Builder<List<GeoRadiusResponse>> GEORADIUS_WITH_PARAMS_RESULT = new Builder<List<GeoRadiusResponse>>() { // from class: redis.clients.jedis.BuilderFactory.19
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<GeoRadiusResponse> build(Object data) {
            if (data == null) {
                return null;
            }
            List<Object> objectList = (List) data;
            List<GeoRadiusResponse> responses = new ArrayList<>(objectList.size());
            if (objectList.isEmpty()) {
                return responses;
            }
            if (objectList.get(0) instanceof List) {
                for (Object obj : objectList) {
                    List<Object> informations = (List) obj;
                    GeoRadiusResponse resp = new GeoRadiusResponse((byte[]) informations.get(0));
                    int size = informations.size();
                    for (int idx = 1; idx < size; idx++) {
                        Object info = informations.get(idx);
                        if (info instanceof List) {
                            List<Object> coord = (List) info;
                            resp.setCoordinate(new GeoCoordinate(BuilderFactory.DOUBLE.build(coord.get(0)).doubleValue(), BuilderFactory.DOUBLE.build(coord.get(1)).doubleValue()));
                        } else {
                            resp.setDistance(BuilderFactory.DOUBLE.build(info).doubleValue());
                        }
                    }
                    responses.add(resp);
                }
            } else {
                for (Object obj2 : objectList) {
                    responses.add(new GeoRadiusResponse((byte[]) obj2));
                }
            }
            return responses;
        }

        public String toString() {
            return "GeoRadiusWithParamsResult";
        }
    };
    public static final Builder<List<Long>> LONG_LIST = new Builder<List<Long>>() { // from class: redis.clients.jedis.BuilderFactory.20
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // redis.clients.jedis.Builder
        public List<Long> build(Object data) {
            if (null == data) {
                return null;
            }
            return (List) data;
        }

        public String toString() {
            return "List<Long>";
        }
    };

    private BuilderFactory() {
        throw new InstantiationError("Must not instantiate this class");
    }
}
