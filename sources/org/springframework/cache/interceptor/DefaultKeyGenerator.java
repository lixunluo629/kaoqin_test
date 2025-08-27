package org.springframework.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Arrays;

@Deprecated
/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/cache/interceptor/DefaultKeyGenerator.class */
public class DefaultKeyGenerator implements KeyGenerator {
    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    @Override // org.springframework.cache.interceptor.KeyGenerator
    public Object generate(Object target, Method method, Object... params) {
        if (params.length == 0) {
            return 0;
        }
        if (params.length == 1) {
            Object param = params[0];
            if (param == null) {
                return 53;
            }
            if (!param.getClass().isArray()) {
                return param;
            }
        }
        return Integer.valueOf(Arrays.deepHashCode(params));
    }
}
