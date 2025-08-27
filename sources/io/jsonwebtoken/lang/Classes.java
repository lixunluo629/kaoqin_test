package io.jsonwebtoken.lang;

import java.io.InputStream;
import java.lang.reflect.Constructor;

/* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/lang/Classes.class */
public class Classes {
    private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() { // from class: io.jsonwebtoken.lang.Classes.1
        @Override // io.jsonwebtoken.lang.Classes.ExceptionIgnoringAccessor
        protected ClassLoader doGetClassLoader() throws Throwable {
            return Thread.currentThread().getContextClassLoader();
        }
    };
    private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() { // from class: io.jsonwebtoken.lang.Classes.2
        @Override // io.jsonwebtoken.lang.Classes.ExceptionIgnoringAccessor
        protected ClassLoader doGetClassLoader() throws Throwable {
            return Classes.class.getClassLoader();
        }
    };
    private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() { // from class: io.jsonwebtoken.lang.Classes.3
        @Override // io.jsonwebtoken.lang.Classes.ExceptionIgnoringAccessor
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ClassLoader.getSystemClassLoader();
        }
    };

    /* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/lang/Classes$ClassLoaderAccessor.class */
    private interface ClassLoaderAccessor {
        Class loadClass(String str);

        InputStream getResourceStream(String str);
    }

    public static Class forName(String fqcn) throws UnknownClassException {
        Class clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);
        if (clazz == null) {
            clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
        }
        if (clazz == null) {
            clazz = SYSTEM_CL_ACCESSOR.loadClass(fqcn);
        }
        if (clazz == null) {
            String msg = "Unable to load class named [" + fqcn + "] from the thread context, current, or system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.";
            if (fqcn != null && fqcn.startsWith("com.stormpath.sdk.impl")) {
                msg = msg + "  Have you remembered to include the stormpath-sdk-impl .jar in your runtime classpath?";
            }
            throw new UnknownClassException(msg);
        }
        return clazz;
    }

    public static InputStream getResourceAsStream(String name) {
        InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);
        if (is == null) {
            is = CLASS_CL_ACCESSOR.getResourceStream(name);
        }
        if (is == null) {
            is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
        }
        return is;
    }

    public static boolean isAvailable(String fullyQualifiedClassName) {
        try {
            forName(fullyQualifiedClassName);
            return true;
        } catch (UnknownClassException e) {
            return false;
        }
    }

    public static Object newInstance(String fqcn) {
        return newInstance(forName(fqcn));
    }

    public static Object newInstance(String fqcn, Object... args) {
        return newInstance(forName(fqcn), args);
    }

    public static <T> T newInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class method parameter cannot be null.");
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new InstantiationException("Unable to instantiate class [" + clazz.getName() + "]", e);
        }
    }

    public static <T> T newInstance(Class<T> cls, Object... objArr) {
        Class[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            clsArr[i] = objArr[i].getClass();
        }
        return (T) instantiate(getConstructor(cls, clsArr), objArr);
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class... argTypes) {
        try {
            return clazz.getConstructor(argTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public static <T> T instantiate(Constructor<T> ctor, Object... args) {
        try {
            return ctor.newInstance(args);
        } catch (Exception e) {
            String msg = "Unable to instantiate instance with constructor [" + ctor + "]";
            throw new InstantiationException(msg, e);
        }
    }

    /* loaded from: jjwt-0.6.0.jar:io/jsonwebtoken/lang/Classes$ExceptionIgnoringAccessor.class */
    private static abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {
        protected abstract ClassLoader doGetClassLoader() throws Throwable;

        private ExceptionIgnoringAccessor() {
        }

        @Override // io.jsonwebtoken.lang.Classes.ClassLoaderAccessor
        public Class loadClass(String fqcn) throws ClassNotFoundException {
            Class clazz = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                try {
                    clazz = cl.loadClass(fqcn);
                } catch (ClassNotFoundException e) {
                }
            }
            return clazz;
        }

        @Override // io.jsonwebtoken.lang.Classes.ClassLoaderAccessor
        public InputStream getResourceStream(String name) {
            InputStream is = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream(name);
            }
            return is;
        }

        protected final ClassLoader getClassLoader() {
            try {
                return doGetClassLoader();
            } catch (Throwable th) {
                return null;
            }
        }
    }
}
