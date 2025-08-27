package org.springframework.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/MethodParameter.class */
public class MethodParameter {
    private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
    private static final Class<?> javaUtilOptionalClass;
    private final Method method;
    private final Constructor<?> constructor;
    private final int parameterIndex;
    private int nestingLevel;
    Map<Integer, Integer> typeIndexesPerLevel;
    private volatile Class<?> containingClass;
    private volatile Class<?> parameterType;
    private volatile Type genericParameterType;
    private volatile Annotation[] parameterAnnotations;
    private volatile ParameterNameDiscoverer parameterNameDiscoverer;
    private volatile String parameterName;
    private volatile MethodParameter nestedMethodParameter;

    static {
        Class<?> clazz;
        try {
            clazz = ClassUtils.forName("java.util.Optional", MethodParameter.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            clazz = null;
        }
        javaUtilOptionalClass = clazz;
    }

    public MethodParameter(Method method, int parameterIndex) {
        this(method, parameterIndex, 1);
    }

    public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
        Assert.notNull(method, "Method must not be null");
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.constructor = null;
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex) {
        this(constructor, parameterIndex, 1);
    }

    public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel) {
        Assert.notNull(constructor, "Constructor must not be null");
        this.constructor = constructor;
        this.parameterIndex = parameterIndex;
        this.nestingLevel = nestingLevel;
        this.method = null;
    }

    public MethodParameter(MethodParameter original) {
        Assert.notNull(original, "Original must not be null");
        this.method = original.method;
        this.constructor = original.constructor;
        this.parameterIndex = original.parameterIndex;
        this.nestingLevel = original.nestingLevel;
        this.typeIndexesPerLevel = original.typeIndexesPerLevel;
        this.containingClass = original.containingClass;
        this.parameterType = original.parameterType;
        this.genericParameterType = original.genericParameterType;
        this.parameterAnnotations = original.parameterAnnotations;
        this.parameterNameDiscoverer = original.parameterNameDiscoverer;
        this.parameterName = original.parameterName;
    }

    public Method getMethod() {
        return this.method;
    }

    public Constructor<?> getConstructor() {
        return this.constructor;
    }

    public Class<?> getDeclaringClass() {
        return getMember().getDeclaringClass();
    }

    public Member getMember() {
        if (this.method != null) {
            return this.method;
        }
        return this.constructor;
    }

    public AnnotatedElement getAnnotatedElement() {
        if (this.method != null) {
            return this.method;
        }
        return this.constructor;
    }

    public int getParameterIndex() {
        return this.parameterIndex;
    }

    public void increaseNestingLevel() {
        this.nestingLevel++;
    }

    public void decreaseNestingLevel() {
        getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
        this.nestingLevel--;
    }

    public int getNestingLevel() {
        return this.nestingLevel;
    }

    public void setTypeIndexForCurrentLevel(int typeIndex) {
        getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
    }

    public Integer getTypeIndexForCurrentLevel() {
        return getTypeIndexForLevel(this.nestingLevel);
    }

    public Integer getTypeIndexForLevel(int nestingLevel) {
        return getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
    }

    private Map<Integer, Integer> getTypeIndexesPerLevel() {
        if (this.typeIndexesPerLevel == null) {
            this.typeIndexesPerLevel = new HashMap(4);
        }
        return this.typeIndexesPerLevel;
    }

    public MethodParameter nested() {
        if (this.nestedMethodParameter != null) {
            return this.nestedMethodParameter;
        }
        MethodParameter nestedParam = mo7600clone();
        nestedParam.nestingLevel = this.nestingLevel + 1;
        this.nestedMethodParameter = nestedParam;
        return nestedParam;
    }

    public boolean isOptional() {
        return getParameterType() == javaUtilOptionalClass;
    }

    public MethodParameter nestedIfOptional() {
        return isOptional() ? nested() : this;
    }

    void setContainingClass(Class<?> containingClass) {
        this.containingClass = containingClass;
    }

    public Class<?> getContainingClass() {
        return this.containingClass != null ? this.containingClass : getDeclaringClass();
    }

    void setParameterType(Class<?> parameterType) {
        this.parameterType = parameterType;
    }

    public Class<?> getParameterType() {
        Class<?> cls;
        Class<?> paramType = this.parameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = method != null ? method.getReturnType() : Void.TYPE;
            } else {
                if (this.method != null) {
                    cls = this.method.getParameterTypes()[this.parameterIndex];
                } else {
                    cls = this.constructor.getParameterTypes()[this.parameterIndex];
                }
                paramType = cls;
            }
            this.parameterType = paramType;
        }
        return paramType;
    }

    public Type getGenericParameterType() {
        Type paramType = this.genericParameterType;
        if (paramType == null) {
            if (this.parameterIndex < 0) {
                Method method = getMethod();
                paramType = method != null ? method.getGenericReturnType() : Void.TYPE;
            } else {
                Type[] genericParameterTypes = this.method != null ? this.method.getGenericParameterTypes() : this.constructor.getGenericParameterTypes();
                int index = this.parameterIndex;
                if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() && !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) && genericParameterTypes.length == this.constructor.getParameterTypes().length - 1) {
                    index = this.parameterIndex - 1;
                }
                paramType = (index < 0 || index >= genericParameterTypes.length) ? getParameterType() : genericParameterTypes[index];
            }
            this.genericParameterType = paramType;
        }
        return paramType;
    }

    public Class<?> getNestedParameterType() {
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            for (int i = 2; i <= this.nestingLevel; i++) {
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Integer index = getTypeIndexForLevel(i);
                    type = args[index != null ? index.intValue() : args.length - 1];
                }
            }
            if (type instanceof Class) {
                return (Class) type;
            }
            if (type instanceof ParameterizedType) {
                Type arg = ((ParameterizedType) type).getRawType();
                if (arg instanceof Class) {
                    return (Class) arg;
                }
                return Object.class;
            }
            return Object.class;
        }
        return getParameterType();
    }

    public Type getNestedGenericParameterType() {
        if (this.nestingLevel > 1) {
            Type type = getGenericParameterType();
            for (int i = 2; i <= this.nestingLevel; i++) {
                if (type instanceof ParameterizedType) {
                    Type[] args = ((ParameterizedType) type).getActualTypeArguments();
                    Integer index = getTypeIndexForLevel(i);
                    type = args[index != null ? index.intValue() : args.length - 1];
                }
            }
            return type;
        }
        return getGenericParameterType();
    }

    public Annotation[] getMethodAnnotations() {
        return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A extends Annotation> A getMethodAnnotation(Class<A> cls) {
        return (A) adaptAnnotation(getAnnotatedElement().getAnnotation(cls));
    }

    public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType) {
        return getAnnotatedElement().isAnnotationPresent(annotationType);
    }

    public Annotation[] getParameterAnnotations() {
        Annotation[] paramAnns = this.parameterAnnotations;
        if (paramAnns == null) {
            Annotation[][] annotationArray = this.method != null ? this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations();
            int index = this.parameterIndex;
            if (this.constructor != null && this.constructor.getDeclaringClass().isMemberClass() && !Modifier.isStatic(this.constructor.getDeclaringClass().getModifiers()) && annotationArray.length == this.constructor.getParameterTypes().length - 1) {
                index = this.parameterIndex - 1;
            }
            paramAnns = (index < 0 || index >= annotationArray.length) ? EMPTY_ANNOTATION_ARRAY : adaptAnnotationArray(annotationArray[index]);
            this.parameterAnnotations = paramAnns;
        }
        return paramAnns;
    }

    public boolean hasParameterAnnotations() {
        return getParameterAnnotations().length != 0;
    }

    public <A extends Annotation> A getParameterAnnotation(Class<A> cls) {
        for (Annotation annotation : getParameterAnnotations()) {
            A a = (A) annotation;
            if (cls.isInstance(a)) {
                return a;
            }
        }
        return null;
    }

    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return getParameterAnnotation(annotationType) != null;
    }

    public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer) {
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    public String getParameterName() {
        ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
        if (discoverer != null) {
            String[] parameterNames = this.method != null ? discoverer.getParameterNames(this.method) : discoverer.getParameterNames(this.constructor);
            if (parameterNames != null) {
                this.parameterName = parameterNames[this.parameterIndex];
            }
            this.parameterNameDiscoverer = null;
        }
        return this.parameterName;
    }

    protected <A extends Annotation> A adaptAnnotation(A annotation) {
        return annotation;
    }

    protected Annotation[] adaptAnnotationArray(Annotation[] annotations) {
        return annotations;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodParameter)) {
            return false;
        }
        MethodParameter otherParam = (MethodParameter) other;
        return getContainingClass() == otherParam.getContainingClass() && ObjectUtils.nullSafeEquals(this.typeIndexesPerLevel, otherParam.typeIndexesPerLevel) && this.nestingLevel == otherParam.nestingLevel && this.parameterIndex == otherParam.parameterIndex && getMember().equals(otherParam.getMember());
    }

    public int hashCode() {
        return (getMember().hashCode() * 31) + this.parameterIndex;
    }

    public String toString() {
        return (this.method != null ? "method '" + this.method.getName() + "'" : "constructor") + " parameter " + this.parameterIndex;
    }

    @Override // 
    /* renamed from: clone */
    public MethodParameter mo7600clone() {
        return new MethodParameter(this);
    }

    public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex) {
        if (methodOrConstructor instanceof Method) {
            return new MethodParameter((Method) methodOrConstructor, parameterIndex);
        }
        if (methodOrConstructor instanceof Constructor) {
            return new MethodParameter((Constructor<?>) methodOrConstructor, parameterIndex);
        }
        throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
    }
}
