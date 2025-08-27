package org.apache.commons.lang;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.exception.CloneFailedException;
import org.apache.commons.lang.reflect.MethodUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/ObjectUtils.class */
public class ObjectUtils {
    public static final Null NULL = new Null();

    public static Object defaultIfNull(Object object, Object defaultValue) {
        return object != null ? object : defaultValue;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 == null || object2 == null) {
            return false;
        }
        return object1.equals(object2);
    }

    public static boolean notEqual(Object object1, Object object2) {
        return !equals(object1, object2);
    }

    public static int hashCode(Object obj) {
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public static String identityToString(Object object) {
        if (object == null) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        identityToString(buffer, object);
        return buffer.toString();
    }

    public static void identityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            throw new NullPointerException("Cannot get the toString of a null identity");
        }
        buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
    }

    public static StringBuffer appendIdentityToString(StringBuffer buffer, Object object) {
        if (object == null) {
            return null;
        }
        if (buffer == null) {
            buffer = new StringBuffer();
        }
        return buffer.append(object.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(object)));
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String toString(Object obj, String nullStr) {
        return obj == null ? nullStr : obj.toString();
    }

    public static Object min(Comparable c1, Comparable c2) {
        return compare(c1, c2, true) <= 0 ? c1 : c2;
    }

    public static Object max(Comparable c1, Comparable c2) {
        return compare(c1, c2, false) >= 0 ? c1 : c2;
    }

    public static int compare(Comparable c1, Comparable c2) {
        return compare(c1, c2, false);
    }

    public static int compare(Comparable c1, Comparable c2, boolean nullGreater) {
        if (c1 == c2) {
            return 0;
        }
        if (c1 == null) {
            return nullGreater ? 1 : -1;
        }
        if (c2 == null) {
            return nullGreater ? -1 : 1;
        }
        return c1.compareTo(c2);
    }

    public static Object clone(Object o) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, CloneNotSupportedException, NegativeArraySizeException {
        Object result;
        if (o instanceof Cloneable) {
            if (o.getClass().isArray()) {
                Class componentType = o.getClass().getComponentType();
                if (!componentType.isPrimitive()) {
                    result = ((Object[]) o).clone();
                } else {
                    int length = Array.getLength(o);
                    result = Array.newInstance((Class<?>) componentType, length);
                    while (true) {
                        int i = length;
                        length--;
                        if (i <= 0) {
                            break;
                        }
                        Array.set(result, length, Array.get(o, length));
                    }
                }
            } else {
                try {
                    result = MethodUtils.invokeMethod(o, "clone", (Object[]) null);
                } catch (IllegalAccessException e) {
                    throw new CloneFailedException(new StringBuffer().append("Cannot clone Cloneable type ").append(o.getClass().getName()).toString(), e);
                } catch (NoSuchMethodException e2) {
                    throw new CloneFailedException(new StringBuffer().append("Cloneable type ").append(o.getClass().getName()).append(" has no clone method").toString(), e2);
                } catch (InvocationTargetException e3) {
                    throw new CloneFailedException(new StringBuffer().append("Exception cloning Cloneable type ").append(o.getClass().getName()).toString(), e3.getTargetException());
                }
            }
            return result;
        }
        return null;
    }

    public static Object cloneIfPossible(Object o) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, CloneNotSupportedException, NegativeArraySizeException {
        Object clone = clone(o);
        return clone == null ? o : clone;
    }

    /* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/ObjectUtils$Null.class */
    public static class Null implements Serializable {
        private static final long serialVersionUID = 7092611880189329093L;

        Null() {
        }

        private Object readResolve() {
            return ObjectUtils.NULL;
        }
    }
}
