package org.springframework.data.redis.connection.jedis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.ReturnType;
import redis.clients.util.SafeEncoder;

/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/jedis/JedisScriptReturnConverter.class */
public class JedisScriptReturnConverter implements Converter<Object, Object> {
    private final ReturnType returnType;

    public JedisScriptReturnConverter(ReturnType returnType) {
        this.returnType = returnType;
    }

    @Override // org.springframework.core.convert.converter.Converter
    /* renamed from: convert */
    public Object convert2(Object result) {
        if (result instanceof String) {
            return SafeEncoder.encode((String) result);
        }
        if (this.returnType == ReturnType.STATUS) {
            return JedisConverters.toString((byte[]) result);
        }
        if (this.returnType == ReturnType.BOOLEAN) {
            if (result == null) {
                return Boolean.FALSE;
            }
            return Boolean.valueOf(((Long) result).longValue() == 1);
        }
        if (this.returnType == ReturnType.MULTI) {
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
