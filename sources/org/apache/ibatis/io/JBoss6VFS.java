package org.apache.ibatis.io;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/JBoss6VFS.class */
public class JBoss6VFS extends org.apache.ibatis.io.VFS {
    private static final Log log = LogFactory.getLog((Class<?>) JBoss6VFS.class);
    private static Boolean valid;

    static {
        initialize();
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/JBoss6VFS$VirtualFile.class */
    static class VirtualFile {
        static Class<?> VirtualFile;
        static Method getPathNameRelativeTo;
        static Method getChildrenRecursively;
        Object virtualFile;

        VirtualFile(Object virtualFile) {
            this.virtualFile = virtualFile;
        }

        String getPathNameRelativeTo(VirtualFile parent) {
            try {
                return (String) org.apache.ibatis.io.VFS.invoke(getPathNameRelativeTo, this.virtualFile, parent.virtualFile);
            } catch (IOException e) {
                JBoss6VFS.log.error("This should not be possible. VirtualFile.getPathNameRelativeTo() threw IOException.");
                return null;
            }
        }

        List<VirtualFile> getChildren() throws IOException {
            List<?> objects = (List) org.apache.ibatis.io.VFS.invoke(getChildrenRecursively, this.virtualFile, new Object[0]);
            List<VirtualFile> children = new ArrayList<>(objects.size());
            for (Object object : objects) {
                children.add(new VirtualFile(object));
            }
            return children;
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/JBoss6VFS$VFS.class */
    static class VFS {
        static Class<?> VFS;
        static Method getChild;

        private VFS() {
        }

        static VirtualFile getChild(URL url) throws IOException, RuntimeException {
            Object o = org.apache.ibatis.io.VFS.invoke(getChild, VFS, url);
            if (o == null) {
                return null;
            }
            return new VirtualFile(o);
        }
    }

    protected static synchronized void initialize() {
        if (valid == null) {
            valid = Boolean.TRUE;
            VFS.VFS = (Class) checkNotNull(getClass("org.jboss.vfs.VFS"));
            VirtualFile.VirtualFile = (Class) checkNotNull(getClass("org.jboss.vfs.VirtualFile"));
            VFS.getChild = (Method) checkNotNull(getMethod(VFS.VFS, "getChild", URL.class));
            VirtualFile.getChildrenRecursively = (Method) checkNotNull(getMethod(VirtualFile.VirtualFile, "getChildrenRecursively", new Class[0]));
            VirtualFile.getPathNameRelativeTo = (Method) checkNotNull(getMethod(VirtualFile.VirtualFile, "getPathNameRelativeTo", VirtualFile.VirtualFile));
            checkReturnType(VFS.getChild, VirtualFile.VirtualFile);
            checkReturnType(VirtualFile.getChildrenRecursively, List.class);
            checkReturnType(VirtualFile.getPathNameRelativeTo, String.class);
        }
    }

    protected static <T> T checkNotNull(T object) {
        if (object == null) {
            setInvalid();
        }
        return object;
    }

    protected static void checkReturnType(Method method, Class<?> expected) {
        if (method != null && !expected.isAssignableFrom(method.getReturnType())) {
            log.error("Method " + method.getClass().getName() + "." + method.getName() + "(..) should return " + expected.getName() + " but returns " + method.getReturnType().getName() + " instead.");
            setInvalid();
        }
    }

    protected static void setInvalid() {
        if (valid == Boolean.TRUE) {
            log.debug("JBoss 6 VFS API is not available in this environment.");
            valid = Boolean.FALSE;
        }
    }

    @Override // org.apache.ibatis.io.VFS
    public boolean isValid() {
        return valid.booleanValue();
    }

    @Override // org.apache.ibatis.io.VFS
    public List<String> list(URL url, String path) throws IOException, RuntimeException {
        VirtualFile directory = VFS.getChild(url);
        if (directory == null) {
            return Collections.emptyList();
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        List<VirtualFile> children = directory.getChildren();
        List<String> names = new ArrayList<>(children.size());
        for (VirtualFile vf : children) {
            names.add(path + vf.getPathNameRelativeTo(directory));
        }
        return names;
    }
}
