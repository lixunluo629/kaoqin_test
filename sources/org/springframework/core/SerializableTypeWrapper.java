package org.springframework.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper.class */
abstract class SerializableTypeWrapper {
    private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = {GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class};
    static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap<>(256);

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$SerializableTypeProxy.class */
    interface SerializableTypeProxy {
        TypeProvider getTypeProvider();
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$TypeProvider.class */
    interface TypeProvider extends Serializable {
        Type getType();

        Object getSource();
    }

    SerializableTypeWrapper() {
    }

    public static Type forField(Field field) {
        return forTypeProvider(new FieldTypeProvider(field));
    }

    public static Type forMethodParameter(MethodParameter methodParameter) {
        return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
    }

    public static Type forGenericSuperclass(final Class<?> type) {
        return forTypeProvider(new SimpleTypeProvider() { // from class: org.springframework.core.SerializableTypeWrapper.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
            public Type getType() {
                return type.getGenericSuperclass();
            }
        });
    }

    public static Type[] forGenericInterfaces(final Class<?> type) {
        Type[] result = new Type[type.getGenericInterfaces().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new SimpleTypeProvider() { // from class: org.springframework.core.SerializableTypeWrapper.2
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
                public Type getType() {
                    return type.getGenericInterfaces()[index];
                }
            });
        }
        return result;
    }

    public static Type[] forTypeParameters(final Class<?> type) {
        Type[] result = new Type[type.getTypeParameters().length];
        for (int i = 0; i < result.length; i++) {
            final int index = i;
            result[i] = forTypeProvider(new SimpleTypeProvider() { // from class: org.springframework.core.SerializableTypeWrapper.3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super();
                }

                @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
                public Type getType() {
                    return type.getTypeParameters()[index];
                }
            });
        }
        return result;
    }

    public static <T extends Type> T unwrap(T t) {
        Type type = t;
        while (true) {
            T t2 = (T) type;
            if (t2 instanceof SerializableTypeProxy) {
                type = ((SerializableTypeProxy) t).getTypeProvider().getType();
            } else {
                return t2;
            }
        }
    }

    static Type forTypeProvider(TypeProvider provider) {
        Type providedType = provider.getType();
        if (providedType == null || (providedType instanceof Serializable)) {
            return providedType;
        }
        Type cached = cache.get(providedType);
        if (cached != null) {
            return cached;
        }
        for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
            if (type.isInstance(providedType)) {
                ClassLoader classLoader = provider.getClass().getClassLoader();
                Class<?>[] interfaces = {type, SerializableTypeProxy.class, Serializable.class};
                InvocationHandler handler = new TypeProxyInvocationHandler(provider);
                Type cached2 = (Type) Proxy.newProxyInstance(classLoader, interfaces, handler);
                cache.put(providedType, cached2);
                return cached2;
            }
        }
        throw new IllegalArgumentException("Unsupported Type class: " + providedType.getClass().getName());
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$SimpleTypeProvider.class */
    private static abstract class SimpleTypeProvider implements TypeProvider {
        private SimpleTypeProvider() {
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Object getSource() {
            return null;
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$TypeProxyInvocationHandler.class */
    private static class TypeProxyInvocationHandler implements InvocationHandler, Serializable {
        private final TypeProvider provider;

        public TypeProxyInvocationHandler(TypeProvider provider) {
            this.provider = provider;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("equals")) {
                Object other = args[0];
                if (other instanceof Type) {
                    other = SerializableTypeWrapper.unwrap((Type) other);
                }
                return Boolean.valueOf(this.provider.getType().equals(other));
            }
            if (method.getName().equals(IdentityNamingStrategy.HASH_CODE_KEY)) {
                return Integer.valueOf(this.provider.getType().hashCode());
            }
            if (method.getName().equals("getTypeProvider")) {
                return this.provider;
            }
            if (Type.class == method.getReturnType() && args == null) {
                return SerializableTypeWrapper.forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, -1));
            }
            if (Type[].class == method.getReturnType() && args == null) {
                Type[] result = new Type[((Type[]) method.invoke(this.provider.getType(), args)).length];
                for (int i = 0; i < result.length; i++) {
                    result[i] = SerializableTypeWrapper.forTypeProvider(new MethodInvokeTypeProvider(this.provider, method, i));
                }
                return result;
            }
            try {
                return method.invoke(this.provider.getType(), args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$FieldTypeProvider.class */
    static class FieldTypeProvider implements TypeProvider {
        private final String fieldName;
        private final Class<?> declaringClass;
        private transient Field field;

        public FieldTypeProvider(Field field) {
            this.fieldName = field.getName();
            this.declaringClass = field.getDeclaringClass();
            this.field = field;
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Type getType() {
            return this.field.getGenericType();
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Object getSource() {
            return this.field;
        }

        private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
            inputStream.defaultReadObject();
            try {
                this.field = this.declaringClass.getDeclaredField(this.fieldName);
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not find original class structure", ex);
            }
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$MethodParameterTypeProvider.class */
    static class MethodParameterTypeProvider implements TypeProvider {
        private final String methodName;
        private final Class<?>[] parameterTypes;
        private final Class<?> declaringClass;
        private final int parameterIndex;
        private transient MethodParameter methodParameter;

        public MethodParameterTypeProvider(MethodParameter methodParameter) {
            if (methodParameter.getMethod() != null) {
                this.methodName = methodParameter.getMethod().getName();
                this.parameterTypes = methodParameter.getMethod().getParameterTypes();
            } else {
                this.methodName = null;
                this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
            }
            this.declaringClass = methodParameter.getDeclaringClass();
            this.parameterIndex = methodParameter.getParameterIndex();
            this.methodParameter = methodParameter;
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Type getType() {
            return this.methodParameter.getGenericParameterType();
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Object getSource() {
            return this.methodParameter;
        }

        private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
            inputStream.defaultReadObject();
            try {
                if (this.methodName != null) {
                    this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
                } else {
                    this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
                }
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not find original class structure", ex);
            }
        }
    }

    /* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/SerializableTypeWrapper$MethodInvokeTypeProvider.class */
    static class MethodInvokeTypeProvider implements TypeProvider {
        private final TypeProvider provider;
        private final String methodName;
        private final Class<?> declaringClass;
        private final int index;
        private transient Method method;
        private volatile transient Object result;

        public MethodInvokeTypeProvider(TypeProvider provider, Method method, int index) {
            this.provider = provider;
            this.methodName = method.getName();
            this.declaringClass = method.getDeclaringClass();
            this.index = index;
            this.method = method;
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Type getType() {
            Object result = this.result;
            if (result == null) {
                result = ReflectionUtils.invokeMethod(this.method, this.provider.getType());
                this.result = result;
            }
            return result instanceof Type[] ? ((Type[]) result)[this.index] : (Type) result;
        }

        @Override // org.springframework.core.SerializableTypeWrapper.TypeProvider
        public Object getSource() {
            return null;
        }

        private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
            inputStream.defaultReadObject();
            this.method = ReflectionUtils.findMethod(this.declaringClass, this.methodName);
            if (this.method.getReturnType() != Type.class && this.method.getReturnType() != Type[].class) {
                throw new IllegalStateException("Invalid return type on deserialized method - needs to be Type or Type[]: " + this.method);
            }
        }
    }
}
