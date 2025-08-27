package org.springframework.data.redis.connection.srp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.connection.ReturnType;
import redis.reply.Reply;

@Deprecated
/* loaded from: spring-data-redis-1.8.23.RELEASE.jar:org/springframework/data/redis/connection/srp/SrpScriptReturnConverter.class */
public class SrpScriptReturnConverter implements Converter<Object, Object> {
    private ReturnType returnType;

    public SrpScriptReturnConverter(ReturnType returnType) {
        this.returnType = returnType;
    }

    @Override // org.springframework.core.convert.converter.Converter
    public Object convert(Object source) {
        if (this.returnType == ReturnType.MULTI) {
            Reply<?>[] replies = (Reply[]) source;
            List<Object> results = new ArrayList<>();
            for (Reply<?> reply : replies) {
                results.add(reply.data());
            }
            return results;
        }
        if (this.returnType == ReturnType.BOOLEAN) {
            if (source == null) {
                return Boolean.FALSE;
            }
            return Boolean.valueOf(((Long) source).longValue() == 1);
        }
        return source;
    }
}
