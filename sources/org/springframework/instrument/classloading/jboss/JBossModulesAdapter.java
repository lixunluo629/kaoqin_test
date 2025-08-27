package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.springframework.util.ReflectionUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/jboss/JBossModulesAdapter.class */
class JBossModulesAdapter implements JBossClassLoaderAdapter {
    private static final String DELEGATING_TRANSFORMER_CLASS_NAME = "org.jboss.as.server.deployment.module.DelegatingClassFileTransformer";
    private final ClassLoader classLoader;
    private final Method addTransformer;
    private final Object delegatingTransformer;

    public JBossModulesAdapter(ClassLoader classLoader) {
        this.classLoader = classLoader;
        try {
            Field transformer = ReflectionUtils.findField(classLoader.getClass(), "transformer");
            if (transformer == null) {
                throw new IllegalArgumentException("Could not find 'transformer' field on JBoss ClassLoader: " + classLoader.getClass().getName());
            }
            transformer.setAccessible(true);
            this.delegatingTransformer = transformer.get(classLoader);
            if (!this.delegatingTransformer.getClass().getName().equals(DELEGATING_TRANSFORMER_CLASS_NAME)) {
                throw new IllegalStateException("Transformer not of the expected type DelegatingClassFileTransformer: " + this.delegatingTransformer.getClass().getName());
            }
            this.addTransformer = ReflectionUtils.findMethod(this.delegatingTransformer.getClass(), "addTransformer", ClassFileTransformer.class);
            if (this.addTransformer == null) {
                throw new IllegalArgumentException("Could not find 'addTransformer' method on JBoss DelegatingClassFileTransformer: " + this.delegatingTransformer.getClass().getName());
            }
            this.addTransformer.setAccessible(true);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not initialize JBoss LoadTimeWeaver", ex);
        }
    }

    @Override // org.springframework.instrument.classloading.jboss.JBossClassLoaderAdapter
    public void addTransformer(ClassFileTransformer transformer) {
        try {
            this.addTransformer.invoke(this.delegatingTransformer, transformer);
        } catch (Throwable ex) {
            throw new IllegalStateException("Could not add transformer on JBoss 7 ClassLoader " + this.classLoader, ex);
        }
    }

    @Override // org.springframework.instrument.classloading.jboss.JBossClassLoaderAdapter
    public ClassLoader getInstrumentableClassLoader() {
        return this.classLoader;
    }
}
