package org.apache.ibatis.io;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.util.ClassUtils;

/* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/ResolverUtil.class */
public class ResolverUtil<T> {
    private static final Log log = LogFactory.getLog((Class<?>) ResolverUtil.class);
    private Set<Class<? extends T>> matches = new HashSet();
    private ClassLoader classloader;

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/ResolverUtil$Test.class */
    public interface Test {
        boolean matches(Class<?> cls);
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/ResolverUtil$IsA.class */
    public static class IsA implements Test {
        private Class<?> parent;

        public IsA(Class<?> parentType) {
            this.parent = parentType;
        }

        @Override // org.apache.ibatis.io.ResolverUtil.Test
        public boolean matches(Class<?> type) {
            return type != null && this.parent.isAssignableFrom(type);
        }

        public String toString() {
            return "is assignable to " + this.parent.getSimpleName();
        }
    }

    /* loaded from: mybatis-3.4.6.jar:org/apache/ibatis/io/ResolverUtil$AnnotatedWith.class */
    public static class AnnotatedWith implements Test {
        private Class<? extends Annotation> annotation;

        public AnnotatedWith(Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }

        @Override // org.apache.ibatis.io.ResolverUtil.Test
        public boolean matches(Class<?> type) {
            return type != null && type.isAnnotationPresent(this.annotation);
        }

        public String toString() {
            return "annotated with @" + this.annotation.getSimpleName();
        }
    }

    public Set<Class<? extends T>> getClasses() {
        return this.matches;
    }

    public ClassLoader getClassLoader() {
        return this.classloader == null ? Thread.currentThread().getContextClassLoader() : this.classloader;
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public ResolverUtil<T> findImplementations(Class<?> parent, String... packageNames) {
        if (packageNames == null) {
            return this;
        }
        Test test = new IsA(parent);
        for (String pkg : packageNames) {
            find(test, pkg);
        }
        return this;
    }

    public ResolverUtil<T> findAnnotated(Class<? extends Annotation> annotation, String... packageNames) {
        if (packageNames == null) {
            return this;
        }
        Test test = new AnnotatedWith(annotation);
        for (String pkg : packageNames) {
            find(test, pkg);
        }
        return this;
    }

    public ResolverUtil<T> find(Test test, String packageName) {
        String path = getPackagePath(packageName);
        try {
            List<String> children = VFS.getInstance().list(path);
            for (String child : children) {
                if (child.endsWith(ClassUtils.CLASS_FILE_SUFFIX)) {
                    addIfMatching(test, child);
                }
            }
        } catch (IOException ioe) {
            log.error("Could not read package: " + packageName, ioe);
        }
        return this;
    }

    protected String getPackagePath(String packageName) {
        if (packageName == null) {
            return null;
        }
        return packageName.replace('.', '/');
    }

    protected void addIfMatching(Test test, String str) {
        try {
            String strReplace = str.substring(0, str.indexOf(46)).replace('/', '.');
            ClassLoader classLoader = getClassLoader();
            if (log.isDebugEnabled()) {
                log.debug("Checking to see if class " + strReplace + " matches criteria [" + test + "]");
            }
            Class<?> clsLoadClass = classLoader.loadClass(strReplace);
            if (test.matches(clsLoadClass)) {
                this.matches.add(clsLoadClass);
            }
        } catch (Throwable th) {
            log.warn("Could not examine class '" + str + "' due to a " + th.getClass().getName() + " with message: " + th.getMessage());
        }
    }
}
