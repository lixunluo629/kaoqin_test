package org.apache.ibatis.reflection.property;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.util.Locale;
import org.apache.ibatis.reflection.ReflectionException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/property/PropertyNamer.class */
public final class PropertyNamer {
    private PropertyNamer() {
    }

    public static String methodToProperty(String name) {
        String name2;
        if (name.startsWith(BeanUtil.PREFIX_GETTER_IS)) {
            name2 = name.substring(2);
        } else if (name.startsWith(BeanUtil.PREFIX_GETTER_GET) || name.startsWith("set")) {
            name2 = name.substring(3);
        } else {
            throw new ReflectionException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
        }
        if (name2.length() == 1 || (name2.length() > 1 && !Character.isUpperCase(name2.charAt(1)))) {
            name2 = name2.substring(0, 1).toLowerCase(Locale.ENGLISH) + name2.substring(1);
        }
        return name2;
    }

    public static boolean isProperty(String name) {
        return name.startsWith(BeanUtil.PREFIX_GETTER_GET) || name.startsWith("set") || name.startsWith(BeanUtil.PREFIX_GETTER_IS);
    }

    public static boolean isGetter(String name) {
        return name.startsWith(BeanUtil.PREFIX_GETTER_GET) || name.startsWith(BeanUtil.PREFIX_GETTER_IS);
    }

    public static boolean isSetter(String name) {
        return name.startsWith("set");
    }
}
