package com.alibaba.excel.util;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.exception.ExcelCommonException;
import com.alibaba.excel.metadata.BaseRowModel;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/ClassUtils.class */
public class ClassUtils {
    private static final Map<Class, SoftReference<FieldCache>> FIELD_CACHE = new ConcurrentHashMap();

    public static void declaredFields(Class clazz, List<Field> defaultFieldList, Map<Integer, Field> customFiledMap, Map<String, Field> ignoreMap, Boolean convertAllFiled) {
        FieldCache fieldCache = getFieldCache(clazz, convertAllFiled);
        if (fieldCache != null) {
            defaultFieldList.addAll(fieldCache.getDefaultFieldList());
            customFiledMap.putAll(fieldCache.getCustomFiledMap());
            ignoreMap.putAll(fieldCache.getIgnoreMap());
        }
    }

    public static void declaredFields(Class clazz, List<Field> fieldList, Boolean convertAllFiled) {
        FieldCache fieldCache = getFieldCache(clazz, convertAllFiled);
        if (fieldCache != null) {
            fieldList.addAll(fieldCache.getAllFieldList());
        }
    }

    private static FieldCache getFieldCache(Class clazz, Boolean convertAllFiled) {
        if (clazz == null) {
            return null;
        }
        SoftReference<FieldCache> fieldCacheSoftReference = FIELD_CACHE.get(clazz);
        if (fieldCacheSoftReference != null && fieldCacheSoftReference.get() != null) {
            return fieldCacheSoftReference.get();
        }
        synchronized (clazz) {
            SoftReference<FieldCache> fieldCacheSoftReference2 = FIELD_CACHE.get(clazz);
            if (fieldCacheSoftReference2 != null && fieldCacheSoftReference2.get() != null) {
                return fieldCacheSoftReference2.get();
            }
            declaredFields(clazz, convertAllFiled);
            return FIELD_CACHE.get(clazz).get();
        }
    }

    private static void declaredFields(Class clazz, Boolean convertAllFiled) {
        List<Field> tempFieldList = new ArrayList<>();
        Class superclass = clazz;
        while (true) {
            Class tempClass = superclass;
            if (tempClass == null || tempClass == BaseRowModel.class) {
                break;
            }
            Collections.addAll(tempFieldList, tempClass.getDeclaredFields());
            superclass = tempClass.getSuperclass();
        }
        List<Field> defaultFieldList = new ArrayList<>();
        Map<Integer, Field> customFiledMap = new TreeMap<>();
        List<Field> allFieldList = new ArrayList<>();
        Map<String, Field> ignoreMap = new HashMap<>(16);
        ExcelIgnoreUnannotated excelIgnoreUnannotated = (ExcelIgnoreUnannotated) clazz.getAnnotation(ExcelIgnoreUnannotated.class);
        for (Field field : tempFieldList) {
            ExcelIgnore excelIgnore = (ExcelIgnore) field.getAnnotation(ExcelIgnore.class);
            if (excelIgnore != null) {
                ignoreMap.put(field.getName(), field);
            } else {
                ExcelProperty excelProperty = (ExcelProperty) field.getAnnotation(ExcelProperty.class);
                boolean noExcelProperty = excelProperty == null && !((convertAllFiled == null || convertAllFiled.booleanValue()) && excelIgnoreUnannotated == null);
                if (noExcelProperty) {
                    ignoreMap.put(field.getName(), field);
                } else {
                    boolean isStaticFinalOrTransient = (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) || Modifier.isTransient(field.getModifiers());
                    if (excelProperty == null && isStaticFinalOrTransient) {
                        ignoreMap.put(field.getName(), field);
                    } else if (excelProperty == null || excelProperty.index() < 0) {
                        defaultFieldList.add(field);
                        allFieldList.add(field);
                    } else {
                        if (customFiledMap.containsKey(Integer.valueOf(excelProperty.index()))) {
                            throw new ExcelCommonException("The index of '" + customFiledMap.get(Integer.valueOf(excelProperty.index())).getName() + "' and '" + field.getName() + "' must be inconsistent");
                        }
                        customFiledMap.put(Integer.valueOf(excelProperty.index()), field);
                        allFieldList.add(field);
                    }
                }
            }
        }
        FIELD_CACHE.put(clazz, new SoftReference<>(new FieldCache(defaultFieldList, customFiledMap, allFieldList, ignoreMap)));
    }

    /* loaded from: easyexcel-2.1.6.jar:com/alibaba/excel/util/ClassUtils$FieldCache.class */
    private static class FieldCache {
        private List<Field> defaultFieldList;
        private Map<Integer, Field> customFiledMap;
        private List<Field> allFieldList;
        private Map<String, Field> ignoreMap;

        public FieldCache(List<Field> defaultFieldList, Map<Integer, Field> customFiledMap, List<Field> allFieldList, Map<String, Field> ignoreMap) {
            this.defaultFieldList = defaultFieldList;
            this.customFiledMap = customFiledMap;
            this.allFieldList = allFieldList;
            this.ignoreMap = ignoreMap;
        }

        public List<Field> getDefaultFieldList() {
            return this.defaultFieldList;
        }

        public Map<Integer, Field> getCustomFiledMap() {
            return this.customFiledMap;
        }

        public List<Field> getAllFieldList() {
            return this.allFieldList;
        }

        public Map<String, Field> getIgnoreMap() {
            return this.ignoreMap;
        }
    }
}
