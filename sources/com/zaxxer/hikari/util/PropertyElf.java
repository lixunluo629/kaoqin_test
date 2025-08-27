package com.zaxxer.hikari.util;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.zaxxer.hikari.HikariConfig;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: HikariCP-3.4.5.jar:com/zaxxer/hikari/util/PropertyElf.class */
public final class PropertyElf {
    private static final Pattern GETTER_PATTERN = Pattern.compile("(get|is)[A-Z].+");

    private PropertyElf() {
    }

    public static void setTargetFromProperties(Object target, Properties properties) {
        if (target == null || properties == null) {
            return;
        }
        List<Method> methods = Arrays.asList(target.getClass().getMethods());
        properties.forEach((key, value) -> {
            if ((target instanceof HikariConfig) && key.toString().startsWith("dataSource.")) {
                ((HikariConfig) target).addDataSourceProperty(key.toString().substring("dataSource.".length()), value);
            } else {
                setProperty(target, key.toString(), value, methods);
            }
        });
    }

    public static Set<String> getPropertyNames(Class<?> targetClass) throws SecurityException {
        HashSet<String> set = new HashSet<>();
        Matcher matcher = GETTER_PATTERN.matcher("");
        for (Method method : targetClass.getMethods()) {
            String name = method.getName();
            if (method.getParameterTypes().length == 0 && matcher.reset(name).matches()) {
                String name2 = name.replaceFirst("(get|is)", "");
                try {
                    if (targetClass.getMethod("set" + name2, method.getReturnType()) != null) {
                        set.add(Character.toLowerCase(name2.charAt(0)) + name2.substring(1));
                    }
                } catch (Exception e) {
                }
            }
        }
        return set;
    }

    public static Object getProperty(String propName, Object target) throws NoSuchMethodException, SecurityException {
        try {
            String capitalized = BeanUtil.PREFIX_GETTER_GET + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
            Method method = target.getClass().getMethod(capitalized, new Class[0]);
            return method.invoke(target, new Object[0]);
        } catch (Exception e) {
            try {
                String capitalized2 = BeanUtil.PREFIX_GETTER_IS + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
                Method method2 = target.getClass().getMethod(capitalized2, new Class[0]);
                return method2.invoke(target, new Object[0]);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static Properties copyProperties(Properties props) {
        Properties copy = new Properties();
        props.forEach((key, value) -> {
            copy.setProperty(key.toString(), value.toString());
        });
        return copy;
    }

    private static void setProperty(Object target, String propName, Object propValue, List<Method> methods) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Logger logger = LoggerFactory.getLogger((Class<?>) PropertyElf.class);
        String methodName = "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
        Method writeMethod = methods.stream().filter(m -> {
            return m.getName().equals(methodName) && m.getParameterCount() == 1;
        }).findFirst().orElse(null);
        if (writeMethod == null) {
            String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
            writeMethod = methods.stream().filter(m2 -> {
                return m2.getName().equals(methodName2) && m2.getParameterCount() == 1;
            }).findFirst().orElse(null);
        }
        if (writeMethod == null) {
            logger.error("Property {} does not exist on target {}", propName, target.getClass());
            throw new RuntimeException(String.format("Property %s does not exist on target %s", propName, target.getClass()));
        }
        try {
            Class<?> paramClass = writeMethod.getParameterTypes()[0];
            if (paramClass == Integer.TYPE) {
                writeMethod.invoke(target, Integer.valueOf(Integer.parseInt(propValue.toString())));
            } else if (paramClass == Long.TYPE) {
                writeMethod.invoke(target, Long.valueOf(Long.parseLong(propValue.toString())));
            } else if (paramClass == Boolean.TYPE || paramClass == Boolean.class) {
                writeMethod.invoke(target, Boolean.valueOf(Boolean.parseBoolean(propValue.toString())));
            } else if (paramClass == String.class) {
                writeMethod.invoke(target, propValue.toString());
            } else {
                try {
                    logger.debug("Try to create a new instance of \"{}\"", propValue.toString());
                    writeMethod.invoke(target, Class.forName(propValue.toString()).newInstance());
                } catch (ClassNotFoundException | InstantiationException e) {
                    logger.debug("Class \"{}\" not found or could not instantiate it (Default constructor)", propValue.toString());
                    writeMethod.invoke(target, propValue);
                }
            }
        } catch (Exception e2) {
            logger.error("Failed to set property {} on target {}", propName, target.getClass(), e2);
            throw new RuntimeException(e2);
        }
    }
}
