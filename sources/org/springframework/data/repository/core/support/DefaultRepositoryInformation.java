package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.CrudMethods;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/core/support/DefaultRepositoryInformation.class */
class DefaultRepositoryInformation implements RepositoryInformation {
    private static final TypeVariable<Class<Repository>>[] PARAMETERS = Repository.class.getTypeParameters();
    private static final String DOMAIN_TYPE_NAME = PARAMETERS[0].getName();
    private static final String ID_TYPE_NAME = PARAMETERS[1].getName();
    private final Map<Method, Method> methodCache = new ConcurrentHashMap();
    private final RepositoryMetadata metadata;
    private final Class<?> repositoryBaseClass;
    private final Class<?> customImplementationClass;

    public DefaultRepositoryInformation(RepositoryMetadata metadata, Class<?> repositoryBaseClass, Class<?> customImplementationClass) {
        Assert.notNull(metadata, "Metadata must not be null!");
        Assert.notNull(repositoryBaseClass, "RepositoryBaseClass must not be null!");
        this.metadata = metadata;
        this.repositoryBaseClass = repositoryBaseClass;
        this.customImplementationClass = customImplementationClass;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getDomainType() {
        return this.metadata.getDomainType();
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<? extends Serializable> getIdType() {
        return this.metadata.getIdType();
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public Class<?> getRepositoryBaseClass() {
        return this.repositoryBaseClass;
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public Method getTargetClassMethod(Method method) throws SecurityException {
        if (this.methodCache.containsKey(method)) {
            return this.methodCache.get(method);
        }
        Method result = getTargetClassMethod(method, this.customImplementationClass);
        if (!result.equals(method)) {
            return cacheAndReturn(method, result);
        }
        return cacheAndReturn(method, getTargetClassMethod(method, this.repositoryBaseClass));
    }

    private Method cacheAndReturn(Method key, Method value) {
        if (value != null) {
            ReflectionUtils.makeAccessible(value);
        }
        this.methodCache.put(key, value);
        return value;
    }

    private boolean isTargetClassMethod(Method method, Class<?> targetType) {
        Assert.notNull(method, "Method must not be null!");
        if (targetType == null) {
            return false;
        }
        return method.getDeclaringClass().isAssignableFrom(targetType) || !method.equals(getTargetClassMethod(method, targetType));
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public Set<Method> getQueryMethods() throws SecurityException {
        Set<Method> result = new HashSet<>();
        for (Method method : getRepositoryInterface().getMethods()) {
            Method method2 = ClassUtils.getMostSpecificMethod(method, getRepositoryInterface());
            if (isQueryMethodCandidate(method2)) {
                result.add(method2);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private boolean isQueryMethodCandidate(Method method) {
        return (method.isBridge() || org.springframework.data.util.ReflectionUtils.isDefaultMethod(method) || Modifier.isStatic(method.getModifiers()) || (!isQueryAnnotationPresentOn(method) && (isCustomMethod(method) || isBaseClassMethod(method)))) ? false : true;
    }

    private boolean isQueryAnnotationPresentOn(Method method) {
        return AnnotationUtils.findAnnotation(method, QueryAnnotation.class) != null;
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public boolean isCustomMethod(Method method) {
        return isTargetClassMethod(method, this.customImplementationClass);
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public boolean isQueryMethod(Method method) {
        return getQueryMethods().contains(method);
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public boolean isBaseClassMethod(Method method) {
        Assert.notNull(method, "Method must not be null!");
        return isTargetClassMethod(method, this.repositoryBaseClass);
    }

    Method getTargetClassMethod(Method method, Class<?> baseClass) throws SecurityException {
        if (baseClass == null) {
            return method;
        }
        Method result = ReflectionUtils.findMethod(baseClass, method.getName(), method.getParameterTypes());
        if (result != null) {
            return result;
        }
        for (Method baseClassMethod : baseClass.getMethods()) {
            if (method.getName().equals(baseClassMethod.getName()) && method.getParameterTypes().length == baseClassMethod.getParameterTypes().length && parametersMatch(method, baseClassMethod)) {
                return baseClassMethod;
            }
        }
        return method;
    }

    @Override // org.springframework.data.repository.core.RepositoryInformation
    public boolean hasCustomMethod() throws SecurityException {
        Class<?> repositoryInterface = getRepositoryInterface();
        if (org.springframework.data.repository.util.ClassUtils.isGenericRepositoryInterface(repositoryInterface)) {
            return false;
        }
        for (Method method : repositoryInterface.getMethods()) {
            if (isCustomMethod(method) && !isBaseClassMethod(method)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getRepositoryInterface() {
        return this.metadata.getRepositoryInterface();
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Class<?> getReturnedDomainClass(Method method) {
        return this.metadata.getReturnedDomainClass(method);
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public CrudMethods getCrudMethods() {
        return this.metadata.getCrudMethods();
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public boolean isPagingRepository() {
        return this.metadata.isPagingRepository();
    }

    @Override // org.springframework.data.repository.core.RepositoryMetadata
    public Set<Class<?>> getAlternativeDomainTypes() {
        return this.metadata.getAlternativeDomainTypes();
    }

    private boolean parametersMatch(Method method, Method baseClassMethod) {
        Class<?>[] methodParameterTypes = method.getParameterTypes();
        Type[] genericTypes = baseClassMethod.getGenericParameterTypes();
        Class<?>[] types = baseClassMethod.getParameterTypes();
        for (int i = 0; i < genericTypes.length; i++) {
            Type genericType = genericTypes[i];
            Class<?> type = types[i];
            MethodParameter parameter = new MethodParameter(method, i);
            Class<?> parameterType = GenericTypeResolver.resolveParameterType(parameter, this.metadata.getRepositoryInterface());
            if (genericType instanceof TypeVariable) {
                if (!matchesGenericType((TypeVariable) genericType, ResolvableType.forMethodParameter(parameter))) {
                    return false;
                }
            } else if (!types[i].equals(parameterType) && (!type.isAssignableFrom(parameterType) || !type.equals(methodParameterTypes[i]))) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesGenericType(TypeVariable<?> variable, ResolvableType parameterType) {
        GenericDeclaration declaration = variable.getGenericDeclaration();
        if (declaration instanceof Class) {
            ResolvableType entityType = ResolvableType.forClass(getDomainType());
            ResolvableType idClass = ResolvableType.forClass(getIdType());
            if (ID_TYPE_NAME.equals(variable.getName()) && parameterType.isAssignableFrom(idClass)) {
                return true;
            }
            Type boundType = variable.getBounds()[0];
            String referenceName = boundType instanceof TypeVariable ? boundType.toString() : variable.toString();
            return DOMAIN_TYPE_NAME.equals(referenceName) && parameterType.isAssignableFrom(entityType);
        }
        for (Type type : variable.getBounds()) {
            if (ResolvableType.forType(type).isAssignableFrom(parameterType)) {
                return true;
            }
        }
        return false;
    }
}
