package org.springframework.instrument.classloading.jboss;

import java.lang.instrument.ClassFileTransformer;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.instrument.classloading.SimpleThrowawayClassLoader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/* loaded from: spring-context-4.3.25.RELEASE.jar:org/springframework/instrument/classloading/jboss/JBossLoadTimeWeaver.class */
public class JBossLoadTimeWeaver implements LoadTimeWeaver {
    private final JBossClassLoaderAdapter adapter;

    public JBossLoadTimeWeaver() {
        this(ClassUtils.getDefaultClassLoader());
    }

    public JBossLoadTimeWeaver(ClassLoader classLoader) {
        Assert.notNull(classLoader, "ClassLoader must not be null");
        if (classLoader.getClass().getName().startsWith("org.jboss.modules")) {
            this.adapter = new JBossModulesAdapter(classLoader);
        } else {
            this.adapter = new JBossMCAdapter(classLoader);
        }
    }

    @Override // org.springframework.instrument.classloading.LoadTimeWeaver
    public void addTransformer(ClassFileTransformer transformer) {
        this.adapter.addTransformer(transformer);
    }

    @Override // org.springframework.instrument.classloading.LoadTimeWeaver
    public ClassLoader getInstrumentableClassLoader() {
        return this.adapter.getInstrumentableClassLoader();
    }

    @Override // org.springframework.instrument.classloading.LoadTimeWeaver
    public ClassLoader getThrowawayClassLoader() {
        return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
    }
}
