package org.apache.ibatis.ognl;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.ibatis.ognl.enhance.UnsupportedCompilationException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/ognl/OgnlOps.class */
public abstract class OgnlOps implements NumericTypes {
    public static int compareWithConversion(Object v1, Object v2) throws NumberFormatException {
        int result;
        if (v1 == v2) {
            result = 0;
        } else {
            int t1 = getNumericType(v1);
            int t2 = getNumericType(v2);
            int type = getNumericType(t1, t2, true);
            switch (type) {
                case 6:
                    result = bigIntValue(v1).compareTo(bigIntValue(v2));
                    break;
                case 7:
                case 8:
                    break;
                case 9:
                    result = bigDecValue(v1).compareTo(bigDecValue(v2));
                    break;
                case 10:
                    if (t1 == 10 && t2 == 10) {
                        if ((v1 instanceof Comparable) && v1.getClass().isAssignableFrom(v2.getClass())) {
                            result = ((Comparable) v1).compareTo(v2);
                            break;
                        } else {
                            throw new IllegalArgumentException("invalid comparison: " + v1.getClass().getName() + " and " + v2.getClass().getName());
                        }
                    }
                    break;
                default:
                    long lv1 = longValue(v1);
                    long lv2 = longValue(v2);
                    if (lv1 == lv2) {
                        return 0;
                    }
                    return lv1 < lv2 ? -1 : 1;
            }
            double dv1 = doubleValue(v1);
            double dv2 = doubleValue(v2);
            if (dv1 == dv2) {
                return 0;
            }
            return dv1 < dv2 ? -1 : 1;
        }
        return result;
    }

    public static boolean isEqual(Object object1, Object object2) {
        boolean result = false;
        if (object1 == object2) {
            result = true;
        } else if (object1 != null && object1.getClass().isArray()) {
            if (object2 != null && object2.getClass().isArray() && object2.getClass() == object1.getClass()) {
                result = Array.getLength(object1) == Array.getLength(object2);
                if (result) {
                    int icount = Array.getLength(object1);
                    for (int i = 0; result && i < icount; i++) {
                        result = isEqual(Array.get(object1, i), Array.get(object2, i));
                    }
                }
            }
        } else {
            result = (object1 == null || object2 == null || (!object1.equals(object2) && compareWithConversion(object1, object2) != 0)) ? false : true;
        }
        return result;
    }

    public static boolean booleanValue(boolean value) {
        return value;
    }

    public static boolean booleanValue(int value) {
        return value > 0;
    }

    public static boolean booleanValue(float value) {
        return value > 0.0f;
    }

    public static boolean booleanValue(long value) {
        return value > 0;
    }

    public static boolean booleanValue(double value) {
        return value > 0.0d;
    }

    public static boolean booleanValue(Object value) {
        if (value == null) {
            return false;
        }
        Class c = value.getClass();
        if (c == Boolean.class) {
            return ((Boolean) value).booleanValue();
        }
        if (c == String.class) {
            return Boolean.parseBoolean(String.valueOf(value));
        }
        return c == Character.class ? ((Character) value).charValue() != 0 : ((value instanceof Number) && ((Number) value).doubleValue() == 0.0d) ? false : true;
    }

    public static long longValue(Object value) throws NumberFormatException {
        if (value == null) {
            return 0L;
        }
        Class c = value.getClass();
        return c.getSuperclass() == Number.class ? ((Number) value).longValue() : c == Boolean.class ? ((Boolean) value).booleanValue() ? 1L : 0L : c == Character.class ? ((Character) value).charValue() : Long.parseLong(stringValue(value, true));
    }

    public static double doubleValue(Object value) throws NumberFormatException {
        if (value == null) {
            return 0.0d;
        }
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number) value).doubleValue();
        }
        if (c == Boolean.class) {
            return ((Boolean) value).booleanValue() ? 1.0d : 0.0d;
        }
        if (c == Character.class) {
            return ((Character) value).charValue();
        }
        String s = stringValue(value, true);
        if (s.length() == 0) {
            return 0.0d;
        }
        return Double.parseDouble(s);
    }

    public static BigInteger bigIntValue(Object value) throws NumberFormatException {
        if (value == null) {
            return BigInteger.valueOf(0L);
        }
        Class c = value.getClass();
        if (c == BigInteger.class) {
            return (BigInteger) value;
        }
        if (c == BigDecimal.class) {
            return ((BigDecimal) value).toBigInteger();
        }
        if (c.getSuperclass() == Number.class) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        if (c == Boolean.class) {
            return BigInteger.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
        }
        return c == Character.class ? BigInteger.valueOf(((Character) value).charValue()) : new BigInteger(stringValue(value, true));
    }

    public static BigDecimal bigDecValue(Object value) throws NumberFormatException {
        if (value == null) {
            return BigDecimal.valueOf(0L);
        }
        Class c = value.getClass();
        if (c == BigDecimal.class) {
            return (BigDecimal) value;
        }
        if (c == BigInteger.class) {
            return new BigDecimal((BigInteger) value);
        }
        if (c == Boolean.class) {
            return BigDecimal.valueOf(((Boolean) value).booleanValue() ? 1L : 0L);
        }
        return c == Character.class ? BigDecimal.valueOf(((Character) value).charValue()) : new BigDecimal(stringValue(value, true));
    }

    public static String stringValue(Object value, boolean trim) {
        String result;
        if (value == null) {
            result = OgnlRuntime.NULL_STRING;
        } else {
            result = value.toString();
            if (trim) {
                result = result.trim();
            }
        }
        return result;
    }

    public static String stringValue(Object value) {
        return stringValue(value, false);
    }

    public static int getNumericType(Object value) {
        if (value != null) {
            Class c = value.getClass();
            if (c == Integer.class) {
                return 4;
            }
            if (c == Double.class) {
                return 8;
            }
            if (c == Boolean.class) {
                return 0;
            }
            if (c == Byte.class) {
                return 1;
            }
            if (c == Character.class) {
                return 2;
            }
            if (c == Short.class) {
                return 3;
            }
            if (c == Long.class) {
                return 5;
            }
            if (c == Float.class) {
                return 7;
            }
            if (c == BigInteger.class) {
                return 6;
            }
            return c == BigDecimal.class ? 9 : 10;
        }
        return 10;
    }

    public static Object toArray(char value, Class toType) {
        return toArray(new Character(value), toType);
    }

    public static Object toArray(byte value, Class toType) {
        return toArray(new Byte(value), toType);
    }

    public static Object toArray(int value, Class toType) {
        return toArray(new Integer(value), toType);
    }

    public static Object toArray(long value, Class toType) {
        return toArray(new Long(value), toType);
    }

    public static Object toArray(float value, Class toType) {
        return toArray(new Float(value), toType);
    }

    public static Object toArray(double value, Class toType) {
        return toArray(new Double(value), toType);
    }

    public static Object toArray(boolean value, Class toType) {
        return toArray(new Boolean(value), toType);
    }

    public static Object convertValue(char value, Class toType) {
        return convertValue(new Character(value), toType);
    }

    public static Object convertValue(byte value, Class toType) {
        return convertValue(new Byte(value), toType);
    }

    public static Object convertValue(int value, Class toType) {
        return convertValue(new Integer(value), toType);
    }

    public static Object convertValue(long value, Class toType) {
        return convertValue(new Long(value), toType);
    }

    public static Object convertValue(float value, Class toType) {
        return convertValue(new Float(value), toType);
    }

    public static Object convertValue(double value, Class toType) {
        return convertValue(new Double(value), toType);
    }

    public static Object convertValue(boolean value, Class toType) {
        return convertValue(new Boolean(value), toType);
    }

    public static Object convertValue(char value, Class toType, boolean preventNull) {
        return convertValue(new Character(value), toType, preventNull);
    }

    public static Object convertValue(byte value, Class toType, boolean preventNull) {
        return convertValue(new Byte(value), toType, preventNull);
    }

    public static Object convertValue(int value, Class toType, boolean preventNull) {
        return convertValue(new Integer(value), toType, preventNull);
    }

    public static Object convertValue(long value, Class toType, boolean preventNull) {
        return convertValue(new Long(value), toType, preventNull);
    }

    public static Object convertValue(float value, Class toType, boolean preventNull) {
        return convertValue(new Float(value), toType, preventNull);
    }

    public static Object convertValue(double value, Class toType, boolean preventNull) {
        return convertValue(new Double(value), toType, preventNull);
    }

    public static Object convertValue(boolean value, Class toType, boolean preventNull) {
        return convertValue(new Boolean(value), toType, preventNull);
    }

    public static Object toArray(char value, Class toType, boolean preventNull) {
        return toArray(new Character(value), toType, preventNull);
    }

    public static Object toArray(byte value, Class toType, boolean preventNull) {
        return toArray(new Byte(value), toType, preventNull);
    }

    public static Object toArray(int value, Class toType, boolean preventNull) {
        return toArray(new Integer(value), toType, preventNull);
    }

    public static Object toArray(long value, Class toType, boolean preventNull) {
        return toArray(new Long(value), toType, preventNull);
    }

    public static Object toArray(float value, Class toType, boolean preventNull) {
        return toArray(new Float(value), toType, preventNull);
    }

    public static Object toArray(double value, Class toType, boolean preventNull) {
        return toArray(new Double(value), toType, preventNull);
    }

    public static Object toArray(boolean value, Class toType, boolean preventNull) {
        return toArray(new Boolean(value), toType, preventNull);
    }

    public static Object convertValue(Object value, Class toType) {
        return convertValue(value, toType, false);
    }

    public static Object toArray(Object value, Class toType) {
        return toArray(value, toType, false);
    }

    public static Object toArray(Object value, Class toType, boolean preventNulls) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        if (value == null) {
            return null;
        }
        if (value.getClass().isArray() && toType.isAssignableFrom(value.getClass().getComponentType())) {
            return value;
        }
        if (!value.getClass().isArray()) {
            if (toType == Character.TYPE) {
                return stringValue(value).toCharArray();
            }
            if (value instanceof Collection) {
                return ((Collection) value).toArray((Object[]) Array.newInstance((Class<?>) toType, 0));
            }
            Object arr = Array.newInstance((Class<?>) toType, 1);
            Array.set(arr, 0, convertValue(value, toType, preventNulls));
            return arr;
        }
        Object result = Array.newInstance((Class<?>) toType, Array.getLength(value));
        int icount = Array.getLength(value);
        for (int i = 0; i < icount; i++) {
            Array.set(result, i, convertValue(Array.get(value, i), toType));
        }
        if (result == null && preventNulls) {
            return value;
        }
        return result;
    }

    public static Object convertValue(Object value, Class toType, boolean preventNulls) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        Object result = null;
        if (value != null && toType.isAssignableFrom(value.getClass())) {
            return value;
        }
        if (value != null) {
            if (value.getClass().isArray() && toType.isArray()) {
                Class componentType = toType.getComponentType();
                result = Array.newInstance((Class<?>) componentType, Array.getLength(value));
                int icount = Array.getLength(value);
                for (int i = 0; i < icount; i++) {
                    Array.set(result, i, convertValue(Array.get(value, i), componentType));
                }
            } else {
                if (value.getClass().isArray() && !toType.isArray()) {
                    return convertValue(Array.get(value, 0), toType);
                }
                if (!value.getClass().isArray() && toType.isArray()) {
                    if (toType.getComponentType() == Character.TYPE) {
                        result = stringValue(value).toCharArray();
                    } else if (toType.getComponentType() == Object.class) {
                        if (value instanceof Collection) {
                            Collection vc = (Collection) value;
                            return vc.toArray(new Object[0]);
                        }
                        return new Object[]{value};
                    }
                } else {
                    if (toType == Integer.class || toType == Integer.TYPE) {
                        result = new Integer((int) longValue(value));
                    }
                    if (toType == Double.class || toType == Double.TYPE) {
                        result = new Double(doubleValue(value));
                    }
                    if (toType == Boolean.class || toType == Boolean.TYPE) {
                        result = booleanValue(value) ? Boolean.TRUE : Boolean.FALSE;
                    }
                    if (toType == Byte.class || toType == Byte.TYPE) {
                        result = new Byte((byte) longValue(value));
                    }
                    if (toType == Character.class || toType == Character.TYPE) {
                        result = new Character((char) longValue(value));
                    }
                    if (toType == Short.class || toType == Short.TYPE) {
                        result = new Short((short) longValue(value));
                    }
                    if (toType == Long.class || toType == Long.TYPE) {
                        result = new Long(longValue(value));
                    }
                    if (toType == Float.class || toType == Float.TYPE) {
                        result = new Float(doubleValue(value));
                    }
                    if (toType == BigInteger.class) {
                        result = bigIntValue(value);
                    }
                    if (toType == BigDecimal.class) {
                        result = bigDecValue(value);
                    }
                    if (toType == String.class) {
                        result = stringValue(value);
                    }
                }
            }
        } else if (toType.isPrimitive()) {
            result = OgnlRuntime.getPrimitiveDefaultValue(toType);
        } else if (preventNulls && toType == Boolean.class) {
            result = Boolean.FALSE;
        } else if (preventNulls && Number.class.isAssignableFrom(toType)) {
            result = OgnlRuntime.getNumericDefaultValue(toType);
        }
        if (result == null && preventNulls) {
            return value;
        }
        if (value != null && result == null) {
            throw new IllegalArgumentException("Unable to convert type " + value.getClass().getName() + " of " + value + " to type of " + toType.getName());
        }
        return result;
    }

    public static int getIntValue(Object value) {
        if (value == null) {
            return -1;
        }
        try {
            if (Number.class.isInstance(value)) {
                return ((Number) value).intValue();
            }
            String str = String.class.isInstance(value) ? (String) value : value.toString();
            return Integer.parseInt(str);
        } catch (Throwable t) {
            throw new RuntimeException("Error converting " + value + " to integer:", t);
        }
    }

    public static int getNumericType(Object v1, Object v2) {
        return getNumericType(v1, v2, false);
    }

    public static int getNumericType(int t1, int t2, boolean canBeNonNumeric) {
        if (t1 == t2) {
            return t1;
        }
        if (canBeNonNumeric && (t1 == 10 || t2 == 10 || t1 == 2 || t2 == 2)) {
            return 10;
        }
        if (t1 == 10) {
            t1 = 8;
        }
        if (t2 == 10) {
            t2 = 8;
        }
        if (t1 >= 7) {
            if (t2 >= 7) {
                return Math.max(t1, t2);
            }
            if (t2 < 4) {
                return t1;
            }
            if (t2 == 6) {
                return 9;
            }
            return Math.max(8, t1);
        }
        if (t2 >= 7) {
            if (t1 < 4) {
                return t2;
            }
            if (t1 == 6) {
                return 9;
            }
            return Math.max(8, t2);
        }
        return Math.max(t1, t2);
    }

    public static int getNumericType(Object v1, Object v2, boolean canBeNonNumeric) {
        return getNumericType(getNumericType(v1), getNumericType(v2), canBeNonNumeric);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Number newInteger(int r5, long r6) {
        /*
            r0 = r5
            switch(r0) {
                case 0: goto L34;
                case 1: goto L6b;
                case 2: goto L34;
                case 3: goto L76;
                case 4: goto L34;
                case 5: goto L62;
                case 6: goto L81;
                case 7: goto L3e;
                case 8: goto L50;
                default: goto L81;
            }
        L34:
            java.lang.Integer r0 = new java.lang.Integer
            r1 = r0
            r2 = r6
            int r2 = (int) r2
            r1.<init>(r2)
            return r0
        L3e:
            r0 = r6
            float r0 = (float) r0
            long r0 = (long) r0
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L50
            java.lang.Float r0 = new java.lang.Float
            r1 = r0
            r2 = r6
            float r2 = (float) r2
            r1.<init>(r2)
            return r0
        L50:
            r0 = r6
            double r0 = (double) r0
            long r0 = (long) r0
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L62
            java.lang.Double r0 = new java.lang.Double
            r1 = r0
            r2 = r6
            double r2 = (double) r2
            r1.<init>(r2)
            return r0
        L62:
            java.lang.Long r0 = new java.lang.Long
            r1 = r0
            r2 = r6
            r1.<init>(r2)
            return r0
        L6b:
            java.lang.Byte r0 = new java.lang.Byte
            r1 = r0
            r2 = r6
            int r2 = (int) r2
            byte r2 = (byte) r2
            r1.<init>(r2)
            return r0
        L76:
            java.lang.Short r0 = new java.lang.Short
            r1 = r0
            r2 = r6
            int r2 = (int) r2
            short r2 = (short) r2
            r1.<init>(r2)
            return r0
        L81:
            r0 = r6
            java.math.BigInteger r0 = java.math.BigInteger.valueOf(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.ibatis.ognl.OgnlOps.newInteger(int, long):java.lang.Number");
    }

    public static Number newReal(int type, double value) {
        return type == 7 ? new Float((float) value) : new Double(value);
    }

    public static Object binaryOr(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        return (type == 6 || type == 9) ? bigIntValue(v1).or(bigIntValue(v2)) : newInteger(type, longValue(v1) | longValue(v2));
    }

    public static Object binaryXor(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        return (type == 6 || type == 9) ? bigIntValue(v1).xor(bigIntValue(v2)) : newInteger(type, longValue(v1) ^ longValue(v2));
    }

    public static Object binaryAnd(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        return (type == 6 || type == 9) ? bigIntValue(v1).and(bigIntValue(v2)) : newInteger(type, longValue(v1) & longValue(v2));
    }

    public static boolean equal(Object v1, Object v2) {
        if (v1 == null) {
            return v2 == null;
        }
        if (v1 == v2 || isEqual(v1, v2)) {
            return true;
        }
        return (v1 instanceof Number) && (v2 instanceof Number) && ((Number) v1).doubleValue() == ((Number) v2).doubleValue();
    }

    public static boolean less(Object v1, Object v2) {
        return compareWithConversion(v1, v2) < 0;
    }

    public static boolean greater(Object v1, Object v2) {
        return compareWithConversion(v1, v2) > 0;
    }

    public static boolean in(Object v1, Object v2) throws OgnlException {
        if (v2 == null) {
            return false;
        }
        ElementsAccessor elementsAccessor = OgnlRuntime.getElementsAccessor(OgnlRuntime.getTargetClass(v2));
        Enumeration e = elementsAccessor.getElements(v2);
        while (e.hasMoreElements()) {
            Object o = e.nextElement();
            if (equal(v1, o)) {
                return true;
            }
        }
        return false;
    }

    public static Object shiftLeft(Object v1, Object v2) {
        int type = getNumericType(v1);
        return (type == 6 || type == 9) ? bigIntValue(v1).shiftLeft((int) longValue(v2)) : newInteger(type, longValue(v1) << ((int) longValue(v2)));
    }

    public static Object shiftRight(Object v1, Object v2) {
        int type = getNumericType(v1);
        return (type == 6 || type == 9) ? bigIntValue(v1).shiftRight((int) longValue(v2)) : newInteger(type, longValue(v1) >> ((int) longValue(v2)));
    }

    public static Object unsignedShiftRight(Object v1, Object v2) {
        int type = getNumericType(v1);
        return (type == 6 || type == 9) ? bigIntValue(v1).shiftRight((int) longValue(v2)) : type <= 4 ? newInteger(4, ((int) longValue(v1)) >>> ((int) longValue(v2))) : newInteger(type, longValue(v1) >>> ((int) longValue(v2)));
    }

    public static Object add(Object v1, Object v2) {
        int type = getNumericType(v1, v2, true);
        switch (type) {
            case 6:
                return bigIntValue(v1).add(bigIntValue(v2));
            case 7:
            case 8:
                return newReal(type, doubleValue(v1) + doubleValue(v2));
            case 9:
                return bigDecValue(v1).add(bigDecValue(v2));
            case 10:
                int t1 = getNumericType(v1);
                int t2 = getNumericType(v2);
                if ((t1 != 10 && v2 == null) || (t2 != 10 && v1 == null)) {
                    throw new NullPointerException("Can't add values " + v1 + " , " + v2);
                }
                return stringValue(v1) + stringValue(v2);
            default:
                return newInteger(type, longValue(v1) + longValue(v2));
        }
    }

    public static Object subtract(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        switch (type) {
            case 6:
                return bigIntValue(v1).subtract(bigIntValue(v2));
            case 7:
            case 8:
                return newReal(type, doubleValue(v1) - doubleValue(v2));
            case 9:
                return bigDecValue(v1).subtract(bigDecValue(v2));
            default:
                return newInteger(type, longValue(v1) - longValue(v2));
        }
    }

    public static Object multiply(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        switch (type) {
            case 6:
                return bigIntValue(v1).multiply(bigIntValue(v2));
            case 7:
            case 8:
                return newReal(type, doubleValue(v1) * doubleValue(v2));
            case 9:
                return bigDecValue(v1).multiply(bigDecValue(v2));
            default:
                return newInteger(type, longValue(v1) * longValue(v2));
        }
    }

    public static Object divide(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        switch (type) {
            case 6:
                return bigIntValue(v1).divide(bigIntValue(v2));
            case 7:
            case 8:
                return newReal(type, doubleValue(v1) / doubleValue(v2));
            case 9:
                return bigDecValue(v1).divide(bigDecValue(v2), 6);
            default:
                return newInteger(type, longValue(v1) / longValue(v2));
        }
    }

    public static Object remainder(Object v1, Object v2) {
        int type = getNumericType(v1, v2);
        switch (type) {
            case 6:
            case 9:
                return bigIntValue(v1).remainder(bigIntValue(v2));
            default:
                return newInteger(type, longValue(v1) % longValue(v2));
        }
    }

    public static Object negate(Object value) {
        int type = getNumericType(value);
        switch (type) {
            case 6:
                return bigIntValue(value).negate();
            case 7:
            case 8:
                return newReal(type, -doubleValue(value));
            case 9:
                return bigDecValue(value).negate();
            default:
                return newInteger(type, -longValue(value));
        }
    }

    public static Object bitNegate(Object value) {
        int type = getNumericType(value);
        switch (type) {
            case 6:
            case 9:
                return bigIntValue(value).not();
            default:
                return newInteger(type, longValue(value) ^ (-1));
        }
    }

    public static String getEscapeString(String value) {
        StringBuffer result = new StringBuffer();
        int icount = value.length();
        for (int i = 0; i < icount; i++) {
            result.append(getEscapedChar(value.charAt(i)));
        }
        return new String(result);
    }

    public static String getEscapedChar(char ch2) {
        String result;
        switch (ch2) {
            case '\b':
                result = "\b";
                break;
            case '\t':
                result = "\\t";
                break;
            case '\n':
                result = "\\n";
                break;
            case '\f':
                result = "\\f";
                break;
            case '\r':
                result = "\\r";
                break;
            case '\"':
                result = "\\\"";
                break;
            case '\'':
                result = "\\'";
                break;
            case '\\':
                result = "\\\\";
                break;
            default:
                if (Character.isISOControl(ch2)) {
                    String hc = Integer.toString(ch2, 16);
                    int hcl = hc.length();
                    String result2 = "\\u";
                    if (hcl < 4) {
                        if (hcl == 3) {
                            result2 = result2 + "0";
                        } else if (hcl == 2) {
                            result2 = result2 + TarConstants.VERSION_POSIX;
                        } else {
                            result2 = result2 + "000";
                        }
                    }
                    result = result2 + hc;
                    break;
                } else {
                    result = new String(ch2 + "");
                    break;
                }
        }
        return result;
    }

    public static Object returnValue(Object ignore, Object returnValue) {
        return returnValue;
    }

    public static RuntimeException castToRuntime(Throwable t) {
        if (RuntimeException.class.isInstance(t)) {
            return (RuntimeException) t;
        }
        if (OgnlException.class.isInstance(t)) {
            throw new UnsupportedCompilationException("Error evluating expression: " + t.getMessage(), t);
        }
        return new RuntimeException(t);
    }
}
