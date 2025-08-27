package org.apache.ibatis.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.xmlbeans.impl.jam.xml.JamXmlElements;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/MetaClass.class */
public class MetaClass {
    private final ReflectorFactory reflectorFactory;
    private final Reflector reflector;

    private MetaClass(Class<?> type, ReflectorFactory reflectorFactory) {
        this.reflectorFactory = reflectorFactory;
        this.reflector = reflectorFactory.findForClass(type);
    }

    public static MetaClass forClass(Class<?> type, ReflectorFactory reflectorFactory) {
        return new MetaClass(type, reflectorFactory);
    }

    public MetaClass metaClassForProperty(String name) {
        Class<?> propType = this.reflector.getGetterType(name);
        return forClass(propType, this.reflectorFactory);
    }

    public String findProperty(String name) {
        StringBuilder prop = buildProperty(name, new StringBuilder());
        if (prop.length() > 0) {
            return prop.toString();
        }
        return null;
    }

    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping) {
            name = name.replace("_", "");
        }
        return findProperty(name);
    }

    public String[] getGetterNames() {
        return this.reflector.getGetablePropertyNames();
    }

    public String[] getSetterNames() {
        return this.reflector.getSetablePropertyNames();
    }

    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = metaClassForProperty(prop.getName());
            return metaProp.getSetterType(prop.getChildren());
        }
        return this.reflector.getSetterType(prop.getName());
    }

    public Class<?> getGetterType(String name) throws NoSuchFieldException {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            MetaClass metaProp = metaClassForProperty(prop);
            return metaProp.getGetterType(prop.getChildren());
        }
        return getGetterType(prop);
    }

    private MetaClass metaClassForProperty(PropertyTokenizer prop) throws NoSuchFieldException {
        Class<?> propType = getGetterType(prop);
        return forClass(propType, this.reflectorFactory);
    }

    private Class<?> getGetterType(PropertyTokenizer prop) throws NoSuchFieldException {
        Type[] actualTypeArguments;
        Class<?> type = this.reflector.getGetterType(prop.getName());
        if (prop.getIndex() != null && Collection.class.isAssignableFrom(type)) {
            Type returnType = getGenericGetterType(prop.getName());
            if ((returnType instanceof ParameterizedType) && (actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments()) != null && actualTypeArguments.length == 1) {
                Type returnType2 = actualTypeArguments[0];
                if (returnType2 instanceof Class) {
                    type = (Class) returnType2;
                } else if (returnType2 instanceof ParameterizedType) {
                    type = (Class) ((ParameterizedType) returnType2).getRawType();
                }
            }
        }
        return type;
    }

    private Type getGenericGetterType(String propertyName) throws NoSuchFieldException {
        try {
            Invoker invoker = this.reflector.getGetInvoker(propertyName);
            if (invoker instanceof MethodInvoker) {
                Field _method = MethodInvoker.class.getDeclaredField(JamXmlElements.METHOD);
                _method.setAccessible(true);
                Method method = (Method) _method.get(invoker);
                return TypeParameterResolver.resolveReturnType(method, this.reflector.getType());
            }
            if (invoker instanceof GetFieldInvoker) {
                Field _field = GetFieldInvoker.class.getDeclaredField(JamXmlElements.FIELD);
                _field.setAccessible(true);
                Field field = (Field) _field.get(invoker);
                return TypeParameterResolver.resolveFieldType(field, this.reflector.getType());
            }
            return null;
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }

    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasSetter(prop.getName())) {
                MetaClass metaProp = metaClassForProperty(prop.getName());
                return metaProp.hasSetter(prop.getChildren());
            }
            return false;
        }
        return this.reflector.hasSetter(prop.getName());
    }

    public boolean hasGetter(String name) throws NoSuchFieldException {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (this.reflector.hasGetter(prop.getName())) {
                MetaClass metaProp = metaClassForProperty(prop);
                return metaProp.hasGetter(prop.getChildren());
            }
            return false;
        }
        return this.reflector.hasGetter(prop.getName());
    }

    public Invoker getGetInvoker(String name) {
        return this.reflector.getGetInvoker(name);
    }

    public Invoker getSetInvoker(String name) {
        return this.reflector.getSetInvoker(name);
    }

    private StringBuilder buildProperty(String name, StringBuilder builder) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            String propertyName = this.reflector.findPropertyName(prop.getName());
            if (propertyName != null) {
                builder.append(propertyName);
                builder.append(".");
                MetaClass metaProp = metaClassForProperty(propertyName);
                metaProp.buildProperty(prop.getChildren(), builder);
            }
        } else {
            String propertyName2 = this.reflector.findPropertyName(name);
            if (propertyName2 != null) {
                builder.append(propertyName2);
            }
        }
        return builder;
    }

    public boolean hasDefaultConstructor() {
        return this.reflector.hasDefaultConstructor();
    }
}
