package org.aspectj.apache.bcel.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/NonCachingClassLoaderRepository.class */
public class NonCachingClassLoaderRepository implements Repository {
    private static ClassLoader bootClassLoader = null;
    private final ClassLoaderReference loaderRef;
    private final Map<String, JavaClass> loadedClasses = new SoftHashMap();

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/NonCachingClassLoaderRepository$SoftHashMap.class */
    public static class SoftHashMap extends AbstractMap {
        private Map<Object, SpecialValue> map;
        private ReferenceQueue rq;

        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/NonCachingClassLoaderRepository$SoftHashMap$SpecialValue.class */
        class SpecialValue extends SoftReference {
            private final Object key;

            SpecialValue(Object obj, Object obj2) {
                super(obj2, SoftHashMap.this.rq);
                this.key = obj;
            }
        }

        public SoftHashMap(Map<Object, SpecialValue> map) {
            this.rq = new ReferenceQueue();
            this.map = map;
        }

        public SoftHashMap() {
            this(new HashMap());
        }

        public SoftHashMap(Map<Object, SpecialValue> map, boolean z) {
            this(map);
        }

        private void processQueue() {
            while (true) {
                SpecialValue specialValue = (SpecialValue) this.rq.poll();
                if (specialValue == null) {
                    return;
                } else {
                    this.map.remove(specialValue.key);
                }
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object get(Object obj) {
            SpecialValue specialValue = this.map.get(obj);
            if (specialValue == null) {
                return null;
            }
            if (specialValue.get() != null) {
                return specialValue.get();
            }
            this.map.remove(specialValue.key);
            return null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object put(Object obj, Object obj2) {
            processQueue();
            return this.map.put(obj, new SpecialValue(obj, obj2));
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set entrySet() {
            return this.map.entrySet();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            processQueue();
            Iterator<Object> it = this.map.keySet().iterator();
            while (it.hasNext()) {
                this.map.remove(it.next());
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            processQueue();
            return this.map.size();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object remove(Object obj) {
            processQueue();
            SpecialValue specialValueRemove = this.map.remove(obj);
            if (specialValueRemove == null || specialValueRemove.get() == null) {
                return null;
            }
            return specialValueRemove.get();
        }
    }

    public NonCachingClassLoaderRepository(ClassLoader classLoader) {
        this.loaderRef = new DefaultClassLoaderReference(classLoader != null ? classLoader : getBootClassLoader());
    }

    public NonCachingClassLoaderRepository(ClassLoaderReference classLoaderReference) {
        this.loaderRef = classLoaderReference;
    }

    private static synchronized ClassLoader getBootClassLoader() {
        if (bootClassLoader == null) {
            bootClassLoader = new URLClassLoader(new URL[0]);
        }
        return bootClassLoader;
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void storeClass(JavaClass javaClass) {
        synchronized (this.loadedClasses) {
            this.loadedClasses.put(javaClass.getClassName(), javaClass);
        }
        javaClass.setRepository(this);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void removeClass(JavaClass javaClass) {
        synchronized (this.loadedClasses) {
            this.loadedClasses.remove(javaClass.getClassName());
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass findClass(String str) {
        synchronized (this.loadedClasses) {
            if (!this.loadedClasses.containsKey(str)) {
                return null;
            }
            return this.loadedClasses.get(str);
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void clear() {
        synchronized (this.loadedClasses) {
            this.loadedClasses.clear();
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(String str) throws ClassNotFoundException {
        JavaClass javaClassFindClass = findClass(str);
        if (javaClassFindClass != null) {
            return javaClassFindClass;
        }
        JavaClass javaClassLoadJavaClass = loadJavaClass(str);
        storeClass(javaClassLoadJavaClass);
        return javaClassLoadJavaClass;
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(Class cls) throws ClassNotFoundException {
        return loadClass(cls.getName());
    }

    private JavaClass loadJavaClass(String str) throws ClassNotFoundException {
        try {
            InputStream resourceAsStream = this.loaderRef.getClassLoader().getResourceAsStream(str.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX);
            if (resourceAsStream == null) {
                throw new ClassNotFoundException(str + " not found.");
            }
            return new ClassParser(resourceAsStream, str).parse();
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
    }
}
