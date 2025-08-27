package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/TypeConverter.class */
public interface TypeConverter {
    <T> T convertIfNecessary(Object obj, Class<T> cls) throws TypeMismatchException;

    <T> T convertIfNecessary(Object obj, Class<T> cls, MethodParameter methodParameter) throws TypeMismatchException;

    <T> T convertIfNecessary(Object obj, Class<T> cls, Field field) throws TypeMismatchException;
}
