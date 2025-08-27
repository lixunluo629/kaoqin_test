package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/jboss/JBossMCAdapter.class */
class JBossMCAdapter implements JBossClassLoaderAdapter {
    private static final String LOADER_NAME = "org.jboss.classloader.spi.base.BaseClassLoader";
    private static final String TRANSLATOR_NAME = "org.jboss.util.loading.Translator";
    private final ClassLoader classLoader;
    private final Object target;
    private final Class<?> translatorClass;
    private final Method addTranslator;

    public JBossMCAdapter(ClassLoader classLoader) {
        try {
            Class<?> clazzLoaderType = classLoader.loadClass(LOADER_NAME);
            ClassLoader clazzLoader = null;
            for (ClassLoader cl = classLoader; cl != null && clazzLoader == null; cl = cl.getParent()) {
                if (clazzLoaderType.isInstance(cl)) {
                    clazzLoader = cl;
                }
            }
            if (clazzLoader == null) {
                throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" + LOADER_NAME + "] implementation is required.");
            }
            this.classLoader = clazzLoader;
            ClassLoader classLoader2 = clazzLoader.getClass().getClassLoader();
            Method method = clazzLoaderType.getDeclaredMethod("getPolicy", new Class[0]);
            ReflectionUtils.makeAccessible(method);
            this.target = method.invoke(this.classLoader, new Object[0]);
            this.translatorClass = classLoader2.loadClass(TRANSLATOR_NAME);
            this.addTranslator = this.target.getClass().getMethod("addTranslator", this.translatorClass);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not initialize JBoss LoadTimeWeaver because the JBoss 6 API classes are not available", ex);
        }
    }

    @Override // org.springframework.instrument.classloading.jboss.JBossClassLoaderAdapter
    public void addTransformer(ClassFileTransformer transformer) throws IllegalArgumentException {
        InvocationHandler adapter = new JBossMCTranslatorAdapter(transformer);
        Object adapterInstance = Proxy.newProxyInstance(this.translatorClass.getClassLoader(), new Class[]{this.translatorClass}, adapter);
        try {
            this.addTranslator.invoke(this.target, adapterInstance);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not add transformer on JBoss 6 ClassLoader " + this.classLoader, ex);
        }
    }

    @Override // org.springframework.instrument.classloading.jboss.JBossClassLoaderAdapter
    public ClassLoader getInstrumentableClassLoader() {
        return this.classLoader;
    }
}
