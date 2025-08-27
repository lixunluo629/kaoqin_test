package org.springframework.scripting.bsh;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.Primitive;
import bsh.XThis;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.springframework.core.NestedRuntimeException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/bsh/BshScriptUtils.class */
public abstract class BshScriptUtils {
    public static Object createBshObject(String scriptSource) throws EvalError {
        return createBshObject(scriptSource, null, null);
    }

    public static Object createBshObject(String scriptSource, Class<?>... scriptInterfaces) throws EvalError {
        return createBshObject(scriptSource, scriptInterfaces, ClassUtils.getDefaultClassLoader());
    }

    public static Object createBshObject(String scriptSource, Class<?>[] scriptInterfaces, ClassLoader classLoader) throws EvalError {
        Object result = evaluateBshScript(scriptSource, scriptInterfaces, classLoader);
        if (result instanceof Class) {
            Class<?> clazz = (Class) result;
            try {
                return clazz.newInstance();
            } catch (Throwable ex) {
                throw new IllegalStateException("Could not instantiate script class: " + clazz.getName(), ex);
            }
        }
        return result;
    }

    static Class<?> determineBshObjectType(String scriptSource, ClassLoader classLoader) throws EvalError {
        Assert.hasText(scriptSource, "Script source must not be empty");
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(classLoader);
        Object result = interpreter.eval(scriptSource);
        if (result instanceof Class) {
            return (Class) result;
        }
        if (result != null) {
            return result.getClass();
        }
        return null;
    }

    static Object evaluateBshScript(String scriptSource, Class<?>[] scriptInterfaces, ClassLoader classLoader) throws EvalError {
        Assert.hasText(scriptSource, "Script source must not be empty");
        Interpreter interpreter = new Interpreter();
        interpreter.setClassLoader(classLoader);
        Object result = interpreter.eval(scriptSource);
        if (result != null) {
            return result;
        }
        Assert.notEmpty(scriptInterfaces, "Given script requires a script proxy: At least one script interface is required.");
        XThis xt = (XThis) interpreter.eval("return this");
        return Proxy.newProxyInstance(classLoader, scriptInterfaces, new BshObjectInvocationHandler(xt));
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/bsh/BshScriptUtils$BshObjectInvocationHandler.class */
    private static class BshObjectInvocationHandler implements InvocationHandler {
        private final XThis xt;

        public BshObjectInvocationHandler(XThis xt) {
            this.xt = xt;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (ReflectionUtils.isEqualsMethod(method)) {
                return Boolean.valueOf(isProxyForSameBshObject(args[0]));
            }
            if (ReflectionUtils.isHashCodeMethod(method)) {
                return Integer.valueOf(this.xt.hashCode());
            }
            if (ReflectionUtils.isToStringMethod(method)) {
                return "BeanShell object [" + this.xt + "]";
            }
            try {
                Object result = this.xt.invokeMethod(method.getName(), args);
                if (result == Primitive.NULL || result == Primitive.VOID) {
                    return null;
                }
                if (result instanceof Primitive) {
                    return ((Primitive) result).getValue();
                }
                return result;
            } catch (EvalError ex) {
                throw new BshExecutionException(ex);
            }
        }

        private boolean isProxyForSameBshObject(Object other) throws IllegalArgumentException {
            if (!Proxy.isProxyClass(other.getClass())) {
                return false;
            }
            InvocationHandler ih = Proxy.getInvocationHandler(other);
            return (ih instanceof BshObjectInvocationHandler) && this.xt.equals(((BshObjectInvocationHandler) ih).xt);
        }
    }

    /* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/scripting/bsh/BshScriptUtils$BshExecutionException.class */
    public static class BshExecutionException extends NestedRuntimeException {
        private BshExecutionException(EvalError ex) {
            super("BeanShell script execution failed", ex);
        }
    }
}
