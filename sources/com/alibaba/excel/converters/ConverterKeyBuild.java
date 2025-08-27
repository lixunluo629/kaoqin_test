package com.alibaba.excel.converters;

import com.alibaba.excel.enums.CellDataTypeEnum;
import java.util.HashMap;
import java.util.Map;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/converters/ConverterKeyBuild.class */
public class ConverterKeyBuild {
    private static final Map<String, String> BOXING_MAP = new HashMap(16);

    static {
        BOXING_MAP.put(Integer.TYPE.getName(), Integer.class.getName());
        BOXING_MAP.put(Byte.TYPE.getName(), Byte.class.getName());
        BOXING_MAP.put(Long.TYPE.getName(), Long.class.getName());
        BOXING_MAP.put(Double.TYPE.getName(), Double.class.getName());
        BOXING_MAP.put(Float.TYPE.getName(), Float.class.getName());
        BOXING_MAP.put(Character.TYPE.getName(), Character.class.getName());
        BOXING_MAP.put(Short.TYPE.getName(), Short.class.getName());
        BOXING_MAP.put(Boolean.TYPE.getName(), Boolean.class.getName());
    }

    public static String buildKey(Class clazz) {
        String className = clazz.getName();
        String boxingClassName = BOXING_MAP.get(clazz.getName());
        if (boxingClassName == null) {
            return className;
        }
        return boxingClassName;
    }

    public static String buildKey(Class clazz, CellDataTypeEnum cellDataTypeEnum) {
        return buildKey(clazz) + "-" + cellDataTypeEnum.toString();
    }
}
