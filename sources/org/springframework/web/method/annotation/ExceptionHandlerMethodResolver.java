package org.springframework.web.method.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/method/annotation/ExceptionHandlerMethodResolver.class */
public class ExceptionHandlerMethodResolver {
    public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = new ReflectionUtils.MethodFilter() { // from class: org.springframework.web.method.annotation.ExceptionHandlerMethodResolver.1
        @Override // org.springframework.util.ReflectionUtils.MethodFilter
        public boolean matches(Method method) {
            return AnnotationUtils.findAnnotation(method, ExceptionHandler.class) != null;
        }
    };
    private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", new Class[0]);
    private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentHashMap(16);
    private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentHashMap(16);

    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS)) {
            for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
                addExceptionMapping(exceptionType, method);
            }
        }
    }

    private List<Class<? extends Throwable>> detectExceptionMappings(Method method) {
        ArrayList arrayList = new ArrayList();
        detectAnnotationExceptionMappings(method, arrayList);
        if (arrayList.isEmpty()) {
            for (Class<?> paramType : method.getParameterTypes()) {
                if (Throwable.class.isAssignableFrom(paramType)) {
                    arrayList.add(paramType);
                }
            }
        }
        if (arrayList.isEmpty()) {
            throw new IllegalStateException("No exception types mapped to " + method);
        }
        return arrayList;
    }

    protected void detectAnnotationExceptionMappings(Method method, List<Class<? extends Throwable>> result) {
        ExceptionHandler ann = (ExceptionHandler) AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
        result.addAll(Arrays.asList(ann.value()));
    }

    private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {
        Method oldMethod = this.mappedMethods.put(exceptionType, method);
        if (oldMethod != null && !oldMethod.equals(method)) {
            throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" + exceptionType + "]: {" + oldMethod + ", " + method + "}");
        }
    }

    public boolean hasExceptionMappings() {
        return !this.mappedMethods.isEmpty();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Method resolveMethod(Exception exception) {
        Throwable cause;
        Method method = resolveMethodByExceptionType(exception.getClass());
        if (method == null && (cause = exception.getCause()) != null) {
            method = resolveMethodByExceptionType(cause.getClass());
        }
        return method;
    }

    public Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionType) {
        Method method = this.exceptionLookupCache.get(exceptionType);
        if (method == null) {
            method = getMappedMethod(exceptionType);
            this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
        }
        if (method != NO_METHOD_FOUND) {
            return method;
        }
        return null;
    }

    private Method getMappedMethod(Class<? extends Throwable> exceptionType) {
        List<Class<? extends Throwable>> matches = new ArrayList<>();
        for (Class<? extends Throwable> mappedException : this.mappedMethods.keySet()) {
            if (mappedException.isAssignableFrom(exceptionType)) {
                matches.add(mappedException);
            }
        }
        if (!matches.isEmpty()) {
            Collections.sort(matches, new ExceptionDepthComparator(exceptionType));
            return this.mappedMethods.get(matches.get(0));
        }
        return null;
    }
}
