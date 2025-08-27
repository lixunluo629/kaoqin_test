package org.hibernate.validator.internal.util.annotationfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.hibernate.validator.internal.util.privilegedactions.ConstructorInstance;
import org.hibernate.validator.internal.util.privilegedactions.GetClassLoader;
import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredConstructor;

/* loaded from: hibernate-validator-5.3.6.Final.jar:org/hibernate/validator/internal/util/annotationfactory/AnnotationFactory.class */
public class AnnotationFactory {
    private AnnotationFactory() {
    }

    public static <T extends Annotation> T create(AnnotationDescriptor<T> annotationDescriptor) {
        try {
            return (T) getProxyInstance(Proxy.getProxyClass((ClassLoader) run(GetClassLoader.fromClass(annotationDescriptor.type())), annotationDescriptor.type()), new AnnotationProxy(annotationDescriptor));
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e2) {
            throw new RuntimeException(e2);
        }
    }

    private static <T extends Annotation> T getProxyInstance(Class<T> proxyClass, InvocationHandler handler) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, IllegalArgumentException, InvocationTargetException {
        Constructor<T> constructor = (Constructor) run(GetDeclaredConstructor.action(proxyClass, InvocationHandler.class));
        return (T) run(ConstructorInstance.action(constructor, handler));
    }

    private static <T> T run(PrivilegedAction<T> privilegedAction) {
        return System.getSecurityManager() != null ? (T) AccessController.doPrivileged(privilegedAction) : privilegedAction.run();
    }
}
