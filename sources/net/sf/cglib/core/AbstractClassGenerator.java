package net.sf.cglib.core;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import org.objectweb.asm.ClassReader;

/* loaded from: cglib-3.1.jar:net/sf/cglib/core/AbstractClassGenerator.class */
public abstract class AbstractClassGenerator implements ClassGenerator {
    private static final Object NAME_KEY = new Object();
    private static final ThreadLocal CURRENT = new ThreadLocal();
    private Source source;
    private ClassLoader classLoader;
    private String namePrefix;
    private Object key;
    private String className;
    private boolean attemptLoad;
    private GeneratorStrategy strategy = DefaultGeneratorStrategy.INSTANCE;
    private NamingPolicy namingPolicy = DefaultNamingPolicy.INSTANCE;
    private boolean useCache = true;

    protected abstract ClassLoader getDefaultClassLoader();

    protected abstract Object firstInstance(Class cls) throws Exception;

    protected abstract Object nextInstance(Object obj) throws Exception;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: cglib-3.1.jar:net/sf/cglib/core/AbstractClassGenerator$Source.class */
    public static class Source {
        String name;
        Map cache = new WeakHashMap();

        public Source(String name) {
            this.name = name;
        }
    }

    protected AbstractClassGenerator(Source source) {
        this.source = source;
    }

    protected void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    protected final String getClassName() {
        if (this.className == null) {
            this.className = getClassName(getClassLoader());
        }
        return this.className;
    }

    private String getClassName(ClassLoader loader) {
        Set nameCache = getClassNameCache(loader);
        return this.namingPolicy.getClassName(this.namePrefix, this.source.name, this.key, new Predicate(this, nameCache) { // from class: net.sf.cglib.core.AbstractClassGenerator.1
            private final Set val$nameCache;
            private final AbstractClassGenerator this$0;

            {
                this.this$0 = this;
                this.val$nameCache = nameCache;
            }

            @Override // net.sf.cglib.core.Predicate
            public boolean evaluate(Object arg) {
                return this.val$nameCache.contains(arg);
            }
        });
    }

    private Set getClassNameCache(ClassLoader loader) {
        return (Set) ((Map) this.source.cache.get(loader)).get(NAME_KEY);
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public void setNamingPolicy(NamingPolicy namingPolicy) {
        if (namingPolicy == null) {
            namingPolicy = DefaultNamingPolicy.INSTANCE;
        }
        this.namingPolicy = namingPolicy;
    }

    public NamingPolicy getNamingPolicy() {
        return this.namingPolicy;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public boolean getUseCache() {
        return this.useCache;
    }

    public void setAttemptLoad(boolean attemptLoad) {
        this.attemptLoad = attemptLoad;
    }

    public boolean getAttemptLoad() {
        return this.attemptLoad;
    }

    public void setStrategy(GeneratorStrategy strategy) {
        if (strategy == null) {
            strategy = DefaultGeneratorStrategy.INSTANCE;
        }
        this.strategy = strategy;
    }

    public GeneratorStrategy getStrategy() {
        return this.strategy;
    }

    public static AbstractClassGenerator getCurrent() {
        return (AbstractClassGenerator) CURRENT.get();
    }

    public ClassLoader getClassLoader() {
        ClassLoader t = this.classLoader;
        if (t == null) {
            t = getDefaultClassLoader();
        }
        if (t == null) {
            t = getClass().getClassLoader();
        }
        if (t == null) {
            t = Thread.currentThread().getContextClassLoader();
        }
        if (t == null) {
            throw new IllegalStateException("Cannot determine classloader");
        }
        return t;
    }

    protected Object create(Object key) {
        try {
            Class gen = null;
            synchronized (this.source) {
                ClassLoader loader = getClassLoader();
                Map cache2 = (Map) this.source.cache.get(loader);
                if (cache2 == null) {
                    cache2 = new HashMap();
                    cache2.put(NAME_KEY, new HashSet());
                    this.source.cache.put(loader, cache2);
                } else if (this.useCache) {
                    Reference ref = (Reference) cache2.get(key);
                    gen = (Class) (ref == null ? null : ref.get());
                }
                if (gen == null) {
                    Object save = CURRENT.get();
                    CURRENT.set(this);
                    try {
                        this.key = key;
                        if (this.attemptLoad) {
                            try {
                                gen = loader.loadClass(getClassName());
                            } catch (ClassNotFoundException e) {
                            }
                        }
                        if (gen == null) {
                            byte[] b = this.strategy.generate(this);
                            String className = ClassNameReader.getClassName(new ClassReader(b));
                            getClassNameCache(loader).add(className);
                            gen = ReflectUtils.defineClass(className, b, loader);
                        }
                        if (this.useCache) {
                            cache2.put(key, new WeakReference(gen));
                        }
                        Object objFirstInstance = firstInstance(gen);
                        CURRENT.set(save);
                        return objFirstInstance;
                    } catch (Throwable th) {
                        CURRENT.set(save);
                        throw th;
                    }
                }
                return firstInstance(gen);
            }
        } catch (Error e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new CodeGenerationException(e4);
        }
    }
}
