package javax.el;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ExpressionFactory.class */
public abstract class ExpressionFactory {
    private static final boolean IS_SECURITY_ENABLED;
    private static final String SERVICE_RESOURCE_NAME = "META-INF/services/javax.el.ExpressionFactory";
    private static final String PROPERTY_NAME = "javax.el.ExpressionFactory";
    private static final String PROPERTY_FILE;
    private static final CacheValue nullTcclFactory;
    private static final ConcurrentMap<CacheKey, CacheValue> factoryCache;

    public abstract ValueExpression createValueExpression(ELContext eLContext, String str, Class<?> cls);

    public abstract ValueExpression createValueExpression(Object obj, Class<?> cls);

    public abstract MethodExpression createMethodExpression(ELContext eLContext, String str, Class<?> cls, Class<?>[] clsArr);

    public abstract Object coerceToType(Object obj, Class<?> cls);

    static {
        IS_SECURITY_ENABLED = System.getSecurityManager() != null;
        nullTcclFactory = new CacheValue();
        factoryCache = new ConcurrentHashMap();
        if (IS_SECURITY_ENABLED) {
            PROPERTY_FILE = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.el.ExpressionFactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public String run() {
                    return System.getProperty("java.home") + File.separator + "lib" + File.separator + "el.properties";
                }
            });
        } else {
            PROPERTY_FILE = System.getProperty("java.home") + File.separator + "lib" + File.separator + "el.properties";
        }
    }

    public static ExpressionFactory newInstance() {
        return newInstance(null);
    }

    public static ExpressionFactory newInstance(Properties properties) throws NoSuchMethodException, SecurityException {
        CacheValue cacheValue;
        ExpressionFactory result;
        ClassLoader tccl = Util.getContextClassLoader();
        if (tccl == null) {
            cacheValue = nullTcclFactory;
        } else {
            CacheKey key = new CacheKey(tccl);
            cacheValue = factoryCache.get(key);
            if (cacheValue == null) {
                CacheValue newCacheValue = new CacheValue();
                cacheValue = factoryCache.putIfAbsent(key, newCacheValue);
                if (cacheValue == null) {
                    cacheValue = newCacheValue;
                }
            }
        }
        Lock readLock = cacheValue.getLock().readLock();
        readLock.lock();
        try {
            Class<?> clazz = cacheValue.getFactoryClass();
            readLock.unlock();
            if (clazz == null) {
                String className = null;
                try {
                    Lock writeLock = cacheValue.getLock().writeLock();
                    writeLock.lock();
                    try {
                        className = cacheValue.getFactoryClassName();
                        if (className == null) {
                            className = discoverClassName(tccl);
                            cacheValue.setFactoryClassName(className);
                        }
                        if (tccl == null) {
                            clazz = Class.forName(className);
                        } else {
                            clazz = tccl.loadClass(className);
                        }
                        cacheValue.setFactoryClass(clazz);
                        writeLock.unlock();
                    } catch (Throwable th) {
                        writeLock.unlock();
                        throw th;
                    }
                } catch (ClassNotFoundException e) {
                    throw new ELException(Util.message(null, "expressionFactory.cannotFind", className), e);
                }
            }
            Constructor<?> constructor = null;
            try {
                if (properties != null) {
                    try {
                        constructor = clazz.getConstructor(Properties.class);
                    } catch (NoSuchMethodException e2) {
                    } catch (SecurityException se) {
                        throw new ELException(se);
                    }
                }
                if (constructor == null) {
                    result = (ExpressionFactory) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                } else {
                    result = (ExpressionFactory) constructor.newInstance(properties);
                }
                return result;
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException e3) {
                throw new ELException(Util.message(null, "expressionFactory.cannotCreate", clazz.getName()), e3);
            } catch (InvocationTargetException e4) {
                Throwable cause = e4.getCause();
                Util.handleThrowable(cause);
                throw new ELException(Util.message(null, "expressionFactory.cannotCreate", clazz.getName()), e4);
            }
        } catch (Throwable th2) {
            readLock.unlock();
            throw th2;
        }
    }

    public ELResolver getStreamELResolver() {
        return null;
    }

    public Map<String, Method> getInitFunctionMap() {
        return null;
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ExpressionFactory$CacheKey.class */
    private static class CacheKey {
        private final int hash;
        private final WeakReference<ClassLoader> ref;

        public CacheKey(ClassLoader cl) {
            this.hash = cl.hashCode();
            this.ref = new WeakReference<>(cl);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            ClassLoader thisCl;
            if (obj == this) {
                return true;
            }
            return (obj instanceof CacheKey) && (thisCl = this.ref.get()) != null && thisCl == ((CacheKey) obj).ref.get();
        }
    }

    /* loaded from: tomcat-embed-el-8.5.43.jar:javax/el/ExpressionFactory$CacheValue.class */
    private static class CacheValue {
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private String className;
        private WeakReference<Class<?>> ref;

        public ReadWriteLock getLock() {
            return this.lock;
        }

        public String getFactoryClassName() {
            return this.className;
        }

        public void setFactoryClassName(String className) {
            this.className = className;
        }

        public Class<?> getFactoryClass() {
            if (this.ref != null) {
                return this.ref.get();
            }
            return null;
        }

        public void setFactoryClass(Class<?> clazz) {
            this.ref = new WeakReference<>(clazz);
        }
    }

    private static String discoverClassName(ClassLoader tccl) throws IOException {
        String className = getClassNameServices(tccl);
        if (className == null) {
            if (IS_SECURITY_ENABLED) {
                className = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.el.ExpressionFactory.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return ExpressionFactory.getClassNameJreDir();
                    }
                });
            } else {
                className = getClassNameJreDir();
            }
        }
        if (className == null) {
            if (IS_SECURITY_ENABLED) {
                className = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.el.ExpressionFactory.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public String run() {
                        return ExpressionFactory.getClassNameSysProp();
                    }
                });
            } else {
                className = getClassNameSysProp();
            }
        }
        if (className == null) {
            className = "org.apache.el.ExpressionFactoryImpl";
        }
        return className;
    }

    /* JADX WARN: Failed to apply debug info
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.applyWithWiderIgnoreUnknown(TypeUpdate.java:74)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:137)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:133)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.searchAndApplyVarDebugInfo(DebugInfoApplyVisitor.java:75)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.lambda$applyDebugInfo$0(DebugInfoApplyVisitor.java:68)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.applyDebugInfo(DebugInfoApplyVisitor.java:68)
    	at jadx.core.dex.visitors.debuginfo.DebugInfoApplyVisitor.visit(DebugInfoApplyVisitor.java:55)
     */
    /* JADX WARN: Failed to calculate best type for var: r12v3 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:156)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:133)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:238)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Failed to calculate best type for var: r12v3 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:145)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:123)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:101)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:101)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.calculateFromBounds(FixTypesVisitor.java:156)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.setBestType(FixTypesVisitor.java:133)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.deduceType(FixTypesVisitor.java:238)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryDeduceTypes(FixTypesVisitor.java:221)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Failed to calculate best type for var: r13v0 ??
    java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.InsnArg.getType()" because "changeArg" is null
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.moveListener(TypeUpdate.java:439)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.runListeners(TypeUpdate.java:232)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.requestUpdate(TypeUpdate.java:212)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeForSsaVar(TypeUpdate.java:183)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.updateTypeChecked(TypeUpdate.java:112)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:83)
    	at jadx.core.dex.visitors.typeinference.TypeUpdate.apply(TypeUpdate.java:56)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.calculateFromBounds(TypeInferenceVisitor.java:145)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.setBestType(TypeInferenceVisitor.java:123)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.lambda$runTypePropagation$1(TypeInferenceVisitor.java:101)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runTypePropagation(TypeInferenceVisitor.java:101)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:75)
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException: Cannot invoke "jadx.core.dex.instructions.args.RegisterArg.getSVar()" because the return value of "jadx.core.dex.nodes.InsnNode.getResult()" is null
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.collectRelatedVars(AbstractTypeConstraint.java:31)
    	at jadx.core.dex.visitors.typeinference.AbstractTypeConstraint.<init>(AbstractTypeConstraint.java:19)
    	at jadx.core.dex.visitors.typeinference.TypeSearch$1.<init>(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeMoveConstraint(TypeSearch.java:376)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.makeConstraint(TypeSearch.java:361)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.collectConstraints(TypeSearch.java:341)
    	at java.base/java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:60)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.runMultiVariableSearch(FixTypesVisitor.java:116)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:91)
     */
    /* JADX WARN: Not initialized variable reg: 12, insn: 0x0125: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r12 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY] A[D('isr' java.io.InputStreamReader)]) A[TRY_LEAVE], block:B:70:0x0125 */
    /* JADX WARN: Not initialized variable reg: 13, insn: 0x0129: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r13 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:72:0x0129 */
    /* JADX WARN: Type inference failed for: r12v3, names: [isr], types: [java.io.InputStreamReader] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.Throwable] */
    private static String getClassNameServices(ClassLoader tccl) throws IOException {
        ?? r12;
        ?? r13;
        InputStream is = tccl == null ? ClassLoader.getSystemResourceAsStream(SERVICE_RESOURCE_NAME) : tccl.getResourceAsStream(SERVICE_RESOURCE_NAME);
        if (is == null) {
            return null;
        }
        try {
            try {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(is, "UTF-8");
                    Throwable th = null;
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    Throwable th2 = null;
                    try {
                        try {
                            String line = bufferedReader.readLine();
                            if (line != null && line.trim().length() > 0) {
                                String strTrim = line.trim();
                                if (bufferedReader != null) {
                                    if (0 != 0) {
                                        try {
                                            bufferedReader.close();
                                        } catch (Throwable th3) {
                                            th2.addSuppressed(th3);
                                        }
                                    } else {
                                        bufferedReader.close();
                                    }
                                }
                                if (inputStreamReader != null) {
                                    if (0 != 0) {
                                        try {
                                            inputStreamReader.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        inputStreamReader.close();
                                    }
                                }
                                return strTrim;
                            }
                            if (bufferedReader != null) {
                                if (0 != 0) {
                                    try {
                                        bufferedReader.close();
                                    } catch (Throwable th5) {
                                        th2.addSuppressed(th5);
                                    }
                                } else {
                                    bufferedReader.close();
                                }
                            }
                            if (inputStreamReader != null) {
                                if (0 != 0) {
                                    try {
                                        inputStreamReader.close();
                                    } catch (Throwable th6) {
                                        th.addSuppressed(th6);
                                    }
                                } else {
                                    inputStreamReader.close();
                                }
                            }
                            try {
                                is.close();
                                return null;
                            } catch (IOException e) {
                                return null;
                            }
                        } catch (Throwable th7) {
                            if (bufferedReader != null) {
                                if (th2 != null) {
                                    try {
                                        bufferedReader.close();
                                    } catch (Throwable th8) {
                                        th2.addSuppressed(th8);
                                    }
                                } else {
                                    bufferedReader.close();
                                }
                            }
                            throw th7;
                        }
                    } catch (Throwable th9) {
                        th2 = th9;
                        throw th9;
                    }
                } catch (UnsupportedEncodingException e2) {
                    try {
                        is.close();
                        return null;
                    } catch (IOException e3) {
                        return null;
                    }
                } catch (IOException e4) {
                    throw new ELException(Util.message(null, "expressionFactory.readFailed", SERVICE_RESOURCE_NAME), e4);
                }
            } catch (Throwable th10) {
                if (r12 != 0) {
                    if (r13 != 0) {
                        try {
                            r12.close();
                        } catch (Throwable th11) {
                            r13.addSuppressed(th11);
                        }
                    } else {
                        r12.close();
                    }
                }
                throw th10;
            }
        } finally {
            try {
                is.close();
            } catch (IOException e5) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Finally extract failed */
    public static String getClassNameJreDir() throws IOException {
        File file = new File(PROPERTY_FILE);
        if (file.canRead()) {
            try {
                InputStream is = new FileInputStream(file);
                Throwable th = null;
                try {
                    Properties props = new Properties();
                    props.load(is);
                    String value = props.getProperty(PROPERTY_NAME);
                    if (value != null && value.trim().length() > 0) {
                        String strTrim = value.trim();
                        if (is != null) {
                            if (0 != 0) {
                                try {
                                    is.close();
                                } catch (Throwable x2) {
                                    th.addSuppressed(x2);
                                }
                            } else {
                                is.close();
                            }
                        }
                        return strTrim;
                    }
                    if (is != null) {
                        if (0 != 0) {
                            try {
                                is.close();
                            } catch (Throwable x22) {
                                th.addSuppressed(x22);
                            }
                        } else {
                            is.close();
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    if (is != null) {
                        if (0 != 0) {
                            try {
                                is.close();
                            } catch (Throwable x23) {
                                th.addSuppressed(x23);
                            }
                        } else {
                            is.close();
                        }
                    }
                    throw th2;
                }
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e2) {
                throw new ELException(Util.message(null, "expressionFactory.readFailed", PROPERTY_FILE), e2);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final String getClassNameSysProp() {
        String value = System.getProperty(PROPERTY_NAME);
        if (value != null && value.trim().length() > 0) {
            return value.trim();
        }
        return null;
    }
}
