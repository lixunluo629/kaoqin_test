package org.ehcache.sizeof.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/annotations/AnnotationProxyFactory.class */
public final class AnnotationProxyFactory {
    private AnnotationProxyFactory() {
    }

    public static <T extends Annotation> T getAnnotationProxy(Annotation customAnnotation, Class<T> referenceAnnotation) {
        AnnotationInvocationHandler handler = new AnnotationInvocationHandler(customAnnotation);
        return (T) Proxy.newProxyInstance(referenceAnnotation.getClassLoader(), new Class[]{referenceAnnotation}, handler);
    }

    /* loaded from: ehcache-3.2.3.jar:org/ehcache/sizeof/annotations/AnnotationProxyFactory$AnnotationInvocationHandler.class */
    private static class AnnotationInvocationHandler implements InvocationHandler {
        private final Annotation customAnnotation;

        public AnnotationInvocationHandler(Annotation customAnnotation) {
            this.customAnnotation = customAnnotation;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method methodOnCustom = getMatchingMethodOnGivenAnnotation(method);
            if (methodOnCustom != null) {
                return methodOnCustom.invoke(this.customAnnotation, args);
            }
            Object defaultValue = method.getDefaultValue();
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new UnsupportedOperationException("The method \"" + method.getName() + "\" does not exist in the custom annotation, and there is no default value for it in the reference annotation, please implement this method in your custom annotation.");
        }

        private Method getMatchingMethodOnGivenAnnotation(Method method) throws NoSuchMethodException, SecurityException {
            try {
                Method customMethod = this.customAnnotation.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
                if (customMethod.getReturnType().isAssignableFrom(method.getReturnType())) {
                    return customMethod;
                }
                return null;
            } catch (NoSuchMethodException e) {
                return null;
            }
        }
    }
}
