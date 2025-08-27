package org.aspectj.apache.bcel.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.aspectj.apache.bcel.classfile.ClassFormatException;
import org.aspectj.apache.bcel.classfile.ClassParser;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.springframework.util.ClassUtils;

/* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassLoaderRepository.class */
public class ClassLoaderRepository implements Repository {
    private ClassLoaderReference loaderRef;
    private WeakHashMap<URL, SoftReference<JavaClass>> localCache = new WeakHashMap<>();
    private SoftHashMap nameMap = new SoftHashMap(new HashMap(), false);
    private long timeManipulatingURLs = 0;
    private long timeSpentLoading = 0;
    private int classesLoadedCount = 0;
    private int misses = 0;
    private int cacheHitsLocal = 0;
    private int missLocalEvicted = 0;
    private static ClassLoader bootClassLoader = null;
    private static SoftHashMap sharedCache = new SoftHashMap(Collections.synchronizedMap(new HashMap()));
    public static boolean useSharedCache = System.getProperty("org.aspectj.apache.bcel.useSharedCache", "true").equalsIgnoreCase("true");
    private static int cacheHitsShared = 0;
    private static int missSharedEvicted = 0;

    /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassLoaderRepository$SoftHashMap.class */
    public static class SoftHashMap extends AbstractMap {
        private Map<Object, SpecialValue> map;
        boolean recordMiss;
        private ReferenceQueue rq;

        /* loaded from: aspectjweaver-1.8.14.jar:org/aspectj/apache/bcel/util/ClassLoaderRepository$SoftHashMap$SpecialValue.class */
        class SpecialValue extends SoftReference {
            private final Object key;

            SpecialValue(Object obj, Object obj2) {
                super(obj2, SoftHashMap.this.rq);
                this.key = obj;
            }
        }

        public SoftHashMap(Map<Object, SpecialValue> map) {
            this.recordMiss = true;
            this.rq = new ReferenceQueue();
            this.map = map;
        }

        public SoftHashMap() {
            this(new HashMap());
        }

        public SoftHashMap(Map map, boolean z) {
            this(map);
            this.recordMiss = z;
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
            if (!this.recordMiss) {
                return null;
            }
            ClassLoaderRepository.access$208();
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
            this.map.clear();
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

    public ClassLoaderRepository(ClassLoader classLoader) {
        this.loaderRef = new DefaultClassLoaderReference(classLoader != null ? classLoader : getBootClassLoader());
    }

    public ClassLoaderRepository(ClassLoaderReference classLoaderReference) {
        this.loaderRef = classLoaderReference;
    }

    private static synchronized ClassLoader getBootClassLoader() {
        if (bootClassLoader == null) {
            bootClassLoader = new URLClassLoader(new URL[0]);
        }
        return bootClassLoader;
    }

    private void storeClassAsReference(URL url, JavaClass javaClass) {
        if (useSharedCache) {
            javaClass.setRepository(null);
            sharedCache.put(url, javaClass);
        } else {
            javaClass.setRepository(this);
            this.localCache.put(url, new SoftReference<>(javaClass));
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void storeClass(JavaClass javaClass) {
        storeClassAsReference(toURL(javaClass.getClassName()), javaClass);
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void removeClass(JavaClass javaClass) {
        if (useSharedCache) {
            sharedCache.remove(toURL(javaClass.getClassName()));
        } else {
            this.localCache.remove(toURL(javaClass.getClassName()));
        }
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass findClass(String str) {
        return useSharedCache ? findClassShared(toURL(str)) : findClassLocal(toURL(str));
    }

    private JavaClass findClassLocal(URL url) {
        SoftReference<JavaClass> softReference = this.localCache.get(url);
        if (softReference == null) {
            return null;
        }
        JavaClass javaClass = softReference.get();
        if (javaClass != null) {
            return javaClass;
        }
        this.missLocalEvicted++;
        return null;
    }

    private JavaClass findClassShared(URL url) {
        return (JavaClass) sharedCache.get(url);
    }

    private URL toURL(String str) {
        URL resource = (URL) this.nameMap.get(str);
        if (resource == null) {
            resource = this.loaderRef.getClassLoader().getResource(str.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX);
            this.nameMap.put(str, resource);
        }
        return resource;
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(String str) throws ClassNotFoundException, ClassFormatException {
        long jCurrentTimeMillis = System.currentTimeMillis();
        URL url = toURL(str);
        this.timeManipulatingURLs += System.currentTimeMillis() - jCurrentTimeMillis;
        if (url == null) {
            throw new ClassNotFoundException(str + " not found - unable to determine URL");
        }
        if (useSharedCache) {
            JavaClass javaClassFindClassShared = findClassShared(url);
            if (javaClassFindClassShared != null) {
                cacheHitsShared++;
                return javaClassFindClassShared;
            }
        } else {
            JavaClass javaClassFindClassLocal = findClassLocal(url);
            if (javaClassFindClassLocal != null) {
                this.cacheHitsLocal++;
                return javaClassFindClassLocal;
            }
        }
        this.misses++;
        try {
            InputStream inputStreamOpenStream = useSharedCache ? url.openStream() : this.loaderRef.getClassLoader().getResourceAsStream(str.replace('.', '/') + ClassUtils.CLASS_FILE_SUFFIX);
            if (inputStreamOpenStream == null) {
                throw new ClassNotFoundException(str + " not found using url " + url);
            }
            JavaClass javaClass = new ClassParser(inputStreamOpenStream, str).parse();
            storeClassAsReference(url, javaClass);
            this.timeSpentLoading += System.currentTimeMillis() - jCurrentTimeMillis;
            this.classesLoadedCount++;
            return javaClass;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
    }

    public String report() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("BCEL repository report.");
        if (useSharedCache) {
            stringBuffer.append(" (shared cache)");
        } else {
            stringBuffer.append(" (local cache)");
        }
        stringBuffer.append(" Total time spent loading: " + this.timeSpentLoading + "ms.");
        stringBuffer.append(" Time spent manipulating URLs: " + this.timeManipulatingURLs + "ms.");
        stringBuffer.append(" Classes loaded: " + this.classesLoadedCount + ".");
        if (useSharedCache) {
            stringBuffer.append(" Shared cache size: " + sharedCache.size());
            stringBuffer.append(" Shared cache (hits/missDueToEviction): (" + cacheHitsShared + "/" + missSharedEvicted + ").");
        } else {
            stringBuffer.append(" Local cache size: " + this.localCache.size());
            stringBuffer.append(" Local cache (hits/missDueToEviction): (" + this.cacheHitsLocal + "/" + this.missLocalEvicted + ").");
        }
        return stringBuffer.toString();
    }

    public long[] reportStats() {
        return new long[]{this.timeSpentLoading, this.timeManipulatingURLs, this.classesLoadedCount, cacheHitsShared, missSharedEvicted, this.cacheHitsLocal, this.missLocalEvicted, sharedCache.size()};
    }

    public void reset() {
        this.timeManipulatingURLs = 0L;
        this.timeSpentLoading = 0L;
        this.classesLoadedCount = 0;
        this.cacheHitsLocal = 0;
        cacheHitsShared = 0;
        missSharedEvicted = 0;
        this.missLocalEvicted = 0;
        this.misses = 0;
        clear();
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public JavaClass loadClass(Class cls) throws ClassNotFoundException {
        return loadClass(cls.getName());
    }

    @Override // org.aspectj.apache.bcel.util.Repository
    public void clear() {
        if (useSharedCache) {
            sharedCache.clear();
        } else {
            this.localCache.clear();
        }
    }

    static /* synthetic */ int access$208() {
        int i = missSharedEvicted;
        missSharedEvicted = i + 1;
        return i;
    }
}
