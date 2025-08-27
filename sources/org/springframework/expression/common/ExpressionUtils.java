package org.springframework.expression.common;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypedValue;
import org.springframework.util.ClassUtils;

/* loaded from: spring-expression-4.3.25.RELEASE.jar:org/springframework/expression/common/ExpressionUtils.class */
public abstract class ExpressionUtils {
    public static <T> T convertTypedValue(EvaluationContext evaluationContext, TypedValue typedValue, Class<T> cls) {
        T t = (T) typedValue.getValue();
        if (cls == null) {
            return t;
        }
        if (evaluationContext != null) {
            return (T) evaluationContext.getTypeConverter().convertValue(t, typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(cls));
        }
        if (ClassUtils.isAssignableValue(cls, t)) {
            return t;
        }
        throw new EvaluationException("Cannot convert value '" + t + "' to type '" + cls.getName() + "'");
    }

    public static int toInt(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Integer) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Integer.class))).intValue();
    }

    public static boolean toBoolean(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Boolean) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Boolean.class))).booleanValue();
    }

    public static double toDouble(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Double) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Double.class))).doubleValue();
    }

    public static long toLong(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Long) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Long.class))).longValue();
    }

    public static char toChar(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Character) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Character.class))).charValue();
    }

    public static short toShort(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Short) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Short.class))).shortValue();
    }

    public static float toFloat(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Float) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Float.class))).floatValue();
    }

    public static byte toByte(TypeConverter typeConverter, TypedValue typedValue) {
        return ((Byte) typeConverter.convertValue(typedValue.getValue(), typedValue.getTypeDescriptor(), TypeDescriptor.valueOf(Byte.class))).byteValue();
    }
}
