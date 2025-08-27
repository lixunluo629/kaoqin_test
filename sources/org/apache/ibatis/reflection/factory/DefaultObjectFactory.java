package org.apache.ibatis.reflection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.ibatis.reflection.ReflectionException;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/factory/DefaultObjectFactory.class */
public class DefaultObjectFactory implements ObjectFactory, Serializable {
    private static final long serialVersionUID = -8855120656740914948L;

    @Override // org.apache.ibatis.reflection.factory.ObjectFactory
    public <T> T create(Class<T> cls) {
        return (T) create(cls, null, null);
    }

    @Override // org.apache.ibatis.reflection.factory.ObjectFactory
    public <T> T create(Class<T> cls, List<Class<?>> list, List<Object> list2) {
        return (T) instantiateClass(resolveInterface(cls), list, list2);
    }

    @Override // org.apache.ibatis.reflection.factory.ObjectFactory
    public void setProperties(Properties properties) {
    }

    private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) throws NoSuchMethodException, SecurityException {
        try {
            if (constructorArgTypes == null || constructorArgs == null) {
                Constructor<T> constructor = type.getDeclaredConstructor(new Class[0]);
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance(new Object[0]);
            }
            Constructor<T> constructor2 = type.getDeclaredConstructor((Class[]) constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
            if (!constructor2.isAccessible()) {
                constructor2.setAccessible(true);
            }
            return constructor2.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));
        } catch (Exception e) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null && !constructorArgTypes.isEmpty()) {
                for (Class<?> argType : constructorArgTypes) {
                    argTypes.append(argType.getSimpleName());
                    argTypes.append(",");
                }
                argTypes.deleteCharAt(argTypes.length() - 1);
            }
            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null && !constructorArgs.isEmpty()) {
                for (Object argValue : constructorArgs) {
                    argValues.append(String.valueOf(argValue));
                    argValues.append(",");
                }
                argValues.deleteCharAt(argValues.length() - 1);
            }
            throw new ReflectionException("Error instantiating " + type + " with invalid types (" + ((Object) argTypes) + ") or values (" + ((Object) argValues) + "). Cause: " + e, e);
        }
    }

    protected Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            classToCreate = ArrayList.class;
        } else if (type == Map.class) {
            classToCreate = HashMap.class;
        } else if (type == SortedSet.class) {
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            classToCreate = HashSet.class;
        } else {
            classToCreate = type;
        }
        return classToCreate;
    }

    @Override // org.apache.ibatis.reflection.factory.ObjectFactory
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }
}
