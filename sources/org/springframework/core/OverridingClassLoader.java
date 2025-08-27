package org.springframework.core;

import java.io.IOException;
import java.io.InputStream;
import org.springframework.lang.UsesJava7;
import org.springframework.util.FileCopyUtils;

@UsesJava7
/* loaded from: spring-core-4.3.25.RELEASE.jar:org/springframework/core/OverridingClassLoader.class */
public class OverridingClassLoader extends DecoratingClassLoader {
    public static final String[] DEFAULT_EXCLUDED_PACKAGES = {"java.", "javax.", "sun.", "oracle.", "javassist.", "org.aspectj.", "net.sf.cglib."};
    private static final String CLASS_FILE_SUFFIX = ".class";
    private final ClassLoader overrideDelegate;

    static {
        if (parallelCapableClassLoaderAvailable) {
            ClassLoader.registerAsParallelCapable();
        }
    }

    public OverridingClassLoader(ClassLoader parent) {
        this(parent, null);
    }

    public OverridingClassLoader(ClassLoader parent, ClassLoader overrideDelegate) {
        super(parent);
        this.overrideDelegate = overrideDelegate;
        for (String packageName : DEFAULT_EXCLUDED_PACKAGES) {
            excludePackage(packageName);
        }
    }

    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (this.overrideDelegate != null && isEligibleForOverriding(name)) {
            return this.overrideDelegate.loadClass(name);
        }
        return super.loadClass(name);
    }

    @Override // java.lang.ClassLoader
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result;
        if (isEligibleForOverriding(name) && (result = loadClassForOverriding(name)) != null) {
            if (resolve) {
                resolveClass(result);
            }
            return result;
        }
        return super.loadClass(name, resolve);
    }

    protected boolean isEligibleForOverriding(String className) {
        return !isExcluded(className);
    }

    protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
        byte[] bytes;
        Class<?> result = findLoadedClass(name);
        if (result == null && (bytes = loadBytesForClass(name)) != null) {
            result = defineClass(name, bytes, 0, bytes.length);
        }
        return result;
    }

    protected byte[] loadBytesForClass(String name) throws ClassNotFoundException {
        InputStream is = openStreamForClass(name);
        if (is == null) {
            return null;
        }
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(is);
            return transformIfNecessary(name, bytes);
        } catch (IOException ex) {
            throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
        }
    }

    protected InputStream openStreamForClass(String name) {
        String internalName = name.replace('.', '/') + ".class";
        return getParent().getResourceAsStream(internalName);
    }

    protected byte[] transformIfNecessary(String name, byte[] bytes) {
        return bytes;
    }
}
