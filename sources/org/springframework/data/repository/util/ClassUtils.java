package org.springframework.data.repository.util;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.data.repository.Repository;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/util/ClassUtils.class */
public abstract class ClassUtils {
    private ClassUtils() {
    }

    public static boolean hasProperty(Class<?> type, String property) {
        return (null == ReflectionUtils.findMethod(type, new StringBuilder().append(BeanUtil.PREFIX_GETTER_GET).append(property).toString()) && null == ReflectionUtils.findField(type, StringUtils.uncapitalize(property))) ? false : true;
    }

    public static boolean isGenericRepositoryInterface(Class<?> interfaze) {
        return Repository.class.equals(interfaze);
    }

    public static boolean isGenericRepositoryInterface(String interfaceName) {
        return Repository.class.getName().equals(interfaceName);
    }

    public static int getNumberOfOccurences(Method method, Class<?> type) {
        int result = 0;
        for (Class<?> clazz : method.getParameterTypes()) {
            if (type.equals(clazz)) {
                result++;
            }
        }
        return result;
    }

    public static void assertReturnTypeAssignable(Method method, Class<?>... types) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notEmpty(types, "Types must not be null or empty!");
        TypeInformation<?> returnType = ClassTypeInformation.fromReturnTypeOf(method);
        TypeInformation<?> returnType2 = QueryExecutionConverters.supports(returnType.getType()) ? returnType.getComponentType() : returnType;
        for (Class<?> type : types) {
            if (type.isAssignableFrom(returnType2.getType())) {
                return;
            }
        }
        throw new IllegalStateException("Method has to have one of the following return types! " + Arrays.toString(types));
    }

    public static boolean isOfType(Object object, Collection<Class<?>> types) {
        if (null == object) {
            return false;
        }
        for (Class<?> type : types) {
            if (type.isAssignableFrom(object.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasParameterOfType(Method method, Class<?> type) {
        return Arrays.asList(method.getParameterTypes()).contains(type);
    }

    public static void unwrapReflectionException(Exception ex) throws Exception {
        if (ex instanceof InvocationTargetException) {
            throw ((InvocationTargetException) ex).getTargetException();
        }
        throw ex;
    }
}
