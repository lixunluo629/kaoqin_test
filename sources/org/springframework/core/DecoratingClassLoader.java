package org.springframework.core;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.lang.UsesJava7;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

@UsesJava7
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/DecoratingClassLoader.class */
public abstract class DecoratingClassLoader extends ClassLoader {
    protected static final boolean parallelCapableClassLoaderAvailable = ClassUtils.hasMethod(ClassLoader.class, "registerAsParallelCapable", new Class[0]);
    private final Set<String> excludedPackages;
    private final Set<String> excludedClasses;

    static {
        if (parallelCapableClassLoaderAvailable) {
            ClassLoader.registerAsParallelCapable();
        }
    }

    public DecoratingClassLoader() {
        this.excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap(8));
        this.excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap(8));
    }

    public DecoratingClassLoader(ClassLoader parent) {
        super(parent);
        this.excludedPackages = Collections.newSetFromMap(new ConcurrentHashMap(8));
        this.excludedClasses = Collections.newSetFromMap(new ConcurrentHashMap(8));
    }

    public void excludePackage(String packageName) {
        Assert.notNull(packageName, "Package name must not be null");
        this.excludedPackages.add(packageName);
    }

    public void excludeClass(String className) {
        Assert.notNull(className, "Class name must not be null");
        this.excludedClasses.add(className);
    }

    protected boolean isExcluded(String className) {
        if (this.excludedClasses.contains(className)) {
            return true;
        }
        for (String packageName : this.excludedPackages) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
}
