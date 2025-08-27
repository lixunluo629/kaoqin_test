package org.apache.ibatis.io;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/VFS.class */
public abstract class VFS {
    private static final Log log = LogFactory.getLog((Class<?>) VFS.class);
    public static final Class<?>[] IMPLEMENTATIONS = {JBoss6VFS.class, DefaultVFS.class};
    public static final List<Class<? extends VFS>> USER_IMPLEMENTATIONS = new ArrayList();

    public abstract boolean isValid();

    protected abstract List<String> list(URL url, String str) throws IOException;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/VFS$VFSHolder.class */
    private static class VFSHolder {
        static final VFS INSTANCE = createVFS();

        private VFSHolder() {
        }

        static VFS createVFS() {
            List<Class<? extends VFS>> impls = new ArrayList<>();
            impls.addAll(VFS.USER_IMPLEMENTATIONS);
            impls.addAll(Arrays.asList(VFS.IMPLEMENTATIONS));
            VFS vfs = null;
            int i = 0;
            while (true) {
                if (vfs != null && vfs.isValid()) {
                    break;
                }
                Class<? extends VFS> impl = impls.get(i);
                try {
                    vfs = (VFS) impl.newInstance();
                    if ((vfs == null || !vfs.isValid()) && VFS.log.isDebugEnabled()) {
                        VFS.log.debug("VFS implementation " + impl.getName() + " is not valid in this environment.");
                    }
                    i++;
                } catch (IllegalAccessException e) {
                    VFS.log.error("Failed to instantiate " + impl, e);
                    return null;
                } catch (InstantiationException e2) {
                    VFS.log.error("Failed to instantiate " + impl, e2);
                    return null;
                }
            }
            if (VFS.log.isDebugEnabled()) {
                VFS.log.debug("Using VFS adapter " + vfs.getClass().getName());
            }
            return vfs;
        }
    }

    public static VFS getInstance() {
        return VFSHolder.INSTANCE;
    }

    public static void addImplClass(Class<? extends VFS> clazz) {
        if (clazz != null) {
            USER_IMPLEMENTATIONS.add(clazz);
        }
    }

    protected static Class<?> getClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            if (log.isDebugEnabled()) {
                log.debug("Class not found: " + className);
                return null;
            }
            return null;
        }
    }

    protected static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error("Method not found " + clazz.getName() + "." + methodName + "." + methodName + ".  Cause: " + e);
            return null;
        } catch (SecurityException e2) {
            log.error("Security exception looking for method " + clazz.getName() + "." + methodName + ".  Cause: " + e2);
            return null;
        }
    }

    protected static <T> T invoke(Method method, Object obj, Object... objArr) throws IOException, RuntimeException {
        try {
            return (T) method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e2) {
            throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            if (e3.getTargetException() instanceof IOException) {
                throw ((IOException) e3.getTargetException());
            }
            throw new RuntimeException(e3);
        }
    }

    protected static List<URL> getResources(String path) throws IOException {
        return Collections.list(Thread.currentThread().getContextClassLoader().getResources(path));
    }

    public List<String> list(String path) throws IOException {
        List<String> names = new ArrayList<>();
        for (URL url : getResources(path)) {
            names.addAll(list(url, path));
        }
        return names;
    }
}
