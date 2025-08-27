package org.apache.commons.lang.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.SystemUtils;

/* loaded from: commons-lang-2.6.jar:org/apache/commons/lang/reflect/MemberUtils.class */
abstract class MemberUtils {
    private static final int ACCESS_TEST = 7;
    private static final Method IS_SYNTHETIC;
    private static final Class[] ORDERED_PRIMITIVE_TYPES;
    static Class class$java$lang$reflect$Member;

    MemberUtils() {
    }

    static {
        Class clsClass$;
        Method isSynthetic = null;
        if (SystemUtils.isJavaVersionAtLeast(1.5f)) {
            try {
                if (class$java$lang$reflect$Member == null) {
                    clsClass$ = class$("java.lang.reflect.Member");
                    class$java$lang$reflect$Member = clsClass$;
                } else {
                    clsClass$ = class$java$lang$reflect$Member;
                }
                isSynthetic = clsClass$.getMethod("isSynthetic", ArrayUtils.EMPTY_CLASS_ARRAY);
            } catch (Exception e) {
            }
        }
        IS_SYNTHETIC = isSynthetic;
        ORDERED_PRIMITIVE_TYPES = new Class[]{Byte.TYPE, Short.TYPE, Character.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void setAccessibleWorkaround(AccessibleObject accessibleObject) throws SecurityException {
        if (accessibleObject == 0 || accessibleObject.isAccessible()) {
            return;
        }
        Member m = (Member) accessibleObject;
        if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
            try {
                accessibleObject.setAccessible(true);
            } catch (SecurityException e) {
            }
        }
    }

    static boolean isPackageAccess(int modifiers) {
        return (modifiers & 7) == 0;
    }

    static boolean isAccessible(Member m) {
        return (m == null || !Modifier.isPublic(m.getModifiers()) || isSynthetic(m)) ? false : true;
    }

    static boolean isSynthetic(Member m) {
        if (IS_SYNTHETIC != null) {
            try {
                return ((Boolean) IS_SYNTHETIC.invoke(m, null)).booleanValue();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    static int compareParameterTypes(Class[] left, Class[] right, Class[] actual) {
        float leftCost = getTotalTransformationCost(actual, left);
        float rightCost = getTotalTransformationCost(actual, right);
        if (leftCost < rightCost) {
            return -1;
        }
        return rightCost < leftCost ? 1 : 0;
    }

    private static float getTotalTransformationCost(Class[] srcArgs, Class[] destArgs) {
        float totalCost = 0.0f;
        for (int i = 0; i < srcArgs.length; i++) {
            Class srcClass = srcArgs[i];
            Class destClass = destArgs[i];
            totalCost += getObjectTransformationCost(srcClass, destClass);
        }
        return totalCost;
    }

    private static float getObjectTransformationCost(Class srcClass, Class destClass) {
        if (destClass.isPrimitive()) {
            return getPrimitivePromotionCost(srcClass, destClass);
        }
        float cost = 0.0f;
        while (true) {
            if (srcClass != null && !destClass.equals(srcClass)) {
                if (destClass.isInterface() && ClassUtils.isAssignable(srcClass, destClass)) {
                    cost += 0.25f;
                    break;
                }
                cost += 1.0f;
                srcClass = srcClass.getSuperclass();
            } else {
                break;
            }
        }
        if (srcClass == null) {
            cost += 1.5f;
        }
        return cost;
    }

    private static float getPrimitivePromotionCost(Class srcClass, Class destClass) {
        float cost = 0.0f;
        Class cls = srcClass;
        if (!cls.isPrimitive()) {
            cost = 0.0f + 0.1f;
            cls = ClassUtils.wrapperToPrimitive(cls);
        }
        for (int i = 0; cls != destClass && i < ORDERED_PRIMITIVE_TYPES.length; i++) {
            if (cls == ORDERED_PRIMITIVE_TYPES[i]) {
                cost += 0.1f;
                if (i < ORDERED_PRIMITIVE_TYPES.length - 1) {
                    cls = ORDERED_PRIMITIVE_TYPES[i + 1];
                }
            }
        }
        return cost;
    }
}
