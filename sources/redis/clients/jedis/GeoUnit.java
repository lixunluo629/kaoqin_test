package redis.clients.jedis;

import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/GeoUnit.class */
public enum GeoUnit {
    M,
    KM,
    MI,
    FT;

    public final byte[] raw = SafeEncoder.encode(name().toLowerCase());

    GeoUnit() {
    }
}
