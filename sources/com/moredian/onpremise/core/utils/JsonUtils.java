package com.moredian.onpremise.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: onpremise-core-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/core/utils/JsonUtils.class */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) JsonUtils.class);

    public static String toJson(Object value) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(value);
        } catch (Exception e) {
        }
        return json;
    }

    public static Map<String, Object> json2Map(String jsondata) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = (Map) mapper.readValue(jsondata, HashMap.class);
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
        return map;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T json2Object(String jsondata, TypeReference<T> valueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        T o = null;
        try {
            o = mapper.readValue(jsondata, valueTypeRef);
        } catch (Exception e) {
        }
        return o;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T json2Object(String jsondata, Class<T> tClass) {
        ObjectMapper mapper = new ObjectMapper();
        T o = null;
        try {
            o = mapper.readValue(jsondata, tClass);
        } catch (IOException e) {
            logger.error("error:{}", (Throwable) e);
        }
        return o;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static List jsoncastListType(Class clazz, String json) {
        ObjectMapper mapper = new ObjectMapper();
        List<Object> list = new ArrayList<>();
        JavaType javaType = mapper.getTypeFactory().constructParametricType((Class<?>) ArrayList.class, (Class<?>[]) new Class[]{clazz});
        try {
            list = (List) mapper.readValue(json, javaType);
        } catch (Exception e) {
            logger.error("error:{}", (Throwable) e);
        }
        return list;
    }
}
