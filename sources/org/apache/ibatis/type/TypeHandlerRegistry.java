package org.apache.ibatis.type;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.reflection.Jdk;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/TypeHandlerRegistry.class */
public final class TypeHandlerRegistry {
    private static final Map<JdbcType, TypeHandler<?>> NULL_TYPE_HANDLER_MAP = Collections.emptyMap();
    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap(JdbcType.class);
    private final Map<Type, Map<JdbcType, TypeHandler<?>>> TYPE_HANDLER_MAP = new ConcurrentHashMap();
    private final TypeHandler<Object> UNKNOWN_TYPE_HANDLER = new UnknownTypeHandler(this);
    private final Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap();
    private Class<? extends TypeHandler> defaultEnumTypeHandler = EnumTypeHandler.class;

    public TypeHandlerRegistry() {
        register(Boolean.class, (TypeHandler) new BooleanTypeHandler());
        register(Boolean.TYPE, (TypeHandler) new BooleanTypeHandler());
        register(JdbcType.BOOLEAN, new BooleanTypeHandler());
        register(JdbcType.BIT, new BooleanTypeHandler());
        register(Byte.class, (TypeHandler) new ByteTypeHandler());
        register(Byte.TYPE, (TypeHandler) new ByteTypeHandler());
        register(JdbcType.TINYINT, new ByteTypeHandler());
        register(Short.class, (TypeHandler) new ShortTypeHandler());
        register(Short.TYPE, (TypeHandler) new ShortTypeHandler());
        register(JdbcType.SMALLINT, new ShortTypeHandler());
        register(Integer.class, (TypeHandler) new IntegerTypeHandler());
        register(Integer.TYPE, (TypeHandler) new IntegerTypeHandler());
        register(JdbcType.INTEGER, new IntegerTypeHandler());
        register(Long.class, (TypeHandler) new LongTypeHandler());
        register(Long.TYPE, (TypeHandler) new LongTypeHandler());
        register(Float.class, (TypeHandler) new FloatTypeHandler());
        register(Float.TYPE, (TypeHandler) new FloatTypeHandler());
        register(JdbcType.FLOAT, new FloatTypeHandler());
        register(Double.class, (TypeHandler) new DoubleTypeHandler());
        register(Double.TYPE, (TypeHandler) new DoubleTypeHandler());
        register(JdbcType.DOUBLE, new DoubleTypeHandler());
        register(Reader.class, (TypeHandler) new ClobReaderTypeHandler());
        register(String.class, (TypeHandler) new StringTypeHandler());
        register(String.class, JdbcType.CHAR, (TypeHandler) new StringTypeHandler());
        register(String.class, JdbcType.CLOB, (TypeHandler) new ClobTypeHandler());
        register(String.class, JdbcType.VARCHAR, (TypeHandler) new StringTypeHandler());
        register(String.class, JdbcType.LONGVARCHAR, (TypeHandler) new ClobTypeHandler());
        register(String.class, JdbcType.NVARCHAR, (TypeHandler) new NStringTypeHandler());
        register(String.class, JdbcType.NCHAR, (TypeHandler) new NStringTypeHandler());
        register(String.class, JdbcType.NCLOB, (TypeHandler) new NClobTypeHandler());
        register(JdbcType.CHAR, new StringTypeHandler());
        register(JdbcType.VARCHAR, new StringTypeHandler());
        register(JdbcType.CLOB, new ClobTypeHandler());
        register(JdbcType.LONGVARCHAR, new ClobTypeHandler());
        register(JdbcType.NVARCHAR, new NStringTypeHandler());
        register(JdbcType.NCHAR, new NStringTypeHandler());
        register(JdbcType.NCLOB, new NClobTypeHandler());
        register(Object.class, JdbcType.ARRAY, (TypeHandler) new ArrayTypeHandler());
        register(JdbcType.ARRAY, new ArrayTypeHandler());
        register(BigInteger.class, (TypeHandler) new BigIntegerTypeHandler());
        register(JdbcType.BIGINT, new LongTypeHandler());
        register(BigDecimal.class, (TypeHandler) new BigDecimalTypeHandler());
        register(JdbcType.REAL, new BigDecimalTypeHandler());
        register(JdbcType.DECIMAL, new BigDecimalTypeHandler());
        register(JdbcType.NUMERIC, new BigDecimalTypeHandler());
        register(InputStream.class, (TypeHandler) new BlobInputStreamTypeHandler());
        register(Byte[].class, (TypeHandler) new ByteObjectArrayTypeHandler());
        register(Byte[].class, JdbcType.BLOB, (TypeHandler) new BlobByteObjectArrayTypeHandler());
        register(Byte[].class, JdbcType.LONGVARBINARY, (TypeHandler) new BlobByteObjectArrayTypeHandler());
        register(byte[].class, (TypeHandler) new ByteArrayTypeHandler());
        register(byte[].class, JdbcType.BLOB, (TypeHandler) new BlobTypeHandler());
        register(byte[].class, JdbcType.LONGVARBINARY, (TypeHandler) new BlobTypeHandler());
        register(JdbcType.LONGVARBINARY, new BlobTypeHandler());
        register(JdbcType.BLOB, new BlobTypeHandler());
        register(Object.class, (TypeHandler) this.UNKNOWN_TYPE_HANDLER);
        register(Object.class, JdbcType.OTHER, (TypeHandler) this.UNKNOWN_TYPE_HANDLER);
        register(JdbcType.OTHER, this.UNKNOWN_TYPE_HANDLER);
        register(Date.class, (TypeHandler) new DateTypeHandler());
        register(Date.class, JdbcType.DATE, (TypeHandler) new DateOnlyTypeHandler());
        register(Date.class, JdbcType.TIME, (TypeHandler) new TimeOnlyTypeHandler());
        register(JdbcType.TIMESTAMP, new DateTypeHandler());
        register(JdbcType.DATE, new DateOnlyTypeHandler());
        register(JdbcType.TIME, new TimeOnlyTypeHandler());
        register(java.sql.Date.class, (TypeHandler) new SqlDateTypeHandler());
        register(Time.class, (TypeHandler) new SqlTimeTypeHandler());
        register(Timestamp.class, (TypeHandler) new SqlTimestampTypeHandler());
        if (Jdk.dateAndTimeApiExists) {
            register(Instant.class, InstantTypeHandler.class);
            register(LocalDateTime.class, LocalDateTimeTypeHandler.class);
            register(LocalDate.class, LocalDateTypeHandler.class);
            register(LocalTime.class, LocalTimeTypeHandler.class);
            register(OffsetDateTime.class, OffsetDateTimeTypeHandler.class);
            register(OffsetTime.class, OffsetTimeTypeHandler.class);
            register(ZonedDateTime.class, ZonedDateTimeTypeHandler.class);
            register(Month.class, MonthTypeHandler.class);
            register(Year.class, YearTypeHandler.class);
            register(YearMonth.class, YearMonthTypeHandler.class);
            register(JapaneseDate.class, JapaneseDateTypeHandler.class);
        }
        register(Character.class, (TypeHandler) new CharacterTypeHandler());
        register(Character.TYPE, (TypeHandler) new CharacterTypeHandler());
    }

    public void setDefaultEnumTypeHandler(Class<? extends TypeHandler> typeHandler) {
        this.defaultEnumTypeHandler = typeHandler;
    }

    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, (JdbcType) null);
    }

    public boolean hasTypeHandler(TypeReference<?> javaTypeReference) {
        return hasTypeHandler(javaTypeReference, (JdbcType) null);
    }

    public boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return (javaType == null || getTypeHandler((Type) javaType, jdbcType) == null) ? false : true;
    }

    public boolean hasTypeHandler(TypeReference<?> javaTypeReference, JdbcType jdbcType) {
        return (javaTypeReference == null || getTypeHandler(javaTypeReference, jdbcType) == null) ? false : true;
    }

    public TypeHandler<?> getMappingTypeHandler(Class<? extends TypeHandler<?>> handlerType) {
        return this.ALL_TYPE_HANDLERS_MAP.get(handlerType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler((Type) type, (JdbcType) null);
    }

    public <T> TypeHandler<T> getTypeHandler(TypeReference<T> javaTypeReference) {
        return getTypeHandler(javaTypeReference, (JdbcType) null);
    }

    public TypeHandler<?> getTypeHandler(JdbcType jdbcType) {
        return this.JDBC_TYPE_HANDLER_MAP.get(jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType) {
        return getTypeHandler((Type) type, jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(TypeReference<T> javaTypeReference, JdbcType jdbcType) {
        return getTypeHandler(javaTypeReference.getRawType(), jdbcType);
    }

    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType) {
        if (MapperMethod.ParamMap.class.equals(type)) {
            return null;
        }
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = getJdbcHandlerMap(type);
        TypeHandler<?> typeHandlerPickSoleHandler = null;
        if (jdbcHandlerMap != null) {
            typeHandlerPickSoleHandler = jdbcHandlerMap.get(jdbcType);
            if (typeHandlerPickSoleHandler == null) {
                typeHandlerPickSoleHandler = jdbcHandlerMap.get(null);
            }
            if (typeHandlerPickSoleHandler == null) {
                typeHandlerPickSoleHandler = pickSoleHandler(jdbcHandlerMap);
            }
        }
        return (TypeHandler<T>) typeHandlerPickSoleHandler;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMap(Type type) {
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = this.TYPE_HANDLER_MAP.get(type);
        if (NULL_TYPE_HANDLER_MAP.equals(jdbcHandlerMap)) {
            return null;
        }
        if (jdbcHandlerMap == null && (type instanceof Class)) {
            Class<?> clazz = (Class) type;
            if (clazz.isEnum()) {
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(clazz, clazz);
                if (jdbcHandlerMap == null) {
                    register((Type) clazz, getInstance(clazz, this.defaultEnumTypeHandler));
                    return this.TYPE_HANDLER_MAP.get(clazz);
                }
            } else {
                jdbcHandlerMap = getJdbcHandlerMapForSuperclass(clazz);
            }
        }
        this.TYPE_HANDLER_MAP.put(type, jdbcHandlerMap == null ? NULL_TYPE_HANDLER_MAP : jdbcHandlerMap);
        return jdbcHandlerMap;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMapForEnumInterfaces(Class<?> clazz, Class<?> enumClazz) {
        for (Class<?> iface : clazz.getInterfaces()) {
            Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = this.TYPE_HANDLER_MAP.get(iface);
            if (jdbcHandlerMap == null) {
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(iface, enumClazz);
            }
            if (jdbcHandlerMap != null) {
                HashMap<JdbcType, TypeHandler<?>> newMap = new HashMap<>();
                for (Map.Entry<JdbcType, TypeHandler<?>> entry : jdbcHandlerMap.entrySet()) {
                    newMap.put(entry.getKey(), getInstance(enumClazz, entry.getValue().getClass()));
                }
                return newMap;
            }
        }
        return null;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMapForSuperclass(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        if (superclass == null || Object.class.equals(superclass)) {
            return null;
        }
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = this.TYPE_HANDLER_MAP.get(superclass);
        if (jdbcHandlerMap != null) {
            return jdbcHandlerMap;
        }
        return getJdbcHandlerMapForSuperclass(superclass);
    }

    private TypeHandler<?> pickSoleHandler(Map<JdbcType, TypeHandler<?>> jdbcHandlerMap) {
        TypeHandler<?> soleHandler = null;
        for (TypeHandler<?> handler : jdbcHandlerMap.values()) {
            if (soleHandler == null) {
                soleHandler = handler;
            } else if (!handler.getClass().equals(soleHandler.getClass())) {
                return null;
            }
        }
        return soleHandler;
    }

    public TypeHandler<Object> getUnknownTypeHandler() {
        return this.UNKNOWN_TYPE_HANDLER;
    }

    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        this.JDBC_TYPE_HANDLER_MAP.put(jdbcType, handler);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> void register(TypeHandler<T> typeHandler) {
        boolean mappedTypeFound = false;
        MappedTypes mappedTypes = (MappedTypes) typeHandler.getClass().getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class<?> handledType : mappedTypes.value()) {
                register((Type) handledType, (TypeHandler) typeHandler);
                mappedTypeFound = true;
            }
        }
        if (!mappedTypeFound && (typeHandler instanceof TypeReference)) {
            try {
                TypeReference<T> typeReference = (TypeReference) typeHandler;
                register(typeReference.getRawType(), typeHandler);
                mappedTypeFound = true;
            } catch (Throwable th) {
            }
        }
        if (!mappedTypeFound) {
            register((Class) null, (TypeHandler) typeHandler);
        }
    }

    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler) {
        register((Type) javaType, (TypeHandler) typeHandler);
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        MappedJdbcTypes mappedJdbcTypes = (MappedJdbcTypes) typeHandler.getClass().getAnnotation(MappedJdbcTypes.class);
        if (mappedJdbcTypes != null) {
            for (JdbcType handledJdbcType : mappedJdbcTypes.value()) {
                register(javaType, handledJdbcType, typeHandler);
            }
            if (mappedJdbcTypes.includeNullJdbcType()) {
                register(javaType, (JdbcType) null, typeHandler);
                return;
            }
            return;
        }
        register(javaType, (JdbcType) null, typeHandler);
    }

    public <T> void register(TypeReference<T> javaTypeReference, TypeHandler<? extends T> handler) {
        register(javaTypeReference.getRawType(), handler);
    }

    public <T> void register(Class<T> type, JdbcType jdbcType, TypeHandler<? extends T> handler) {
        register((Type) type, jdbcType, (TypeHandler<?>) handler);
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (javaType != null) {
            Map<JdbcType, TypeHandler<?>> map = this.TYPE_HANDLER_MAP.get(javaType);
            if (map == null || map == NULL_TYPE_HANDLER_MAP) {
                map = new HashMap();
                this.TYPE_HANDLER_MAP.put(javaType, map);
            }
            map.put(jdbcType, handler);
        }
        this.ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }

    public void register(Class<?> typeHandlerClass) {
        boolean mappedTypeFound = false;
        MappedTypes mappedTypes = (MappedTypes) typeHandlerClass.getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class<?> javaTypeClass : mappedTypes.value()) {
                register(javaTypeClass, typeHandlerClass);
                mappedTypeFound = true;
            }
        }
        if (!mappedTypeFound) {
            register(getInstance(null, typeHandlerClass));
        }
    }

    public void register(String javaTypeClassName, String typeHandlerClassName) throws ClassNotFoundException {
        register(Resources.classForName(javaTypeClassName), Resources.classForName(typeHandlerClassName));
    }

    public void register(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        register((Type) javaTypeClass, getInstance(javaTypeClass, typeHandlerClass));
    }

    public void register(Class<?> javaTypeClass, JdbcType jdbcType, Class<?> typeHandlerClass) {
        register((Type) javaTypeClass, jdbcType, getInstance(javaTypeClass, typeHandlerClass));
    }

    public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        if (javaTypeClass != null) {
            try {
                Constructor<?> c = typeHandlerClass.getConstructor(Class.class);
                return (TypeHandler) c.newInstance(javaTypeClass);
            } catch (NoSuchMethodException e) {
            } catch (Exception e2) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, e2);
            }
        }
        try {
            Constructor<?> c2 = typeHandlerClass.getConstructor(new Class[0]);
            return (TypeHandler) c2.newInstance(new Object[0]);
        } catch (Exception e3) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, e3);
        }
    }

    public void register(String packageName) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(TypeHandler.class), packageName);
        Set<Class<? extends Class<?>>> handlerSet = resolverUtil.getClasses();
        for (Class<?> type : handlerSet) {
            if (!type.isAnonymousClass() && !type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
                register(type);
            }
        }
    }

    public Collection<TypeHandler<?>> getTypeHandlers() {
        return Collections.unmodifiableCollection(this.ALL_TYPE_HANDLERS_MAP.values());
    }
}
