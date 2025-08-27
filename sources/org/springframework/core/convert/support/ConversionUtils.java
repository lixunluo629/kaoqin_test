package org.springframework.core.convert.support;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.util.ClassUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/convert/support/ConversionUtils.class */
abstract class ConversionUtils {
    ConversionUtils() {
    }

    public static Object invokeConverter(GenericConverter converter, Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        try {
            return converter.convert(source, sourceType, targetType);
        } catch (ConversionFailedException ex) {
            throw ex;
        } catch (Throwable ex2) {
            throw new ConversionFailedException(sourceType, targetType, source, ex2);
        }
    }

    public static boolean canConvertElements(TypeDescriptor sourceElementType, TypeDescriptor targetElementType, ConversionService conversionService) {
        if (targetElementType == null || sourceElementType == null || conversionService.canConvert(sourceElementType, targetElementType) || ClassUtils.isAssignable(sourceElementType.getType(), targetElementType.getType())) {
            return true;
        }
        return false;
    }

    public static Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType;
        Class<?> superclass = targetType;
        while (true) {
            enumType = superclass;
            if (enumType == null || enumType.isEnum()) {
                break;
            }
            superclass = enumType.getSuperclass();
        }
        if (enumType == null) {
            throw new IllegalArgumentException("The target type " + targetType.getName() + " does not refer to an enum");
        }
        return enumType;
    }
}
