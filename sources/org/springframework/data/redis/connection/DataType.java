package org.springframework.data.redis.connection;

import io.swagger.models.properties.StringProperty;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/DataType.class */
public enum DataType {
    NONE("none"),
    STRING(StringProperty.TYPE),
    LIST("list"),
    SET("set"),
    ZSET("zset"),
    HASH("hash");

    private static final Map<String, DataType> codeLookup = new ConcurrentHashMap(6);
    private final String code;

    static {
        Iterator it = EnumSet.allOf(DataType.class).iterator();
        while (it.hasNext()) {
            DataType type = (DataType) it.next();
            codeLookup.put(type.code, type);
        }
    }

    DataType(String name) {
        this.code = name;
    }

    public String code() {
        return this.code;
    }

    public static DataType fromCode(String code) {
        DataType data = codeLookup.get(code);
        if (data == null) {
            throw new IllegalArgumentException("unknown data type code");
        }
        return data;
    }
}
