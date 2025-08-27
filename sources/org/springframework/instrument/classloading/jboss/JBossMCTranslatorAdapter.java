package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/jboss/JBossMCTranslatorAdapter.class */
class JBossMCTranslatorAdapter implements InvocationHandler {
    private final ClassFileTransformer transformer;

    public JBossMCTranslatorAdapter(ClassFileTransformer transformer) {
        this.transformer = transformer;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String name = method.getName();
        if ("equals".equals(name)) {
            return Boolean.valueOf(proxy == args[0]);
        }
        if (IdentityNamingStrategy.HASH_CODE_KEY.equals(name)) {
            return Integer.valueOf(hashCode());
        }
        if ("toString".equals(name)) {
            return toString();
        }
        if ("transform".equals(name)) {
            return transform((ClassLoader) args[0], (String) args[1], (Class) args[2], (ProtectionDomain) args[3], (byte[]) args[4]);
        }
        if ("unregisterClassLoader".equals(name)) {
            unregisterClassLoader((ClassLoader) args[0]);
            return null;
        }
        throw new IllegalArgumentException("Unknown method: " + method);
    }

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception {
        return this.transformer.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
    }

    public void unregisterClassLoader(ClassLoader loader) {
    }

    public String toString() {
        return getClass().getName() + " for transformer: " + this.transformer;
    }
}
