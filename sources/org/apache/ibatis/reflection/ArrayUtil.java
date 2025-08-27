package org.apache.ibatis.reflection;

import java.util.Arrays;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/ArrayUtil.class */
public class ArrayUtil {
    public static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        Class<?> clazz = obj.getClass();
        if (!clazz.isArray()) {
            return obj.hashCode();
        }
        Class<?> componentType = clazz.getComponentType();
        if (Long.TYPE.equals(componentType)) {
            return Arrays.hashCode((long[]) obj);
        }
        if (Integer.TYPE.equals(componentType)) {
            return Arrays.hashCode((int[]) obj);
        }
        if (Short.TYPE.equals(componentType)) {
            return Arrays.hashCode((short[]) obj);
        }
        if (Character.TYPE.equals(componentType)) {
            return Arrays.hashCode((char[]) obj);
        }
        if (Byte.TYPE.equals(componentType)) {
            return Arrays.hashCode((byte[]) obj);
        }
        if (Boolean.TYPE.equals(componentType)) {
            return Arrays.hashCode((boolean[]) obj);
        }
        if (Float.TYPE.equals(componentType)) {
            return Arrays.hashCode((float[]) obj);
        }
        if (Double.TYPE.equals(componentType)) {
            return Arrays.hashCode((double[]) obj);
        }
        return Arrays.hashCode((Object[]) obj);
    }

    public static boolean equals(Object thisObj, Object thatObj) {
        if (thisObj == null) {
            return thatObj == null;
        }
        if (thatObj == null) {
            return false;
        }
        Class<?> clazz = thisObj.getClass();
        if (!clazz.equals(thatObj.getClass())) {
            return false;
        }
        if (!clazz.isArray()) {
            return thisObj.equals(thatObj);
        }
        Class<?> componentType = clazz.getComponentType();
        if (Long.TYPE.equals(componentType)) {
            return Arrays.equals((long[]) thisObj, (long[]) thatObj);
        }
        if (Integer.TYPE.equals(componentType)) {
            return Arrays.equals((int[]) thisObj, (int[]) thatObj);
        }
        if (Short.TYPE.equals(componentType)) {
            return Arrays.equals((short[]) thisObj, (short[]) thatObj);
        }
        if (Character.TYPE.equals(componentType)) {
            return Arrays.equals((char[]) thisObj, (char[]) thatObj);
        }
        if (Byte.TYPE.equals(componentType)) {
            return Arrays.equals((byte[]) thisObj, (byte[]) thatObj);
        }
        if (Boolean.TYPE.equals(componentType)) {
            return Arrays.equals((boolean[]) thisObj, (boolean[]) thatObj);
        }
        if (Float.TYPE.equals(componentType)) {
            return Arrays.equals((float[]) thisObj, (float[]) thatObj);
        }
        if (Double.TYPE.equals(componentType)) {
            return Arrays.equals((double[]) thisObj, (double[]) thatObj);
        }
        return Arrays.equals((Object[]) thisObj, (Object[]) thatObj);
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return "null";
        }
        Class<?> clazz = obj.getClass();
        if (!clazz.isArray()) {
            return obj.toString();
        }
        Class<?> componentType = obj.getClass().getComponentType();
        if (Long.TYPE.equals(componentType)) {
            return Arrays.toString((long[]) obj);
        }
        if (Integer.TYPE.equals(componentType)) {
            return Arrays.toString((int[]) obj);
        }
        if (Short.TYPE.equals(componentType)) {
            return Arrays.toString((short[]) obj);
        }
        if (Character.TYPE.equals(componentType)) {
            return Arrays.toString((char[]) obj);
        }
        if (Byte.TYPE.equals(componentType)) {
            return Arrays.toString((byte[]) obj);
        }
        if (Boolean.TYPE.equals(componentType)) {
            return Arrays.toString((boolean[]) obj);
        }
        if (Float.TYPE.equals(componentType)) {
            return Arrays.toString((float[]) obj);
        }
        if (Double.TYPE.equals(componentType)) {
            return Arrays.toString((double[]) obj);
        }
        return Arrays.toString((Object[]) obj);
    }
}
