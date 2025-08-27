package org.apache.catalina.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.xml.ws.WebServiceRef;
import org.apache.catalina.ContainerServlet;
import org.apache.catalina.Globals;
import org.apache.catalina.security.SecurityUtil;
import org.apache.catalina.util.Introspection;
import org.apache.juli.logging.Log;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.collections.ManagedConcurrentWeakHashMap;
import org.apache.tomcat.util.res.StringManager;
import org.springframework.jndi.JndiLocatorSupport;

/* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/DefaultInstanceManager.class */
public class DefaultInstanceManager implements InstanceManager {
    private static final AnnotationCacheEntry[] ANNOTATIONS_EMPTY = new AnnotationCacheEntry[0];
    protected static final StringManager sm = StringManager.getManager(Constants.Package);
    private static final boolean EJB_PRESENT;
    private static final boolean JPA_PRESENT;
    private static final boolean WS_PRESENT;
    private final Context context;
    private final Map<String, Map<String, String>> injectionMap;
    protected final ClassLoader classLoader;
    protected final ClassLoader containerClassLoader;
    protected final boolean privileged;
    protected final boolean ignoreAnnotations;
    private final Set<String> restrictedClasses;
    private final ManagedConcurrentWeakHashMap<Class<?>, AnnotationCacheEntry[]> annotationCache = new ManagedConcurrentWeakHashMap<>();
    private final Map<String, String> postConstructMethods;
    private final Map<String, String> preDestroyMethods;

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/DefaultInstanceManager$AnnotationCacheEntryType.class */
    private enum AnnotationCacheEntryType {
        FIELD,
        SETTER,
        POST_CONSTRUCT,
        PRE_DESTROY
    }

    static {
        Class<?> clazz = null;
        try {
            clazz = Class.forName("javax.ejb.EJB");
        } catch (ClassNotFoundException e) {
        }
        EJB_PRESENT = clazz != null;
        Class<?> clazz2 = null;
        try {
            clazz2 = Class.forName("javax.persistence.PersistenceContext");
        } catch (ClassNotFoundException e2) {
        }
        JPA_PRESENT = clazz2 != null;
        Class<?> clazz3 = null;
        try {
            clazz3 = Class.forName("javax.xml.ws.WebServiceRef");
        } catch (ClassNotFoundException e3) {
        }
        WS_PRESENT = clazz3 != null;
    }

    public DefaultInstanceManager(Context context, Map<String, Map<String, String>> injectionMap, org.apache.catalina.Context catalinaContext, ClassLoader containerClassLoader) throws IOException {
        this.classLoader = catalinaContext.getLoader().getClassLoader();
        this.privileged = catalinaContext.getPrivileged();
        this.containerClassLoader = containerClassLoader;
        this.ignoreAnnotations = catalinaContext.getIgnoreAnnotations();
        Log log = catalinaContext.getLogger();
        Set<String> classNames = new HashSet<>();
        loadProperties(classNames, "org/apache/catalina/core/RestrictedServlets.properties", "defaultInstanceManager.restrictedServletsResource", log);
        loadProperties(classNames, "org/apache/catalina/core/RestrictedListeners.properties", "defaultInstanceManager.restrictedListenersResource", log);
        loadProperties(classNames, "org/apache/catalina/core/RestrictedFilters.properties", "defaultInstanceManager.restrictedFiltersResource", log);
        this.restrictedClasses = Collections.unmodifiableSet(classNames);
        this.context = context;
        this.injectionMap = injectionMap;
        this.postConstructMethods = catalinaContext.findPostConstructMethods();
        this.preDestroyMethods = catalinaContext.findPreDestroyMethods();
    }

    @Override // org.apache.tomcat.InstanceManager
    public Object newInstance(Class<?> clazz) throws IllegalAccessException, NoSuchMethodException, InstantiationException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException {
        return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
    }

    @Override // org.apache.tomcat.InstanceManager
    public Object newInstance(String className) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = loadClassMaybePrivileged(className, this.classLoader);
        return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
    }

    @Override // org.apache.tomcat.InstanceManager
    public Object newInstance(String className, ClassLoader classLoader) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, NamingException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = classLoader.loadClass(className);
        return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
    }

    @Override // org.apache.tomcat.InstanceManager
    public void newInstance(Object o) throws IllegalAccessException, NoSuchMethodException, SecurityException, NamingException, InvocationTargetException {
        newInstance(o, o.getClass());
    }

    private Object newInstance(Object instance, Class<?> clazz) throws IllegalAccessException, NoSuchMethodException, SecurityException, NamingException, InvocationTargetException {
        if (!this.ignoreAnnotations) {
            Map<String, String> injections = assembleInjectionsFromClassHierarchy(clazz);
            populateAnnotationsCache(clazz, injections);
            processAnnotations(instance, injections);
            postConstruct(instance, clazz);
        }
        return instance;
    }

    private Map<String, String> assembleInjectionsFromClassHierarchy(Class<?> clazz) {
        Map<String, String> injections = new HashMap<>();
        while (clazz != null) {
            Map<? extends String, ? extends String> map = this.injectionMap.get(clazz.getName());
            if (map != null) {
                injections.putAll(map);
            }
            clazz = clazz.getSuperclass();
        }
        return injections;
    }

    @Override // org.apache.tomcat.InstanceManager
    public void destroyInstance(Object instance) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        if (!this.ignoreAnnotations) {
            preDestroy(instance, instance.getClass());
        }
    }

    protected void postConstruct(Object instance, Class<?> clazz) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        if (this.context == null) {
            return;
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            postConstruct(instance, superClass);
        }
        AnnotationCacheEntry[] annotations = this.annotationCache.get(clazz);
        for (AnnotationCacheEntry entry : annotations) {
            if (entry.getType() == AnnotationCacheEntryType.POST_CONSTRUCT) {
                Method postConstruct = getMethod(clazz, entry);
                synchronized (postConstruct) {
                    boolean accessibility = postConstruct.isAccessible();
                    postConstruct.setAccessible(true);
                    postConstruct.invoke(instance, new Object[0]);
                    postConstruct.setAccessible(accessibility);
                }
            }
        }
    }

    protected void preDestroy(Object instance, Class<?> clazz) throws IllegalAccessException, NoSuchMethodException, SecurityException, InvocationTargetException {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != Object.class) {
            preDestroy(instance, superClass);
        }
        AnnotationCacheEntry[] annotations = this.annotationCache.get(clazz);
        if (annotations == null) {
            return;
        }
        for (AnnotationCacheEntry entry : annotations) {
            if (entry.getType() == AnnotationCacheEntryType.PRE_DESTROY) {
                Method preDestroy = getMethod(clazz, entry);
                synchronized (preDestroy) {
                    boolean accessibility = preDestroy.isAccessible();
                    preDestroy.setAccessible(true);
                    preDestroy.invoke(instance, new Object[0]);
                    preDestroy.setAccessible(accessibility);
                }
            }
        }
    }

    public void backgroundProcess() {
        this.annotationCache.maintain();
    }

    protected void populateAnnotationsCache(Class<?> clazz, Map<String, String> injections) throws IllegalAccessException, NamingException, InvocationTargetException {
        AnnotationCacheEntry[] annotationsArray;
        Annotation persistenceUnitAnnotation;
        Annotation persistenceContextAnnotation;
        WebServiceRef annotation;
        EJB annotation2;
        Annotation persistenceUnitAnnotation2;
        Annotation persistenceContextAnnotation2;
        WebServiceRef annotation3;
        EJB annotation4;
        List<AnnotationCacheEntry> annotations = null;
        Set<String> injectionsMatchedToSetter = new HashSet<>();
        while (clazz != null) {
            AnnotationCacheEntry[] annotationsArray2 = this.annotationCache.get(clazz);
            if (annotationsArray2 == null) {
                if (annotations == null) {
                    annotations = new ArrayList<>();
                } else {
                    annotations.clear();
                }
                Method[] methods = Introspection.getDeclaredMethods(clazz);
                Method postConstruct = null;
                String postConstructFromXml = this.postConstructMethods.get(clazz.getName());
                Method preDestroy = null;
                String preDestroyFromXml = this.preDestroyMethods.get(clazz.getName());
                for (Method method : methods) {
                    if (this.context != null) {
                        if (injections != null && Introspection.isValidSetter(method)) {
                            String fieldName = Introspection.getPropertyName(method);
                            injectionsMatchedToSetter.add(fieldName);
                            if (injections.containsKey(fieldName)) {
                                annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), injections.get(fieldName), AnnotationCacheEntryType.SETTER));
                            }
                        }
                        Resource resourceAnnotation = (Resource) method.getAnnotation(Resource.class);
                        if (resourceAnnotation != null) {
                            annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), resourceAnnotation.name(), AnnotationCacheEntryType.SETTER));
                        } else if (EJB_PRESENT && (annotation4 = method.getAnnotation(EJB.class)) != null) {
                            annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), annotation4.name(), AnnotationCacheEntryType.SETTER));
                        } else if (WS_PRESENT && (annotation3 = method.getAnnotation(WebServiceRef.class)) != null) {
                            annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), annotation3.name(), AnnotationCacheEntryType.SETTER));
                        } else if (JPA_PRESENT && (persistenceContextAnnotation2 = method.getAnnotation(PersistenceContext.class)) != null) {
                            annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), ((PersistenceContext) persistenceContextAnnotation2).name(), AnnotationCacheEntryType.SETTER));
                        } else if (JPA_PRESENT && (persistenceUnitAnnotation2 = method.getAnnotation(PersistenceUnit.class)) != null) {
                            annotations.add(new AnnotationCacheEntry(method.getName(), method.getParameterTypes(), ((PersistenceUnit) persistenceUnitAnnotation2).name(), AnnotationCacheEntryType.SETTER));
                        }
                        postConstruct = findPostConstruct(postConstruct, postConstructFromXml, method);
                        preDestroy = findPreDestroy(preDestroy, preDestroyFromXml, method);
                    } else {
                        postConstruct = findPostConstruct(postConstruct, postConstructFromXml, method);
                        preDestroy = findPreDestroy(preDestroy, preDestroyFromXml, method);
                    }
                }
                if (postConstruct != null) {
                    annotations.add(new AnnotationCacheEntry(postConstruct.getName(), postConstruct.getParameterTypes(), null, AnnotationCacheEntryType.POST_CONSTRUCT));
                } else if (postConstructFromXml != null) {
                    throw new IllegalArgumentException("Post construct method " + postConstructFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
                }
                if (preDestroy != null) {
                    annotations.add(new AnnotationCacheEntry(preDestroy.getName(), preDestroy.getParameterTypes(), null, AnnotationCacheEntryType.PRE_DESTROY));
                } else if (preDestroyFromXml != null) {
                    throw new IllegalArgumentException("Pre destroy method " + preDestroyFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
                }
                if (this.context != null) {
                    Field[] fields = Introspection.getDeclaredFields(clazz);
                    for (Field field : fields) {
                        String fieldName2 = field.getName();
                        if (injections != null && injections.containsKey(fieldName2) && !injectionsMatchedToSetter.contains(fieldName2)) {
                            annotations.add(new AnnotationCacheEntry(fieldName2, null, injections.get(fieldName2), AnnotationCacheEntryType.FIELD));
                        } else {
                            Resource resourceAnnotation2 = (Resource) field.getAnnotation(Resource.class);
                            if (resourceAnnotation2 != null) {
                                annotations.add(new AnnotationCacheEntry(fieldName2, null, resourceAnnotation2.name(), AnnotationCacheEntryType.FIELD));
                            } else if (EJB_PRESENT && (annotation2 = field.getAnnotation(EJB.class)) != null) {
                                annotations.add(new AnnotationCacheEntry(fieldName2, null, annotation2.name(), AnnotationCacheEntryType.FIELD));
                            } else if (WS_PRESENT && (annotation = field.getAnnotation(WebServiceRef.class)) != null) {
                                annotations.add(new AnnotationCacheEntry(fieldName2, null, annotation.name(), AnnotationCacheEntryType.FIELD));
                            } else if (JPA_PRESENT && (persistenceContextAnnotation = field.getAnnotation(PersistenceContext.class)) != null) {
                                annotations.add(new AnnotationCacheEntry(fieldName2, null, ((PersistenceContext) persistenceContextAnnotation).name(), AnnotationCacheEntryType.FIELD));
                            } else if (JPA_PRESENT && (persistenceUnitAnnotation = field.getAnnotation(PersistenceUnit.class)) != null) {
                                annotations.add(new AnnotationCacheEntry(fieldName2, null, ((PersistenceUnit) persistenceUnitAnnotation).name(), AnnotationCacheEntryType.FIELD));
                            }
                        }
                    }
                }
                if (annotations.isEmpty()) {
                    annotationsArray = ANNOTATIONS_EMPTY;
                } else {
                    annotationsArray = (AnnotationCacheEntry[]) annotations.toArray(new AnnotationCacheEntry[annotations.size()]);
                }
                synchronized (this.annotationCache) {
                    this.annotationCache.put(clazz, annotationsArray);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    protected void processAnnotations(Object instance, Map<String, String> injections) throws IllegalAccessException, NamingException, InvocationTargetException {
        if (this.context == null) {
            return;
        }
        Class<?> superclass = instance.getClass();
        while (true) {
            Class<?> clazz = superclass;
            if (clazz != null) {
                AnnotationCacheEntry[] annotations = this.annotationCache.get(clazz);
                for (AnnotationCacheEntry entry : annotations) {
                    if (entry.getType() == AnnotationCacheEntryType.SETTER) {
                        lookupMethodResource(this.context, instance, getMethod(clazz, entry), entry.getName(), clazz);
                    } else if (entry.getType() == AnnotationCacheEntryType.FIELD) {
                        lookupFieldResource(this.context, instance, getField(clazz, entry), entry.getName(), clazz);
                    }
                }
                superclass = clazz.getSuperclass();
            } else {
                return;
            }
        }
    }

    protected int getAnnotationCacheSize() {
        return this.annotationCache.size();
    }

    protected Class<?> loadClassMaybePrivileged(final String className, final ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> clazz;
        if (SecurityUtil.isPackageProtectionEnabled()) {
            try {
                clazz = (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() { // from class: org.apache.catalina.core.DefaultInstanceManager.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedExceptionAction
                    public Class<?> run() throws Exception {
                        return DefaultInstanceManager.this.loadClass(className, classLoader);
                    }
                });
            } catch (PrivilegedActionException e) {
                Throwable t = e.getCause();
                if (t instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) t);
                }
                throw new RuntimeException(t);
            }
        } else {
            clazz = loadClass(className, classLoader);
        }
        checkAccess(clazz);
        return clazz;
    }

    protected Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (className.startsWith("org.apache.catalina")) {
            return this.containerClassLoader.loadClass(className);
        }
        try {
            Class<?> clazz = this.containerClassLoader.loadClass(className);
            if (ContainerServlet.class.isAssignableFrom(clazz)) {
                return clazz;
            }
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
        }
        return classLoader.loadClass(className);
    }

    private void checkAccess(Class<?> clazz) {
        if (this.privileged) {
            return;
        }
        if (ContainerServlet.class.isAssignableFrom(clazz)) {
            throw new SecurityException(sm.getString("defaultInstanceManager.restrictedContainerServlet", clazz));
        }
        while (clazz != null) {
            if (this.restrictedClasses.contains(clazz.getName())) {
                throw new SecurityException(sm.getString("defaultInstanceManager.restrictedClass", clazz));
            }
            clazz = clazz.getSuperclass();
        }
    }

    protected static void lookupFieldResource(Context context, Object instance, Field field, String name, Class<?> clazz) throws IllegalAccessException, NamingException {
        Object lookedupResource;
        String normalizedName = normalize(name);
        if (normalizedName != null && normalizedName.length() > 0) {
            lookedupResource = context.lookup(normalizedName);
        } else {
            lookedupResource = context.lookup(clazz.getName() + "/" + field.getName());
        }
        synchronized (field) {
            boolean accessibility = field.isAccessible();
            field.setAccessible(true);
            field.set(instance, lookedupResource);
            field.setAccessible(accessibility);
        }
    }

    protected static void lookupMethodResource(Context context, Object instance, Method method, String name, Class<?> clazz) throws IllegalAccessException, NamingException, InvocationTargetException {
        Object lookedupResource;
        if (!Introspection.isValidSetter(method)) {
            throw new IllegalArgumentException(sm.getString("defaultInstanceManager.invalidInjection"));
        }
        String normalizedName = normalize(name);
        if (normalizedName != null && normalizedName.length() > 0) {
            lookedupResource = context.lookup(normalizedName);
        } else {
            lookedupResource = context.lookup(clazz.getName() + "/" + Introspection.getPropertyName(method));
        }
        synchronized (method) {
            boolean accessibility = method.isAccessible();
            method.setAccessible(true);
            method.invoke(instance, lookedupResource);
            method.setAccessible(accessibility);
        }
    }

    private static void loadProperties(Set<String> classNames, String resourceName, String messageKey, Log log) throws IOException {
        Properties properties = new Properties();
        ClassLoader cl = DefaultInstanceManager.class.getClassLoader();
        try {
            try {
                InputStream is = cl.getResourceAsStream(resourceName);
                Throwable th = null;
                if (is == null) {
                    log.error(sm.getString(messageKey, resourceName));
                } else {
                    properties.load(is);
                }
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
            } finally {
            }
        } catch (IOException ioe) {
            log.error(sm.getString(messageKey, resourceName), ioe);
        }
        if (properties.isEmpty()) {
            return;
        }
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            if ("restricted".equals(e.getValue())) {
                classNames.add(e.getKey().toString());
            } else {
                log.warn(sm.getString("defaultInstanceManager.restrictedWrongValue", resourceName, e.getKey(), e.getValue()));
            }
        }
    }

    private static String normalize(String jndiName) {
        if (jndiName != null && jndiName.startsWith(JndiLocatorSupport.CONTAINER_PREFIX)) {
            return jndiName.substring(14);
        }
        return jndiName;
    }

    private static Method getMethod(final Class<?> clazz, final AnnotationCacheEntry entry) throws NoSuchMethodException, SecurityException {
        Method result = null;
        if (Globals.IS_SECURITY_ENABLED) {
            result = (Method) AccessController.doPrivileged(new PrivilegedAction<Method>() { // from class: org.apache.catalina.core.DefaultInstanceManager.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Method run() throws NoSuchMethodException, SecurityException {
                    Method result2 = null;
                    try {
                        result2 = clazz.getDeclaredMethod(entry.getAccessibleObjectName(), entry.getParamTypes());
                    } catch (NoSuchMethodException e) {
                    }
                    return result2;
                }
            });
        } else {
            try {
                result = clazz.getDeclaredMethod(entry.getAccessibleObjectName(), entry.getParamTypes());
            } catch (NoSuchMethodException e) {
            }
        }
        return result;
    }

    private static Field getField(final Class<?> clazz, final AnnotationCacheEntry entry) throws NoSuchFieldException {
        Field result = null;
        if (Globals.IS_SECURITY_ENABLED) {
            result = (Field) AccessController.doPrivileged(new PrivilegedAction<Field>() { // from class: org.apache.catalina.core.DefaultInstanceManager.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Field run() throws NoSuchFieldException {
                    Field result2 = null;
                    try {
                        result2 = clazz.getDeclaredField(entry.getAccessibleObjectName());
                    } catch (NoSuchFieldException e) {
                    }
                    return result2;
                }
            });
        } else {
            try {
                result = clazz.getDeclaredField(entry.getAccessibleObjectName());
            } catch (NoSuchFieldException e) {
            }
        }
        return result;
    }

    private static Method findPostConstruct(Method currentPostConstruct, String postConstructFromXml, Method method) {
        return findLifecycleCallback(currentPostConstruct, postConstructFromXml, method, PostConstruct.class);
    }

    private static Method findPreDestroy(Method currentPreDestroy, String preDestroyFromXml, Method method) {
        return findLifecycleCallback(currentPreDestroy, preDestroyFromXml, method, PreDestroy.class);
    }

    private static Method findLifecycleCallback(Method currentMethod, String methodNameFromXml, Method method, Class<? extends Annotation> annotation) {
        Method result = currentMethod;
        if (methodNameFromXml != null) {
            if (method.getName().equals(methodNameFromXml)) {
                if (!Introspection.isValidLifecycleCallback(method)) {
                    throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
                }
                result = method;
            }
        } else if (method.isAnnotationPresent(annotation)) {
            if (currentMethod != null || !Introspection.isValidLifecycleCallback(method)) {
                throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
            }
            result = method;
        }
        return result;
    }

    /* loaded from: tomcat-embed-core-8.5.43.jar:org/apache/catalina/core/DefaultInstanceManager$AnnotationCacheEntry.class */
    private static final class AnnotationCacheEntry {
        private final String accessibleObjectName;
        private final Class<?>[] paramTypes;
        private final String name;
        private final AnnotationCacheEntryType type;

        public AnnotationCacheEntry(String accessibleObjectName, Class<?>[] paramTypes, String name, AnnotationCacheEntryType type) {
            this.accessibleObjectName = accessibleObjectName;
            this.paramTypes = paramTypes;
            this.name = name;
            this.type = type;
        }

        public String getAccessibleObjectName() {
            return this.accessibleObjectName;
        }

        public Class<?>[] getParamTypes() {
            return this.paramTypes;
        }

        public String getName() {
            return this.name;
        }

        public AnnotationCacheEntryType getType() {
            return this.type;
        }
    }
}
