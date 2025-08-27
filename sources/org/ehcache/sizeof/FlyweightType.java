package org.ehcache.sizeof;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.net.Proxy;
import java.nio.charset.CodingErrorAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.namespace.QName;

/* compiled from: FlyWeightType.java */
/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/FlyweightType.class */
enum FlyweightType {
    ENUM(Enum.class) { // from class: org.ehcache.sizeof.FlyweightType.1
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return true;
        }
    },
    CLASS(Class.class) { // from class: org.ehcache.sizeof.FlyweightType.2
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return true;
        }
    },
    BOOLEAN(Boolean.class) { // from class: org.ehcache.sizeof.FlyweightType.3
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == Boolean.TRUE || obj == Boolean.FALSE;
        }
    },
    INTEGER(Integer.class) { // from class: org.ehcache.sizeof.FlyweightType.4
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            int value = ((Integer) obj).intValue();
            return value >= -128 && value <= 127 && obj == Integer.valueOf(value);
        }
    },
    SHORT(Short.class) { // from class: org.ehcache.sizeof.FlyweightType.5
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            short value = ((Short) obj).shortValue();
            return value >= -128 && value <= 127 && obj == Short.valueOf(value);
        }
    },
    BYTE(Byte.class) { // from class: org.ehcache.sizeof.FlyweightType.6
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == Byte.valueOf(((Byte) obj).byteValue());
        }
    },
    LONG(Long.class) { // from class: org.ehcache.sizeof.FlyweightType.7
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            long value = ((Long) obj).longValue();
            return value >= -128 && value <= 127 && obj == Long.valueOf(value);
        }
    },
    BIGINTEGER(BigInteger.class) { // from class: org.ehcache.sizeof.FlyweightType.8
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == BigInteger.ZERO || obj == BigInteger.ONE || obj == BigInteger.TEN;
        }
    },
    BIGDECIMAL(BigDecimal.class) { // from class: org.ehcache.sizeof.FlyweightType.9
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == BigDecimal.ZERO || obj == BigDecimal.ONE || obj == BigDecimal.TEN;
        }
    },
    MATHCONTEXT(MathContext.class) { // from class: org.ehcache.sizeof.FlyweightType.10
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == MathContext.UNLIMITED || obj == MathContext.DECIMAL32 || obj == MathContext.DECIMAL64 || obj == MathContext.DECIMAL128;
        }
    },
    CHARACTER(Character.class) { // from class: org.ehcache.sizeof.FlyweightType.11
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return ((Character) obj).charValue() <= 127 && obj == Character.valueOf(((Character) obj).charValue());
        }
    },
    LOCALE(Locale.class) { // from class: org.ehcache.sizeof.FlyweightType.12
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return (obj instanceof Locale) && FlyweightType.GLOBAL_LOCALES.contains(obj);
        }
    },
    LOGGER(Logger.class) { // from class: org.ehcache.sizeof.FlyweightType.13
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == Logger.global;
        }
    },
    PROXY(Proxy.class) { // from class: org.ehcache.sizeof.FlyweightType.14
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == Proxy.NO_PROXY;
        }
    },
    CODINGERRORACTION(CodingErrorAction.class) { // from class: org.ehcache.sizeof.FlyweightType.15
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return true;
        }
    },
    DATATYPECONSTANTS_FIELD(DatatypeConstants.Field.class) { // from class: org.ehcache.sizeof.FlyweightType.16
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return true;
        }
    },
    QNAME(QName.class) { // from class: org.ehcache.sizeof.FlyweightType.17
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            return obj == DatatypeConstants.DATETIME || obj == DatatypeConstants.TIME || obj == DatatypeConstants.DATE || obj == DatatypeConstants.GYEARMONTH || obj == DatatypeConstants.GMONTHDAY || obj == DatatypeConstants.GYEAR || obj == DatatypeConstants.GMONTH || obj == DatatypeConstants.GDAY || obj == DatatypeConstants.DURATION || obj == DatatypeConstants.DURATION_DAYTIME || obj == DatatypeConstants.DURATION_YEARMONTH;
        }
    },
    MISC(Void.class) { // from class: org.ehcache.sizeof.FlyweightType.18
        @Override // org.ehcache.sizeof.FlyweightType
        boolean isShared(Object obj) {
            boolean emptyCollection = obj == Collections.EMPTY_SET || obj == Collections.EMPTY_LIST || obj == Collections.EMPTY_MAP;
            boolean systemStream = obj == System.in || obj == System.out || obj == System.err;
            return emptyCollection || systemStream || obj == String.CASE_INSENSITIVE_ORDER;
        }
    };

    private static final Map<Class<?>, FlyweightType> TYPE_MAPPINGS = new HashMap();
    private static final Set<Locale> GLOBAL_LOCALES;
    private final Class<?> clazz;

    abstract boolean isShared(Object obj);

    static {
        FlyweightType[] arr$ = values();
        for (FlyweightType type : arr$) {
            TYPE_MAPPINGS.put(type.clazz, type);
        }
        Map<Locale, Void> locales = new IdentityHashMap<>();
        Field[] arr$2 = Locale.class.getFields();
        for (Field f : arr$2) {
            int modifiers = f.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers) && Locale.class.equals(f.getType())) {
                try {
                    locales.put((Locale) f.get(null), null);
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e2) {
                }
            }
        }
        GLOBAL_LOCALES = locales.keySet();
    }

    FlyweightType(Class cls) {
        this.clazz = cls;
    }

    static FlyweightType getFlyweightType(Class<?> aClazz) {
        if (aClazz.isEnum() || (aClazz.getSuperclass() != null && aClazz.getSuperclass().isEnum())) {
            return ENUM;
        }
        FlyweightType flyweightType = TYPE_MAPPINGS.get(aClazz);
        return flyweightType != null ? flyweightType : MISC;
    }
}
