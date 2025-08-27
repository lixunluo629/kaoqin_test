package org.apache.ibatis.javassist.util.proxy;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/javassist/util/proxy/SecurityActions.class */
class SecurityActions {
    SecurityActions() {
    }

    static Method[] getDeclaredMethods(final Class clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethods();
        }
        return (Method[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return clazz.getDeclaredMethods();
            }
        });
    }

    static Constructor[] getDeclaredConstructors(final Class clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructors();
        }
        return (Constructor[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.2
            @Override // java.security.PrivilegedAction
            public Object run() {
                return clazz.getDeclaredConstructors();
            }
        });
    }

    static Method getDeclaredMethod(final Class clazz, final String name, final Class[] types) throws NoSuchMethodException {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethod(name, types);
        }
        try {
            return (Method) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.3
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    return clazz.getDeclaredMethod(name, types);
                }
            });
        } catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw ((NoSuchMethodException) e.getCause());
            }
            throw new RuntimeException(e.getCause());
        }
    }

    static Constructor getDeclaredConstructor(final Class clazz, final Class[] types) throws NoSuchMethodException {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructor(types);
        }
        try {
            return (Constructor) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.4
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    return clazz.getDeclaredConstructor(types);
                }
            });
        } catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw ((NoSuchMethodException) e.getCause());
            }
            throw new RuntimeException(e.getCause());
        }
    }

    static void setAccessible(final AccessibleObject ao, final boolean accessible) throws SecurityException {
        if (System.getSecurityManager() == null) {
            ao.setAccessible(accessible);
        } else {
            AccessController.doPrivileged(new PrivilegedAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.5
                @Override // java.security.PrivilegedAction
                public Object run() throws SecurityException {
                    ao.setAccessible(accessible);
                    return null;
                }
            });
        }
    }

    static void set(final Field fld, final Object target, final Object value) throws IllegalAccessException, PrivilegedActionException, IllegalArgumentException {
        if (System.getSecurityManager() == null) {
            fld.set(target, value);
            return;
        }
        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: org.apache.ibatis.javassist.util.proxy.SecurityActions.6
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    fld.set(target, value);
                    return null;
                }
            });
        } catch (PrivilegedActionException e) {
            if (e.getCause() instanceof NoSuchMethodException) {
                throw ((IllegalAccessException) e.getCause());
            }
            throw new RuntimeException(e.getCause());
        }
    }
}
