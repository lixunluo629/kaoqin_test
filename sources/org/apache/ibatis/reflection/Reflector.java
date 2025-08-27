package org.apache.ibatis.reflection;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.ReflectPermission;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;
import org.apache.ibatis.reflection.invoker.SetFieldInvoker;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/reflection/Reflector.class */
public class Reflector {
    private final Class<?> type;
    private final String[] readablePropertyNames;
    private final String[] writeablePropertyNames;
    private Constructor<?> defaultConstructor;
    private final Map<String, Invoker> setMethods = new HashMap();
    private final Map<String, Invoker> getMethods = new HashMap();
    private final Map<String, Class<?>> setTypes = new HashMap();
    private final Map<String, Class<?>> getTypes = new HashMap();
    private Map<String, String> caseInsensitivePropertyMap = new HashMap();

    public Reflector(Class<?> clazz) throws SecurityException {
        this.type = clazz;
        addDefaultConstructor(clazz);
        addGetMethods(clazz);
        addSetMethods(clazz);
        addFields(clazz);
        this.readablePropertyNames = (String[]) this.getMethods.keySet().toArray(new String[this.getMethods.keySet().size()]);
        this.writeablePropertyNames = (String[]) this.setMethods.keySet().toArray(new String[this.setMethods.keySet().size()]);
        for (String propName : this.readablePropertyNames) {
            this.caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH), propName);
        }
        for (String propName2 : this.writeablePropertyNames) {
            this.caseInsensitivePropertyMap.put(propName2.toUpperCase(Locale.ENGLISH), propName2);
        }
    }

    private void addDefaultConstructor(Class<?> clazz) throws SecurityException {
        Constructor<?>[] consts = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : consts) {
            if (constructor.getParameterTypes().length == 0) {
                if (canAccessPrivateMethods()) {
                    try {
                        constructor.setAccessible(true);
                    } catch (Exception e) {
                    }
                }
                if (constructor.isAccessible()) {
                    this.defaultConstructor = constructor;
                }
            }
        }
    }

    private void addGetMethods(Class<?> cls) {
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Method[] methods = getClassMethods(cls);
        for (Method method : methods) {
            if (method.getParameterTypes().length <= 0) {
                String name = method.getName();
                if ((name.startsWith(BeanUtil.PREFIX_GETTER_GET) && name.length() > 3) || (name.startsWith(BeanUtil.PREFIX_GETTER_IS) && name.length() > 2)) {
                    addMethodConflict(conflictingGetters, PropertyNamer.methodToProperty(name), method);
                }
            }
        }
        resolveGetterConflicts(conflictingGetters);
    }

    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        for (Map.Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
            Method winner = null;
            String propName = entry.getKey();
            for (Method candidate : entry.getValue()) {
                if (winner == null) {
                    winner = candidate;
                } else {
                    Class<?> winnerType = winner.getReturnType();
                    Class<?> candidateType = candidate.getReturnType();
                    if (candidateType.equals(winnerType)) {
                        if (!Boolean.TYPE.equals(candidateType)) {
                            throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                        }
                        if (candidate.getName().startsWith(BeanUtil.PREFIX_GETTER_IS)) {
                            winner = candidate;
                        }
                    } else if (candidateType.isAssignableFrom(winnerType)) {
                        continue;
                    } else if (winnerType.isAssignableFrom(candidateType)) {
                        winner = candidate;
                    } else {
                        throw new ReflectionException("Illegal overloaded getter method with ambiguous type for property " + propName + " in class " + winner.getDeclaringClass() + ". This breaks the JavaBeans specification and can cause unpredictable results.");
                    }
                }
            }
            addGetMethod(propName, winner);
        }
    }

    private void addGetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            this.getMethods.put(name, new MethodInvoker(method));
            Type returnType = TypeParameterResolver.resolveReturnType(method, this.type);
            this.getTypes.put(name, typeToClass(returnType));
        }
    }

    private void addSetMethods(Class<?> cls) {
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        Method[] methods = getClassMethods(cls);
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("set") && name.length() > 3 && method.getParameterTypes().length == 1) {
                addMethodConflict(conflictingSetters, PropertyNamer.methodToProperty(name), method);
            }
        }
        resolveSetterConflicts(conflictingSetters);
    }

    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        List<Method> list = conflictingMethods.get(name);
        if (list == null) {
            list = new ArrayList();
            conflictingMethods.put(name, list);
        }
        list.add(method);
    }

    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        for (String propName : conflictingSetters.keySet()) {
            List<Method> setters = conflictingSetters.get(propName);
            Class<?> getterType = this.getTypes.get(propName);
            Method match = null;
            ReflectionException exception = null;
            Iterator<Method> it = setters.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Method setter = it.next();
                Class<?> paramType = setter.getParameterTypes()[0];
                if (paramType.equals(getterType)) {
                    match = setter;
                    break;
                } else if (exception == null) {
                    try {
                        match = pickBetterSetter(match, setter, propName);
                    } catch (ReflectionException e) {
                        match = null;
                        exception = e;
                    }
                }
            }
            if (match == null) {
                throw exception;
            }
            addSetMethod(propName, match);
        }
    }

    private Method pickBetterSetter(Method setter1, Method setter2, String property) {
        if (setter1 == null) {
            return setter2;
        }
        Class<?> paramType1 = setter1.getParameterTypes()[0];
        Class<?> paramType2 = setter2.getParameterTypes()[0];
        if (paramType1.isAssignableFrom(paramType2)) {
            return setter2;
        }
        if (paramType2.isAssignableFrom(paramType1)) {
            return setter1;
        }
        throw new ReflectionException("Ambiguous setters defined for property '" + property + "' in class '" + setter2.getDeclaringClass() + "' with types '" + paramType1.getName() + "' and '" + paramType2.getName() + "'.");
    }

    private void addSetMethod(String name, Method method) {
        if (isValidPropertyName(name)) {
            this.setMethods.put(name, new MethodInvoker(method));
            Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, this.type);
            this.setTypes.put(name, typeToClass(paramTypes[0]));
        }
    }

    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class) src;
        } else if (src instanceof ParameterizedType) {
            result = (Class) ((ParameterizedType) src).getRawType();
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class<?>) componentType, 0).getClass();
            } else {
                Class<?> componentClass = typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }
        if (result == null) {
            result = Object.class;
        }
        return result;
    }

    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (canAccessPrivateMethods()) {
                try {
                    field.setAccessible(true);
                } catch (Exception e) {
                }
            }
            if (field.isAccessible()) {
                if (!this.setMethods.containsKey(field.getName())) {
                    int modifiers = field.getModifiers();
                    if (!Modifier.isFinal(modifiers) || !Modifier.isStatic(modifiers)) {
                        addSetField(field);
                    }
                }
                if (!this.getMethods.containsKey(field.getName())) {
                    addGetField(field);
                }
            }
        }
        if (clazz.getSuperclass() != null) {
            addFields(clazz.getSuperclass());
        }
    }

    private void addSetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            this.setMethods.put(field.getName(), new SetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.setTypes.put(field.getName(), typeToClass(fieldType));
        }
    }

    private void addGetField(Field field) {
        if (isValidPropertyName(field.getName())) {
            this.getMethods.put(field.getName(), new GetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field, this.type);
            this.getTypes.put(field.getName(), typeToClass(fieldType));
        }
    }

    private boolean isValidPropertyName(String name) {
        return (name.startsWith(PropertiesBeanDefinitionReader.CONSTRUCTOR_ARG_PREFIX) || "serialVersionUID".equals(name) || "class".equals(name)) ? false : true;
    }

    private Method[] getClassMethods(Class<?> cls) {
        Map<String, Method> uniqueMethods = new HashMap<>();
        Class<?> superclass = cls;
        while (true) {
            Class<?> currentClass = superclass;
            if (currentClass == null || currentClass == Object.class) {
                break;
            }
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getMethods());
            }
            superclass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();
        return (Method[]) methods.toArray(new Method[methods.size()]);
    }

    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method currentMethod : methods) {
            if (!currentMethod.isBridge()) {
                String signature = getSignature(currentMethod);
                if (!uniqueMethods.containsKey(signature)) {
                    if (canAccessPrivateMethods()) {
                        try {
                            currentMethod.setAccessible(true);
                        } catch (Exception e) {
                        }
                    }
                    uniqueMethods.put(signature, currentMethod);
                }
            }
        }
    }

    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        if (returnType != null) {
            sb.append(returnType.getName()).append('#');
        }
        sb.append(method.getName());
        Class<?>[] parameters = method.getParameterTypes();
        for (int i = 0; i < parameters.length; i++) {
            if (i == 0) {
                sb.append(':');
            } else {
                sb.append(',');
            }
            sb.append(parameters[i].getName());
        }
        return sb.toString();
    }

    private static boolean canAccessPrivateMethods() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    public Class<?> getType() {
        return this.type;
    }

    public Constructor<?> getDefaultConstructor() {
        if (this.defaultConstructor != null) {
            return this.defaultConstructor;
        }
        throw new ReflectionException("There is no default constructor for " + this.type);
    }

    public boolean hasDefaultConstructor() {
        return this.defaultConstructor != null;
    }

    public Invoker getSetInvoker(String propertyName) {
        Invoker method = this.setMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return method;
    }

    public Invoker getGetInvoker(String propertyName) {
        Invoker method = this.getMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return method;
    }

    public Class<?> getSetterType(String propertyName) {
        Class<?> clazz = this.setTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return clazz;
    }

    public Class<?> getGetterType(String propertyName) {
        Class<?> clazz = this.getTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + this.type + "'");
        }
        return clazz;
    }

    public String[] getGetablePropertyNames() {
        return this.readablePropertyNames;
    }

    public String[] getSetablePropertyNames() {
        return this.writeablePropertyNames;
    }

    public boolean hasSetter(String propertyName) {
        return this.setMethods.keySet().contains(propertyName);
    }

    public boolean hasGetter(String propertyName) {
        return this.getMethods.keySet().contains(propertyName);
    }

    public String findPropertyName(String name) {
        return this.caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }
}
