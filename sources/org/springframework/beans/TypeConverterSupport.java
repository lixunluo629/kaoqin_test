package org.springframework.beans;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConverterNotFoundException;

/* loaded from: spring-beans-4.3.25.RELEASE.jar:org/springframework/beans/TypeConverterSupport.class */
public abstract class TypeConverterSupport extends PropertyEditorRegistrySupport implements TypeConverter {
    TypeConverterDelegate typeConverterDelegate;

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls) throws TypeMismatchException {
        return (T) doConvert(obj, cls, null, null);
    }

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls, MethodParameter methodParameter) throws TypeMismatchException {
        return (T) doConvert(obj, cls, methodParameter, null);
    }

    @Override // org.springframework.beans.TypeConverter
    public <T> T convertIfNecessary(Object obj, Class<T> cls, Field field) throws TypeMismatchException {
        return (T) doConvert(obj, cls, null, field);
    }

    private <T> T doConvert(Object obj, Class<T> cls, MethodParameter methodParameter, Field field) throws TypeMismatchException {
        try {
            if (field != null) {
                return (T) this.typeConverterDelegate.convertIfNecessary(obj, cls, field);
            }
            return (T) this.typeConverterDelegate.convertIfNecessary(obj, cls, methodParameter);
        } catch (IllegalArgumentException e) {
            throw new TypeMismatchException(obj, (Class<?>) cls, (Throwable) e);
        } catch (IllegalStateException e2) {
            throw new ConversionNotSupportedException(obj, (Class<?>) cls, (Throwable) e2);
        } catch (ConverterNotFoundException e3) {
            throw new ConversionNotSupportedException(obj, (Class<?>) cls, (Throwable) e3);
        } catch (ConversionException e4) {
            throw new TypeMismatchException(obj, (Class<?>) cls, (Throwable) e4);
        }
    }
}
