package org.apache.commons.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import org.apache.commons.lang.ClassUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/reflect/FieldUtils.class */
public class FieldUtils {
    public static Field getField(Class cls, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = getField(cls, fieldName, false);
        MemberUtils.setAccessibleWorkaround(field);
        return field;
    }

    public static Field getField(Class cls, String fieldName, boolean forceAccess) throws NoSuchFieldException {
        Field test;
        Field field;
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("The field name must not be null");
        }
        Class superclass = cls;
        while (true) {
            Class acls = superclass;
            if (acls != null) {
                try {
                    field = acls.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                }
                if (Modifier.isPublic(field.getModifiers())) {
                    break;
                }
                if (!forceAccess) {
                    superclass = acls.getSuperclass();
                } else {
                    field.setAccessible(true);
                    break;
                }
            } else {
                Field match = null;
                Iterator intf = ClassUtils.getAllInterfaces(cls).iterator();
                while (intf.hasNext()) {
                    try {
                        test = ((Class) intf.next()).getField(fieldName);
                    } catch (NoSuchFieldException e2) {
                    }
                    if (match != null) {
                        throw new IllegalArgumentException(new StringBuffer().append("Reference to field ").append(fieldName).append(" is ambiguous relative to ").append(cls).append("; a matching field exists on two or more implemented interfaces.").toString());
                    }
                    match = test;
                }
                return match;
            }
        }
        return field;
    }

    public static Field getDeclaredField(Class cls, String fieldName) {
        return getDeclaredField(cls, fieldName, false);
    }

    public static Field getDeclaredField(Class cls, String fieldName, boolean forceAccess) throws NoSuchFieldException {
        if (cls == null) {
            throw new IllegalArgumentException("The class must not be null");
        }
        if (fieldName == null) {
            throw new IllegalArgumentException("The field name must not be null");
        }
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (!MemberUtils.isAccessible(field)) {
                if (forceAccess) {
                    field.setAccessible(true);
                } else {
                    return null;
                }
            }
            return field;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static Object readStaticField(Field field) throws IllegalAccessException {
        return readStaticField(field, false);
    }

    public static Object readStaticField(Field field, boolean forceAccess) throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException(new StringBuffer().append("The field '").append(field.getName()).append("' is not static").toString());
        }
        return readField(field, (Object) null, forceAccess);
    }

    public static Object readStaticField(Class cls, String fieldName) throws IllegalAccessException {
        return readStaticField(cls, fieldName, false);
    }

    public static Object readStaticField(Class cls, String fieldName, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException {
        Field field = getField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate field ").append(fieldName).append(" on ").append(cls).toString());
        }
        return readStaticField(field, false);
    }

    public static Object readDeclaredStaticField(Class cls, String fieldName) throws IllegalAccessException {
        return readDeclaredStaticField(cls, fieldName, false);
    }

    public static Object readDeclaredStaticField(Class cls, String fieldName, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException {
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate declared field ").append(cls.getName()).append(".").append(fieldName).toString());
        }
        return readStaticField(field, false);
    }

    public static Object readField(Field field, Object target) throws IllegalAccessException {
        return readField(field, target, false);
    }

    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException, SecurityException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        return field.get(target);
    }

    public static Object readField(Object target, String fieldName) throws IllegalAccessException {
        return readField(target, fieldName, false);
    }

    public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate field ").append(fieldName).append(" on ").append(cls).toString());
        }
        return readField(field, target);
    }

    public static Object readDeclaredField(Object target, String fieldName) throws IllegalAccessException {
        return readDeclaredField(target, fieldName, false);
    }

    public static Object readDeclaredField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate declared field ").append(cls.getName()).append(".").append(fieldName).toString());
        }
        return readField(field, target);
    }

    public static void writeStaticField(Field field, Object value) throws IllegalAccessException, SecurityException, IllegalArgumentException {
        writeStaticField(field, value, false);
    }

    public static void writeStaticField(Field field, Object value, boolean forceAccess) throws IllegalAccessException, SecurityException, IllegalArgumentException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (!Modifier.isStatic(field.getModifiers())) {
            throw new IllegalArgumentException(new StringBuffer().append("The field '").append(field.getName()).append("' is not static").toString());
        }
        writeField(field, (Object) null, value, forceAccess);
    }

    public static void writeStaticField(Class cls, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        writeStaticField(cls, fieldName, value, false);
    }

    public static void writeStaticField(Class cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field field = getField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate field ").append(fieldName).append(" on ").append(cls).toString());
        }
        writeStaticField(field, value);
    }

    public static void writeDeclaredStaticField(Class cls, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        writeDeclaredStaticField(cls, fieldName, value, false);
    }

    public static void writeDeclaredStaticField(Class cls, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate declared field ").append(cls.getName()).append(".").append(fieldName).toString());
        }
        writeField(field, (Object) null, value);
    }

    public static void writeField(Field field, Object target, Object value) throws IllegalAccessException, SecurityException, IllegalArgumentException {
        writeField(field, target, value, false);
    }

    public static void writeField(Field field, Object target, Object value, boolean forceAccess) throws IllegalAccessException, SecurityException, IllegalArgumentException {
        if (field == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        field.set(target, value);
    }

    public static void writeField(Object target, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        writeField(target, fieldName, value, false);
    }

    public static void writeField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate declared field ").append(cls.getName()).append(".").append(fieldName).toString());
        }
        writeField(field, target, value);
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        writeDeclaredField(target, fieldName, value, false);
    }

    public static void writeDeclaredField(Object target, String fieldName, Object value, boolean forceAccess) throws IllegalAccessException, NoSuchFieldException, SecurityException, IllegalArgumentException {
        if (target == null) {
            throw new IllegalArgumentException("target object must not be null");
        }
        Class cls = target.getClass();
        Field field = getDeclaredField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot locate declared field ").append(cls.getName()).append(".").append(fieldName).toString());
        }
        writeField(field, target, value);
    }
}
