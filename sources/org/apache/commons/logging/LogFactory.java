package org.apache.commons.logging;

import java.security.PrivilegedAction;
import java.util.Hashtable;
import org.apache.commons.logging.impl.SLF4JLogFactory;

/* JADX WARN: Classes with same name are omitted:
  commons-logging-1.0.4.jar:org/apache/commons/logging/LogFactory.class
 */
/* loaded from: jcl-over-slf4j-1.7.26.jar:org/apache/commons/logging/LogFactory.class */
public abstract class LogFactory {
    public static final String PRIORITY_KEY = "priority";
    public static final String TCCL_KEY = "use_tccl";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.SLF4JLogFactory";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    static String UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J = "http://www.slf4j.org/codes.html#unsupported_operation_in_jcl_over_slf4j";
    static LogFactory logFactory = new SLF4JLogFactory();
    protected static Hashtable factories = null;
    protected static LogFactory nullClassLoaderFactory = null;

    public abstract Object getAttribute(String str);

    public abstract String[] getAttributeNames();

    public abstract Log getInstance(Class cls) throws LogConfigurationException;

    public abstract Log getInstance(String str) throws LogConfigurationException;

    public abstract void release();

    public abstract void removeAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    protected LogFactory() {
    }

    /* renamed from: org.apache.commons.logging.LogFactory$1, reason: invalid class name */
    /* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/LogFactory$1.class */
    static class AnonymousClass1 implements PrivilegedAction {
        AnonymousClass1() {
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            return LogFactory.getContextClassLoader();
        }
    }

    public static LogFactory getFactory() throws LogConfigurationException {
        return logFactory;
    }

    public static Log getLog(Class clazz) throws LogConfigurationException {
        return getFactory().getInstance(clazz);
    }

    public static Log getLog(String name) throws LogConfigurationException {
        return getFactory().getInstance(name);
    }

    public static void release(ClassLoader classLoader) {
    }

    public static void releaseAll() {
    }

    public static String objectId(Object o) {
        if (o == null) {
            return "null";
        }
        return o.getClass().getName() + "@" + System.identityHashCode(o);
    }

    protected static Object createFactory(String factoryClass, ClassLoader classLoader) {
        throw new UnsupportedOperationException("Operation [factoryClass] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static ClassLoader directGetContextClassLoader() {
        throw new UnsupportedOperationException("Operation [directGetContextClassLoader] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        throw new UnsupportedOperationException("Operation [getContextClassLoader] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static ClassLoader getClassLoader(Class clazz) {
        throw new UnsupportedOperationException("Operation [getClassLoader] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static boolean isDiagnosticsEnabled() {
        throw new UnsupportedOperationException("Operation [isDiagnosticsEnabled] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static void logRawDiagnostic(String msg) {
        throw new UnsupportedOperationException("Operation [logRawDiagnostic] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader, ClassLoader contextClassLoader) {
        throw new UnsupportedOperationException("Operation [logRawDiagnostic] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    protected static LogFactory newFactory(String factoryClass, ClassLoader classLoader) {
        throw new UnsupportedOperationException("Operation [newFactory] is not supported in jcl-over-slf4j. See also " + UNSUPPORTED_OPERATION_IN_JCL_OVER_SLF4J);
    }

    /* renamed from: org.apache.commons.logging.LogFactory$2, reason: invalid class name */
    /* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/LogFactory$2.class */
    static class AnonymousClass2 implements PrivilegedAction {
        static Class class$org$apache$commons$logging$LogFactory;
        private final ClassLoader val$classLoader;
        private final String val$factoryClass;

        AnonymousClass2(ClassLoader val$classLoader, String val$factoryClass) {
            this.val$classLoader = val$classLoader;
            this.val$factoryClass = val$factoryClass;
        }

        @Override // java.security.PrivilegedAction
        public Object run() throws ClassNotFoundException {
            Class clsClass$;
            Class clsClass$2;
            Class clsClass$3;
            Class clsClass$4;
            Class logFactoryClass = null;
            try {
                if (this.val$classLoader != null) {
                    try {
                        try {
                            logFactoryClass = this.val$classLoader.loadClass(this.val$factoryClass);
                            return (LogFactory) logFactoryClass.newInstance();
                        } catch (ClassCastException e) {
                            ClassLoader classLoader = this.val$classLoader;
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                clsClass$3 = class$(LogFactory.FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = clsClass$3;
                            } else {
                                clsClass$3 = class$org$apache$commons$logging$LogFactory;
                            }
                            if (classLoader == clsClass$3.getClassLoader()) {
                                throw e;
                            }
                        } catch (NoClassDefFoundError e2) {
                            ClassLoader classLoader2 = this.val$classLoader;
                            if (class$org$apache$commons$logging$LogFactory == null) {
                                clsClass$2 = class$(LogFactory.FACTORY_PROPERTY);
                                class$org$apache$commons$logging$LogFactory = clsClass$2;
                            } else {
                                clsClass$2 = class$org$apache$commons$logging$LogFactory;
                            }
                            if (classLoader2 == clsClass$2.getClassLoader()) {
                                throw e2;
                            }
                        }
                    } catch (ClassNotFoundException ex) {
                        ClassLoader classLoader3 = this.val$classLoader;
                        if (class$org$apache$commons$logging$LogFactory == null) {
                            clsClass$4 = class$(LogFactory.FACTORY_PROPERTY);
                            class$org$apache$commons$logging$LogFactory = clsClass$4;
                        } else {
                            clsClass$4 = class$org$apache$commons$logging$LogFactory;
                        }
                        if (classLoader3 == clsClass$4.getClassLoader()) {
                            throw ex;
                        }
                    }
                }
                return (LogFactory) Class.forName(this.val$factoryClass).newInstance();
            } catch (Exception e3) {
                if (logFactoryClass != null) {
                    if (class$org$apache$commons$logging$LogFactory == null) {
                        clsClass$ = class$(LogFactory.FACTORY_PROPERTY);
                        class$org$apache$commons$logging$LogFactory = clsClass$;
                    } else {
                        clsClass$ = class$org$apache$commons$logging$LogFactory;
                    }
                    if (!clsClass$.isAssignableFrom(logFactoryClass)) {
                        return new LogConfigurationException("The chosen LogFactory implementation does not extend LogFactory. Please check your configuration.", e3);
                    }
                }
                return new LogConfigurationException(e3);
            }
        }

        static Class class$(String x0) {
            try {
                return Class.forName(x0);
            } catch (ClassNotFoundException x1) {
                throw new NoClassDefFoundError(x1.getMessage());
            }
        }
    }

    /* renamed from: org.apache.commons.logging.LogFactory$3, reason: invalid class name */
    /* loaded from: commons-logging-1.0.4.jar:org/apache/commons/logging/LogFactory$3.class */
    static class AnonymousClass3 implements PrivilegedAction {
        private final ClassLoader val$loader;
        private final String val$name;

        AnonymousClass3(ClassLoader val$loader, String val$name) {
            this.val$loader = val$loader;
            this.val$name = val$name;
        }

        @Override // java.security.PrivilegedAction
        public Object run() {
            if (this.val$loader != null) {
                return this.val$loader.getResourceAsStream(this.val$name);
            }
            return ClassLoader.getSystemResourceAsStream(this.val$name);
        }
    }
}
