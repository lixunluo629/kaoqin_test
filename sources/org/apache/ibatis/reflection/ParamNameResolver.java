package org.apache.ibatis.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/ParamNameResolver.class */
public class ParamNameResolver {
    private static final String GENERIC_NAME_PREFIX = "param";
    private final SortedMap<Integer, String> names;
    private boolean hasParamAnnotation;

    public ParamNameResolver(Configuration config, Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        SortedMap<Integer, String> map = new TreeMap<>();
        int paramCount = paramAnnotations.length;
        for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
            if (!isSpecialParameter(paramTypes[paramIndex])) {
                String name = null;
                Annotation[] annotationArr = paramAnnotations[paramIndex];
                int length = annotationArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    Annotation annotation = annotationArr[i];
                    if (!(annotation instanceof Param)) {
                        i++;
                    } else {
                        this.hasParamAnnotation = true;
                        name = ((Param) annotation).value();
                        break;
                    }
                }
                if (name == null) {
                    name = config.isUseActualParamName() ? getActualParamName(method, paramIndex) : name;
                    if (name == null) {
                        name = String.valueOf(map.size());
                    }
                }
                map.put(Integer.valueOf(paramIndex), name);
            }
        }
        this.names = Collections.unmodifiableSortedMap(map);
    }

    private String getActualParamName(Method method, int paramIndex) {
        if (Jdk.parameterExists) {
            return ParamNameUtil.getParamNames(method).get(paramIndex);
        }
        return null;
    }

    private static boolean isSpecialParameter(Class<?> clazz) {
        return RowBounds.class.isAssignableFrom(clazz) || ResultHandler.class.isAssignableFrom(clazz);
    }

    public String[] getNames() {
        return (String[]) this.names.values().toArray(new String[0]);
    }

    public Object getNamedParams(Object[] args) {
        int paramCount = this.names.size();
        if (args == null || paramCount == 0) {
            return null;
        }
        if (!this.hasParamAnnotation && paramCount == 1) {
            return args[this.names.firstKey().intValue()];
        }
        Map<String, Object> param = new MapperMethod.ParamMap<>();
        int i = 0;
        for (Map.Entry<Integer, String> entry : this.names.entrySet()) {
            param.put(entry.getValue(), args[entry.getKey().intValue()]);
            String genericParamName = GENERIC_NAME_PREFIX + String.valueOf(i + 1);
            if (!this.names.containsValue(genericParamName)) {
                param.put(genericParamName, args[entry.getKey().intValue()]);
            }
            i++;
        }
        return param;
    }
}
