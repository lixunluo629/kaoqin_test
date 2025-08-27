package org.hibernate.validator.internal.metadata.raw;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import javax.validation.ParameterNameProvider;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.hibernate.validator.internal.util.ExecutableHelper;
import org.hibernate.validator.internal.util.ReflectionHelper;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ExecutableElement.class */
public abstract class ExecutableElement {
    private String signature;

    public abstract List<String> getParameterNames(ParameterNameProvider parameterNameProvider);

    public abstract Annotation[][] getParameterAnnotations();

    public abstract Class<?>[] getParameterTypes();

    public abstract Class<?> getReturnType();

    public abstract Type[] getGenericParameterTypes();

    public abstract AccessibleObject getAccessibleObject();

    public abstract Member getMember();

    public abstract ElementType getElementType();

    public abstract String getSimpleName();

    public abstract boolean isGetterMethod();

    public abstract boolean equals(Object obj);

    public abstract int hashCode();

    public static ExecutableElement forConstructor(Constructor<?> constructor) {
        return new ConstructorElement(constructor);
    }

    public static List<ExecutableElement> forConstructors(Constructor<?>[] constructors) {
        List<ExecutableElement> executableElements = CollectionHelper.newArrayList(constructors.length);
        for (Constructor<?> constructor : constructors) {
            executableElements.add(forConstructor(constructor));
        }
        return executableElements;
    }

    public static ExecutableElement forMethod(Method method) {
        return new MethodElement(method);
    }

    public static List<ExecutableElement> forMethods(Method[] methods) {
        List<ExecutableElement> executableElements = CollectionHelper.newArrayList(methods.length);
        for (Method method : methods) {
            executableElements.add(forMethod(method));
        }
        return executableElements;
    }

    private ExecutableElement(String name, Class<?>[] parameterTypes) {
        this.signature = ExecutableHelper.getSignature(name, parameterTypes);
    }

    public String getAsString() {
        return getExecutableAsString(getSimpleName(), getParameterTypes());
    }

    public String getSignature() {
        return this.signature;
    }

    public static String getExecutableAsString(String name, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(name);
        sb.append("(");
        boolean isFirst = true;
        for (Class<?> parameterType : parameterTypes) {
            if (!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append(parameterType.getSimpleName());
        }
        sb.append(")");
        return sb.toString();
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ExecutableElement$ConstructorElement.class */
    private static class ConstructorElement extends ExecutableElement {
        private final Constructor<?> constructor;

        private ConstructorElement(Constructor<?> constructor) {
            super(constructor.getDeclaringClass().getSimpleName(), constructor.getParameterTypes());
            this.constructor = constructor;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public List<String> getParameterNames(ParameterNameProvider parameterNameProvider) {
            return parameterNameProvider.getParameterNames(this.constructor);
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Annotation[][] getParameterAnnotations() {
            Annotation[][] parameterAnnotations = this.constructor.getParameterAnnotations();
            int parameterCount = this.constructor.getParameterTypes().length;
            if (parameterAnnotations.length == parameterCount) {
                return parameterAnnotations;
            }
            return (Annotation[][]) paddedLeft(parameterAnnotations, new Annotation[parameterCount], new Annotation[0]);
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Class<?>[] getParameterTypes() {
            return this.constructor.getParameterTypes();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Class<?> getReturnType() {
            return this.constructor.getDeclaringClass();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Type[] getGenericParameterTypes() {
            return this.constructor.getGenericParameterTypes();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public AccessibleObject getAccessibleObject() {
            return this.constructor;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Member getMember() {
            return this.constructor;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public ElementType getElementType() {
            return ElementType.CONSTRUCTOR;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public String getSimpleName() {
            return this.constructor.getDeclaringClass().getSimpleName();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public boolean isGetterMethod() {
            return false;
        }

        public String toString() {
            return this.constructor.toGenericString();
        }

        private <T> T[] paddedLeft(T[] src, T[] dest, T fillElement) {
            int originalCount = src.length;
            int targetCount = dest.length;
            System.arraycopy(src, 0, dest, targetCount - originalCount, originalCount);
            Arrays.fill(dest, 0, targetCount - originalCount, fillElement);
            return dest;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public int hashCode() {
            int result = (31 * 1) + (this.constructor == null ? 0 : this.constructor.hashCode());
            return result;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            ConstructorElement other = (ConstructorElement) obj;
            if (this.constructor == null) {
                if (other.constructor != null) {
                    return false;
                }
                return true;
            }
            if (!this.constructor.equals(other.constructor)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/metadata/raw/ExecutableElement$MethodElement.class */
    private static class MethodElement extends ExecutableElement {
        private final Method method;
        private final boolean isGetterMethod;

        public MethodElement(Method method) {
            super(method.getName(), method.getParameterTypes());
            this.method = method;
            this.isGetterMethod = ReflectionHelper.isGetterMethod(method);
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public List<String> getParameterNames(ParameterNameProvider parameterNameProvider) {
            return parameterNameProvider.getParameterNames(this.method);
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Annotation[][] getParameterAnnotations() {
            return this.method.getParameterAnnotations();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Class<?>[] getParameterTypes() {
            return this.method.getParameterTypes();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Class<?> getReturnType() {
            return this.method.getReturnType();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Type[] getGenericParameterTypes() {
            return this.method.getGenericParameterTypes();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public AccessibleObject getAccessibleObject() {
            return this.method;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public Member getMember() {
            return this.method;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public ElementType getElementType() {
            return ElementType.METHOD;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public String getSimpleName() {
            return this.method.getName();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public boolean isGetterMethod() {
            return this.isGetterMethod;
        }

        public String toString() {
            return this.method.toGenericString();
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public int hashCode() {
            int result = (31 * 1) + (this.method == null ? 0 : this.method.hashCode());
            return result;
        }

        @Override // org.hibernate.validator.internal.metadata.raw.ExecutableElement
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            MethodElement other = (MethodElement) obj;
            if (this.method == null) {
                if (other.method != null) {
                    return false;
                }
                return true;
            }
            if (!this.method.equals(other.method)) {
                return false;
            }
            return true;
        }
    }
}
