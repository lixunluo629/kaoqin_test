package org.apache.ibatis.type;

import io.swagger.models.properties.StringProperty;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.io.ResolverUtil;
import org.apache.ibatis.io.Resources;
import org.apache.xmlbeans.XmlErrorCodes;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/type/TypeAliasRegistry.class */
public class TypeAliasRegistry {
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap();

    public TypeAliasRegistry() {
        registerAlias(StringProperty.TYPE, String.class);
        registerAlias("byte", Byte.class);
        registerAlias(XmlErrorCodes.LONG, Long.class);
        registerAlias("short", Short.class);
        registerAlias(XmlErrorCodes.INT, Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias(XmlErrorCodes.DOUBLE, Double.class);
        registerAlias(XmlErrorCodes.FLOAT, Float.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("byte[]", Byte[].class);
        registerAlias("long[]", Long[].class);
        registerAlias("short[]", Short[].class);
        registerAlias("int[]", Integer[].class);
        registerAlias("integer[]", Integer[].class);
        registerAlias("double[]", Double[].class);
        registerAlias("float[]", Float[].class);
        registerAlias("boolean[]", Boolean[].class);
        registerAlias("_byte", Byte.TYPE);
        registerAlias("_long", Long.TYPE);
        registerAlias("_short", Short.TYPE);
        registerAlias("_int", Integer.TYPE);
        registerAlias("_integer", Integer.TYPE);
        registerAlias("_double", Double.TYPE);
        registerAlias("_float", Float.TYPE);
        registerAlias("_boolean", Boolean.TYPE);
        registerAlias("_byte[]", byte[].class);
        registerAlias("_long[]", long[].class);
        registerAlias("_short[]", short[].class);
        registerAlias("_int[]", int[].class);
        registerAlias("_integer[]", int[].class);
        registerAlias("_double[]", double[].class);
        registerAlias("_float[]", float[].class);
        registerAlias("_boolean[]", boolean[].class);
        registerAlias("date", Date.class);
        registerAlias(XmlErrorCodes.DECIMAL, BigDecimal.class);
        registerAlias("bigdecimal", BigDecimal.class);
        registerAlias("biginteger", BigInteger.class);
        registerAlias("object", Object.class);
        registerAlias("date[]", Date[].class);
        registerAlias("decimal[]", BigDecimal[].class);
        registerAlias("bigdecimal[]", BigDecimal[].class);
        registerAlias("biginteger[]", BigInteger[].class);
        registerAlias("object[]", Object[].class);
        registerAlias(BeanDefinitionParserDelegate.MAP_ELEMENT, Map.class);
        registerAlias("hashmap", HashMap.class);
        registerAlias("list", List.class);
        registerAlias("arraylist", ArrayList.class);
        registerAlias("collection", Collection.class);
        registerAlias("iterator", Iterator.class);
        registerAlias("ResultSet", ResultSet.class);
    }

    public <T> Class<T> resolveAlias(String str) {
        Class<?> clsClassForName;
        if (str == null) {
            return null;
        }
        try {
            String lowerCase = str.toLowerCase(Locale.ENGLISH);
            if (this.TYPE_ALIASES.containsKey(lowerCase)) {
                clsClassForName = this.TYPE_ALIASES.get(lowerCase);
            } else {
                clsClassForName = Resources.classForName(str);
            }
            return (Class<T>) clsClassForName;
        } catch (ClassNotFoundException e) {
            throw new TypeException("Could not resolve type alias '" + str + "'.  Cause: " + e, e);
        }
    }

    public void registerAliases(String packageName) {
        registerAliases(packageName, Object.class);
    }

    public void registerAliases(String packageName, Class<?> superType) {
        ResolverUtil<Class<?>> resolverUtil = new ResolverUtil<>();
        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
        Set<Class<? extends Class<?>>> typeSet = resolverUtil.getClasses();
        for (Class<?> type : typeSet) {
            if (!type.isAnonymousClass() && !type.isInterface() && !type.isMemberClass()) {
                registerAlias(type);
            }
        }
    }

    public void registerAlias(Class<?> type) {
        String alias = type.getSimpleName();
        Alias aliasAnnotation = (Alias) type.getAnnotation(Alias.class);
        if (aliasAnnotation != null) {
            alias = aliasAnnotation.value();
        }
        registerAlias(alias, type);
    }

    public void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            throw new TypeException("The parameter alias cannot be null");
        }
        String key = alias.toLowerCase(Locale.ENGLISH);
        if (this.TYPE_ALIASES.containsKey(key) && this.TYPE_ALIASES.get(key) != null && !this.TYPE_ALIASES.get(key).equals(value)) {
            throw new TypeException("The alias '" + alias + "' is already mapped to the value '" + this.TYPE_ALIASES.get(key).getName() + "'.");
        }
        this.TYPE_ALIASES.put(key, value);
    }

    public void registerAlias(String alias, String value) {
        try {
            registerAlias(alias, Resources.classForName(value));
        } catch (ClassNotFoundException e) {
            throw new TypeException("Error registering type alias " + alias + " for " + value + ". Cause: " + e, e);
        }
    }

    public Map<String, Class<?>> getTypeAliases() {
        return Collections.unmodifiableMap(this.TYPE_ALIASES);
    }
}
