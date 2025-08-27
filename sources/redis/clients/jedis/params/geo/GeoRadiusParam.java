package redis.clients.jedis.params.geo;

import java.util.ArrayList;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.params.Params;
import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/params/geo/GeoRadiusParam.class */
public class GeoRadiusParam extends Params {
    private static final String WITHCOORD = "withcoord";
    private static final String WITHDIST = "withdist";
    private static final String ASC = "asc";
    private static final String DESC = "desc";
    private static final String COUNT = "count";

    private GeoRadiusParam() {
    }

    public static GeoRadiusParam geoRadiusParam() {
        return new GeoRadiusParam();
    }

    public GeoRadiusParam withCoord() {
        addParam(WITHCOORD);
        return this;
    }

    public GeoRadiusParam withDist() {
        addParam(WITHDIST);
        return this;
    }

    public GeoRadiusParam sortAscending() {
        addParam(ASC);
        return this;
    }

    public GeoRadiusParam sortDescending() {
        addParam(DESC);
        return this;
    }

    public GeoRadiusParam count(int count) {
        if (count > 0) {
            addParam(COUNT, Integer.valueOf(count));
        }
        return this;
    }

    public byte[][] getByteParams(byte[]... args) {
        ArrayList<byte[]> byteParams = new ArrayList<>();
        for (byte[] arg : args) {
            byteParams.add(arg);
        }
        if (contains(WITHCOORD)) {
            byteParams.add(SafeEncoder.encode(WITHCOORD));
        }
        if (contains(WITHDIST)) {
            byteParams.add(SafeEncoder.encode(WITHDIST));
        }
        if (contains(COUNT)) {
            byteParams.add(SafeEncoder.encode(COUNT));
            byteParams.add(Protocol.toByteArray(((Integer) getParam(COUNT)).intValue()));
        }
        if (contains(ASC)) {
            byteParams.add(SafeEncoder.encode(ASC));
        } else if (contains(DESC)) {
            byteParams.add(SafeEncoder.encode(DESC));
        }
        return (byte[][]) byteParams.toArray((Object[]) new byte[byteParams.size()]);
    }
}
